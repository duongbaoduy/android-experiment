###################################
#### main module zalo-audio-device
LOCAL_PATH := $(call my-dir)/cam_device
include $(CLEAR_VARS)

LOCAL_MODULE    				:= cam-device
LOCAL_CPP_EXTENSION 			:= .cc

CLASSES_FILES   				:= \
							device_info_impl.cc       \
							video_capture_factory.cc  \
							video_capture_impl.cc     \
							android/video_capture_android.cc  \
#							android/device_info_android.cc    \


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