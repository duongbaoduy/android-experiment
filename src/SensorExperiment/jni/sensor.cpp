#include <jni.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <android/log.h>

#define LOG_TAG "WebRtc ADM TEST"

#define TEST_LOG(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

#define TEST_LOG_ERROR(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define JOW(rettype,name) extern "C" rettype JNIEXPORT JNICALL  Java_org_dbd_exp_android_dev_NativeSensor_##name

#define THIS_FILE "sensor.cpp"

// Abort the process if |x| is false, emitting |msg|.

#define CHECK(x, msg)                                                         \
 	if (x) {} else {                                                          \
 		TEST_LOG("%s :  %d : %s  ", __FILE__ , __LINE__ , msg);             \
 		abort();                                                              \
 	}

// Abort the process if |jni| has a Java exception pending, emitting |msg|.

#define CHECK_EXCEPTION(jni, msg)                                              \
 	if (0) {} else {                                                           \
 		if (jni->ExceptionCheck()) {                                           \
 			jni->ExceptionDescribe();                                          \
 			jni->ExceptionClear();                                             \
 			CHECK(0, msg);                                                     \
 		}                                                                      \
 	}

// Helper that calls ptr->Release() and logs a useful message if that didn't
// actually delete *ptr because of extra refcounts.

#define CHECK_RELEASE(ptr)                                        		\
 	do {                                                            	\
 		int count = (ptr)->Release();                                 	\
 		if (count != 0) {                                             	\
 			TEST_LOG( "Refcount unexpectedly not 0: %d : %d " , (ptr)  	\
 					,count);                             				\
 		}                                                             	\
 		CHECK(!count, "Unexpected refcount");                         	\
 	} while 	(0)

// Helper functions

char filenameStr[2][256] = { { 0 }, { 0 }, }; // Allow two buffers for those API calls taking two filenames
int currentStr = 0;

const char* GetFilename(const char* filename) {
	currentStr = !currentStr;
	sprintf(filenameStr[currentStr], "/sdcard/admtest/%s", filename);
	return filenameStr[currentStr];
}

static JavaVM* g_jvm;

extern "C" jint JNIEXPORT JNICALL JNI_OnLoad(JavaVM * jvm, void * reserved) {
	CHECK(!g_jvm, "JNI_OnLoad called more than once!");
	g_jvm = jvm;
	CHECK(g_jvm, "JNI_OnLoad handed NULL?");

	JNIEnv* jni;
	if (jvm->GetEnv(reinterpret_cast<void**>(&jni), JNI_VERSION_1_6) != JNI_OK)
		return -1;
//	g_class_reference_holder = new ClassReferenceHolder(jni);

	return JNI_VERSION_1_6;
}

JOW(jint, SensorInit)(JNIEnv *env, jclass thiz) {
	return 0;
}

JOW(jint, RunTest)(JNIEnv *env, jclass thiz, jint type) {
	return 0;
}

