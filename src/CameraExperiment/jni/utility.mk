###################################
# utility
LOCAL_PATH := $(call my-dir)/utility
include $(CLEAR_VARS)

LOCAL_MODULE    				:= utility
LOCAL_CPP_EXTENSION 			:= .cc

CLASSES_FILES 					:= \
								audio_frame_operations.cc	\
								helpers_android.cc     \
								process_thread_impl.cc \

CLASSES_FILES  					:= $(patsubst  %, source/%,$(CLASSES_FILES))

LOCAL_SRC_FILES 				:= $(CLASSES_FILES)
LOCAL_C_INCLUDES				:= \
								$(JNI_DIR)

#LOCAL_CFLAGS 					:= $(ZALO_CFLAGS)
#LOCAL_CPPFLAGS					:= $(LOCAL_CFLAGS) $(ZALO_CPPFLAGS)

TARGET_ARFLAGS 					:= crsT

#LOCAL_EXPORT_C_INCLUDES 		:= $(LOCAL_C_INCLUDES)
##LOCAL_EXPORT_CFLAGS				:= $(LOCAL_CFLAGS)
#LOCAL_EXPORT_LDLIBS 			:= $(LOCAL_LDLIBS)

include $(BUILD_STATIC_LIBRARY)

###################################