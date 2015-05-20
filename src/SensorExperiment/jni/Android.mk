###################################
#### main 
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    				:= sensor

LOCAL_SRC_FILES 				:= sensor.cpp
LOCAL_C_INCLUDES				:= $(LOCAL_PATH)

LOCAL_LDLIBS   += -llog -ldl -lOpenSLES -lm -landroid -lz 
LOCAL_CPPFLAGS   +=
LOCAL_STATIC_LIBRARIES    = #cam-device utility  system_wrappers    
 
include $(BUILD_SHARED_LIBRARY)

###################################
#include $(JNI_DIR)/system_wrappers.mk
#include $(JNI_DIR)/utility.mk
#include $(JNI_DIR)/cam_device.mk

$(call import-module,cpufeatures)
