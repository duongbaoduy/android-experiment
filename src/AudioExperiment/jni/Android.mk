LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := audiodev
LOCAL_SRC_FILES := audiodev.cpp

include $(BUILD_SHARED_LIBRARY)
