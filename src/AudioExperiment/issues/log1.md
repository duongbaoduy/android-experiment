E/trace_impl.cc(31684): CRITICAL  ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; unable to create the platform specific audio device implementation
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; ~AudioDeviceModuleImpl destroyed
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:383 |    0) AUDIO DEVICE:         -1;     32120; ~AudioDeviceBuffer destroyed
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:383 |    0) AUDIO DEVICE:         -1;     32120; AudioDeviceBuffer created
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; AudioDeviceModuleImpl created
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; CheckPlatform
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; current platform is ANDROID
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; dbd - CreatePlatformSpecificObjects
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; output: kPlatformDefaultAudio
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:383 |    0) AUDIO DEVICE:    0   444;     32120; Create AudioDeviceTemplate
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:398 |   15) AUDIO DEVICE:    0   444;     32120; Android OpenSLES Audio APIs will be utilized
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:398 |    0) AUDIO DEVICE:    0   444;     32120; AudioDeviceUtilityAndroid created
E/trace_impl.cc(31684): DEBUGINFO ; ( 8:57: 7:398 |    0) AUDIO DEVICE:    0   444;     32120; AttachAudioBuffer
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:398 |    0) AUDIO DEVICE:    0   444;     32120; AudioDeviceBuffer::SetId(id=444)
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 7:398 |    0) AUDIO DEVICE:    0   444;     32120; output: NOT_SUPPORTED
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 7:399 |    1)      UTILITY:         -1;     32122; Thread with name:ProcessThread started 
E/trace_impl.cc(31684): ERROR     ; ( 8:57: 7:399 |    0)      UTILITY:         -1;     32122; unable to set thread priority
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 7:400 |    1)      UTILITY:         -1;     32122; Thread with name:ProcessThread stopped
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 7:408 |    8) AUDIO DEVICE:    0   444;     32120;   OS info: Android
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:412 |    4) AUDIO DEVICE:    0   444;     32120; AudioDeviceBuffer::SetPlayoutSampleRate(fsHz=44100)
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:412 |    0) AUDIO DEVICE:    0   444;     32120; AudioDeviceBuffer::SetPlayoutChannels(channels=1)
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:413 |    1) AUDIO DEVICE:    0   444;     32120; AudioDeviceBuffer::SetRecordingSampleRate(fsHz=44100)
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:413 |    0) AUDIO DEVICE:    0   444;     32120; AudioDeviceBuffer::SetRecordingChannels(channels=1)
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:413 |    0) AUDIO DEVICE:    0   444;     32120; InitRecording
E/trace_impl.cc(31684): MEMORY    ; ( 8:57: 7:413 |    0) AUDIO DEVICE:    0   444;     32120; InitPlayout
W/AudioRecord(31684): AUDIO_INPUT_FLAG_FAST denied by client
E/trace_impl.cc(31684): STATEINFO ; ( 8:57: 8: 58 |    0)      UTILITY:         -1;     32124; Thread with name:opensl_play_thread started 
E/trace_impl.cc(31684): ERROR     ; ( 8:57: 8: 61 |    3)      UTILITY:         -1;     32124; unable to set thread priority
E/AudioRecord(31684): AudioFlinger could not create record track, status: -1
E/trace_impl.cc(31684): WARNING   ; ( 8:57: 8:110 |   49) AUDIO DEVICE:    0   444;     32124; Audio underrun
E/trace_impl.cc(31684): WARNING   ; ( 8:57: 8:111 |    1) AUDIO DEVICE:    0   444;     32124; Audio underrun
E/libOpenSLES(31684): android_audioRecorder_realize(0x52c71768) error creating AudioRecord object
W/libOpenSLES(31684): Leaving Object::Realize (SL_RESULT_CONTENT_UNSUPPORTED)
A/libc(31684): jni/audio_device/android/opensles_input.cc:345: bool webrtc::OpenSlesInput::CreateAudioRecorder(): assertion "false" failed
E/trace_impl.cc(31684): ERROR     ; ( 8:57: 8:122 |   11) AUDIO DEVICE:    0   444;     32120; OpenSL error: 9
A/libc(31684): Fatal signal 6 (SIGABRT) at 0x00007bc4 (code=-6), thread 32120 (Thread-3799)
