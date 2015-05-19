#include <jni.h>

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "audio_device.h"
#include "utility/interface/process_thread.h"
#include <pthread.h>
#include "system_wrappers/interface/trace.h"

#include <android/log.h>
#define LOG_TAG "WebRtc ADM TEST"
#define TEST_LOG(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define TEST_LOG_ERROR(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define JOW(rettype,name) extern "C" rettype JNIEXPORT JNICALL  Java_org_dbd_exp_android_ui_activity_AudioDevice_##name

#define THIS_FILE "audiodev.cpp"

// Abort the process if |x| is false, emitting |msg|.
#define CHECK(x, msg)                                                         \
	if (x) {} else {                                                          \
		TEST_LOG("%s :  %d : %s  ", __FILE__ , __LINE__ , msg);             \
		abort();                                                              \
	}

//// Abort the process if |jni| has a Java exception pending, emitting |msg|.
//
//#define CHECK_EXCEPTION(jni, msg)                                              \
//	if (0) {} else {                                                           \
//		if (jni->ExceptionCheck()) {                                           \
//			jni->ExceptionDescribe();                                          \
//			jni->ExceptionClear();                                             \
//			CHECK(0, msg);                                                     \
//		}                                                                      \
//	}
//
//// Helper that calls ptr->Release() and logs a useful message if that didn't
//// actually delete *ptr because of extra refcounts.
//
//#define CHECK_RELEASE(ptr)                                        		\
//	do {                                                            	\
//		int count = (ptr)->Release();                                 	\
//		if (count != 0) {                                             	\
//			TEST_LOG( "Refcount unexpectedly not 0: %d : %d " , (ptr)  	\
//					,count);                             				\
//		}                                                             	\
//		CHECK(!count, "Unexpected refcount");                         	\
//	} while 	(0)

// Helper functions
#if defined(ANDROID)
char filenameStr[2][256] = { { 0 }, { 0 }, }; // Allow two buffers for those API calls taking two filenames
int currentStr = 0;

const char* GetFilename(const char* filename) {
	currentStr = !currentStr;
	sprintf(filenameStr[currentStr], "/sdcard/admtest/%s", filename);
	return filenameStr[currentStr];
}
#elif !defined(WEBRTC_IOS)
const char* GetFilename(const char* filename) {
	std::string full_path_filename = webrtc::test::OutputPath() + filename;
	return full_path_filename.c_str();
}
#endif

using namespace webrtc;

class AudioEventObserverAPI: public AudioDeviceObserver {
public:
	AudioEventObserverAPI(AudioDeviceModule* audioDevice) :
			error_(kRecordingError), warning_(kRecordingWarning), audio_device_(
					audioDevice) {
	}

	~AudioEventObserverAPI() {
	}

	virtual void OnErrorIsReported(const ErrorCode error) {
		TEST_LOG("\n[*** ERROR ***] => OnErrorIsReported(%d)\n\n", error);
		error_ = error;
	}

	virtual void OnWarningIsReported(const WarningCode warning) {
		TEST_LOG("\n[*** WARNING ***] => OnWarningIsReported(%d)\n\n", warning);
		warning_ = warning;
		assert(0 == audio_device_->StopRecording());
		assert(0 == audio_device_->StopPlayout());
	}

public:
	ErrorCode error_;
	WarningCode warning_;
private:
	AudioDeviceModule* audio_device_;
};

class AudioTransportAPI: public AudioTransport {
public:
	AudioTransportAPI(AudioDeviceModule* audioDevice) :
			rec_count_(0), play_count_(0) {
	}

	~AudioTransportAPI() {
	}

	virtual int32_t RecordedDataIsAvailable(const void* audioSamples,
			const uint32_t nSamples, const uint8_t nBytesPerSample,
			const uint8_t nChannels, const uint32_t sampleRate,
			const uint32_t totalDelay, const int32_t clockSkew,
			const uint32_t currentMicLevel, const bool keyPressed,
			uint32_t& newMicLevel) {
		rec_count_++;
		if (rec_count_ % 100 == 0) {
			if (nChannels == 1) {
				// mono
				TEST_LOG("-");
			} else if ((nChannels == 2) && (nBytesPerSample == 2)) {
				// stereo but only using one channel
				TEST_LOG("-|");
			} else {
				// stereo
				TEST_LOG("--");
			}
		}
		return 0;
	}

	virtual int32_t NeedMorePlayData(const uint32_t nSamples,
			const uint8_t nBytesPerSample, const uint8_t nChannels,
			const uint32_t sampleRate, void* audioSamples,
			uint32_t& nSamplesOut, int64_t* elapsed_time_ms,
			int64_t* ntp_time_ms) {
		play_count_++;
		if (play_count_ % 100 == 0) {
			if (nChannels == 1) {
				TEST_LOG("+");
			} else {
				TEST_LOG("++");
			}
		}
		nSamplesOut = 480;
		return 0;
	}

	virtual int OnDataAvailable(const int voe_channels[],
			int number_of_voe_channels, const int16_t* audio_data,
			int sample_rate, int number_of_channels, int number_of_frames,
			int audio_delay_milliseconds, int current_volume, bool key_pressed,
			bool need_audio_processing) {
		return 0;
	}

	virtual void PushCaptureData(int voe_channel, const void* audio_data,
			int bits_per_sample, int sample_rate, int number_of_channels,
			int number_of_frames) {
	}

	virtual void PullRenderData(int bits_per_sample, int sample_rate,
			int number_of_channels, int number_of_frames, void* audio_data,
			int64_t* elapsed_time_ms, int64_t* ntp_time_ms) {
	}
private:
	uint32_t rec_count_;
	uint32_t play_count_;
};

class AudioDeviceAPITest {
protected:
	AudioDeviceAPITest() {
	}

	virtual ~AudioDeviceAPITest() {
	}

public:

	static void test_run() {
		static ProcessThread* process_thread_;
		static AudioDeviceModule* audio_device_;
		static AudioTransportAPI* audio_transport_;
		static AudioEventObserverAPI* event_observer_;
		process_thread_ = ProcessThread::CreateProcessThread();
		process_thread_->Start();

		// Windows:
		//      if (WEBRTC_WINDOWS_CORE_AUDIO_BUILD)
		//          user can select between default (Core) or Wave
		//      else
		//          user can select between default (Wave) or Wave
		const int32_t kId = 444;

		// Fails tests
		assert(
				(audio_device_ = AudioDeviceModuleImpl::Create(kId, AudioDeviceModule::kWindowsWaveAudio)) == NULL);
		assert(
				(audio_device_ = AudioDeviceModuleImpl::Create(kId, AudioDeviceModule::kWindowsCoreAudio)) == NULL);
		assert(
				(audio_device_ = AudioDeviceModuleImpl::Create(kId, AudioDeviceModule::kLinuxAlsaAudio)) == NULL);
		assert(
				(audio_device_ = AudioDeviceModuleImpl::Create(kId, AudioDeviceModule::kLinuxPulseAudio)) == NULL);
		// Create default implementation instance
		assert(
				(audio_device_ = AudioDeviceModuleImpl::Create(kId, AudioDeviceModule::kPlatformDefaultAudio)) != NULL);

		if (audio_device_ == NULL) {
			TEST_LOG_ERROR("Failed creating audio device object!");
		}

		// The ADM is reference counted.
		audio_device_->AddRef();

		process_thread_->RegisterModule(audio_device_);

		AudioDeviceModule::AudioLayer audio_layer =
				AudioDeviceModule::kPlatformDefaultAudio;
		assert(0 == audio_device_->ActiveAudioLayer(&audio_layer));

		if (process_thread_) {
			process_thread_->DeRegisterModule(audio_device_);
			process_thread_->Stop();
			ProcessThread::DestroyProcessThread(process_thread_);
		}
		audio_transport_ = new AudioTransportAPI(audio_device_);

//		if (event_observer_) {
//			delete event_observer_;
//			event_observer_ = NULL;
//		}
//		if (audio_transport_) {
//			delete audio_transport_;
//			audio_transport_ = NULL;
//		}

//		if (audio_device_) {
//			assert(0 == audio_device_->Release());
//		}

		audio_device_->RegisterAudioCallback(audio_transport_);

		audio_device_->Init();

		audio_device_->SetRecordingDevice(0);
		audio_device_->SetPlayoutDevice(0);

		// initialize for audio device module, before audio stream start
		audio_device_->InitRecording();
		audio_device_->InitPlayout();

//		assert(0 == audio_device_->Initialized());
//
//		assert( 0 == audio_device_->PlayoutIsInitialized());
//		assert( 0 == audio_device_->Playing());
//		assert( 0 == audio_device_->SpeakerIsInitialized());
//
//		assert( 0 == audio_device_->RecordingIsInitialized());
//		assert( 0 == audio_device_->Recording());
//		assert( 0 == audio_device_->MicrophoneIsInitialized());

		audio_device_->StartPlayout();
		audio_device_->StartRecording();
	}

};

static JavaVM* g_jvm;

extern "C" jint JNIEXPORT JNICALL JNI_OnLoad(JavaVM * jvm, void * reserved) {
	CHECK(!g_jvm, "JNI_OnLoad called more than once!");
	g_jvm = jvm;
	CHECK(g_jvm, "JNI_OnLoad handed NULL?");

//	CHECK(!pthread_once(&g_jni_ptr_once, &CreateJNIPtrKey), "pthread_once");

//	CHECK(talk_base::InitializeSSL(), "Failed to InitializeSSL()");

	JNIEnv* jni;
	if (jvm->GetEnv(reinterpret_cast<void**>(&jni), JNI_VERSION_1_6) != JNI_OK)
		return -1;
//	g_class_reference_holder = new ClassReferenceHolder(jni);

	return JNI_VERSION_1_6;
}

JOW(jboolean, NativeInit)(JNIEnv *env, jclass thiz) {
	return JNI_TRUE;
}

JOW(jint, RunTest)(JNIEnv *env, jclass thiz, jint type) {
	webrtc_trace_create();
	AudioDeviceAPITest::test_run();
	return 0;
}

JOW(jint, AudioInit)(JNIEnv *env, jclass thiz, jobject obj_context) {
#ifdef WEBRTC_ANDROID
	typedef webrtc::AudioDeviceTemplate<webrtc::OpenSlesInput,
			webrtc::OpenSlesOutput> AudioDeviceInstance;
	if (g_jvm && env && obj_context) {
		AudioDeviceInstance::SetAndroidAudioDeviceObjects(g_jvm, env,
				obj_context);
	} else {
		AudioDeviceInstance::ClearAndroidAudioDeviceObjects();
	}
	return 0;
#else
	return -1;
#endif
}
