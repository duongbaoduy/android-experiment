LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := camdev
LOCAL_SRC_FILES := camdev.cpp

include $(BUILD_SHARED_LIBRARY)
