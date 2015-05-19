###################################
#### main 
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    				:= audiodev

LOCAL_SRC_FILES 				:= audiodev.cpp
LOCAL_C_INCLUDES				:= $(LOCAL_PATH)

LOCAL_LDLIBS   += -llog -ldl -lOpenSLES -lm -landroid -lz 
LOCAL_CXXFLAGS   +=
LOCAL_STATIC_LIBRARIES    = audio-device utility  system_wrappers    
 
include $(BUILD_SHARED_LIBRARY)

###################################
include $(JNI_DIR)/system_wrappers.mk
include $(JNI_DIR)/utility.mk
include $(JNI_DIR)/audio_device.mk

$(call import-module,cpufeatures)