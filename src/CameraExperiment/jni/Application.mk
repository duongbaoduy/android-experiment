JNI_DIR := $(call my-dir)
export PATH := /cygdrive/c/tools/cygwin/bin:$(PATH) # /cygdrive/d/tools/swigwin-3.0.2:
SHELL=/bin/bash
SWIG ?= E:\dbd\dbd_workdspace\dbd_tools\bootstrap\window-x86_32\swigwin-3.0.2\swig
MKDIR ?= mkdir 


# Create a Local.mk file if you need to customize values below

APP_OPTIME        := release
APP_ABI := armeabi #armeabi-v7a# x86 mips

APP_PLATFORM := android-19
APP_STL := stlport_static

# These defines will apply to all source files
# Think again before changing it
MY_WEBRTC_COMMON_DEFS := \
    -DWEBRTC_TARGET_PC \
    -DWEBRTC_LINUX \
    -DWEBRTC_THREAD_RR \
    -DWEBRTC_CLOCK_TYPE_REALTIME \
    -DWEBRTC_ANDROID
    
MY_WEBRTC_COMMON_DEFS += \
   	-DWEBRTC_ANDROID_OPENSLES

# Enable c++11 extentions in source code
APP_CFLAGS  	:= -g -ggdb -D__GXX_EXPERIMENTAL_CXX0X__ -D_GNU_SOURCE $(MY_WEBRTC_COMMON_DEFS)
APP_CPPFLAGS 	+= -g -ggdb -std=gnu++11 -fpermissive
# or use APP_CPPFLAGS := -std=gnu++11 -std=c++11