###################################
#### main module zalo-audio-device
LOCAL_PATH := $(call my-dir)/audio_device
include $(CLEAR_VARS)

LOCAL_MODULE    				:= audio-device
LOCAL_CPP_EXTENSION 			:= .cc

CLASSES_FILES   				:= \
							audio_device_buffer.cc                    \
							audio_device_buffer.h                     \
							audio_device_config.h                     \
							audio_device_generic.cc                   \
							audio_device_generic.h                    \
							audio_device_impl.cc                      \
							audio_device_impl.h                       \
							audio_device_utility.cc                   \
							audio_device_utility.h                    \
							mock_audio_device_buffer.h                \
							android/audio_common.h                    \
							android/audio_device_template.h           \
							android/audio_device_utility_android.cc   \
							android/audio_device_utility_android.h    \
							android/audio_manager_jni.cc              \
							android/audio_manager_jni.h               \
							android/audio_record_jni.cc               \
							android/audio_record_jni.h                \
							android/audio_track_jni.cc                \
							android/audio_track_jni.h                 \
							android/fine_audio_buffer.cc              \
							android/fine_audio_buffer.h               \
							android/low_latency_event_posix.cc        \
							android/low_latency_event.h               \
							android/opensles_common.cc                \
							android/opensles_common.h                 \
							android/opensles_input.cc                 \
							android/opensles_input.h                  \
							android/opensles_output.cc                \
							android/opensles_output.h                 \
							android/single_rw_fifo.cc                 \
							android/single_rw_fifo.h                  \
							dummy/audio_device_dummy.cc               \
							dummy/audio_device_dummy.h                \
							dummy/audio_device_utility_dummy.cc       \
							dummy/audio_device_utility_dummy.h        \
							dummy/file_audio_device_factory.cc        \
							dummy/file_audio_device_factory.h         \
							dummy/file_audio_device.cc                \
							dummy/file_audio_device.h               


#CLASSES_FILES  					:= $(patsubst %,,$(CLASSES_FILES))
LOCAL_SRC_FILES 				:= $(CLASSES_FILES)
LOCAL_C_INCLUDES				:= \
								$(JNI_DIR) \
								$(LOCAL_PATH)/android	
								
#LOCAL_CFLAGS 					:= $(ZALO_CFLAGS)
##LOCAL_CFLAGS					+= -DGTEST_HAS_POSIX_RE=0 -DGTEST_LANG_CXX11=0 -DPLATFORM_ANDROID -fvisibility=hidden
# 
#LOCAL_CPPFLAGS 					:= $(LOCAL_CFLAGS) $(ZALO_CPPFLAGS)  

TARGET_ARFLAGS 					:= crsT
					
#LOCAL_EXPORT_C_INCLUDES 		:= $(LOCAL_C_INCLUDES)
#LOCAL_EXPORT_LDLIBS 			:= $(LOCAL_LDLIBS)

include $(BUILD_STATIC_LIBRARY)

###################################