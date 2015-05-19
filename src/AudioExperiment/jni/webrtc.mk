export WEBRTC_SRC_PATH = $(SRC_DIR)webrtc/src
export WEBRTC_SRC_DIR = $(WEBRTC_SRC_PATH)/
export WEBRTC_DEBUG ?=
ifneq ($(WEBRTC_DEBUG),)
$(info WEBRTC_SRC_DIR --  $(WEBRTC_SRC_DIR))
endif

# These defines will apply to all source files
# Think again before changing it
MY_WEBRTC_COMMON_DEFS := \
    -DWEBRTC_TARGET_PC \
    -DWEBRTC_LINUX \
    -DWEBRTC_THREAD_RR \
    -DWEBRTC_CLOCK_TYPE_REALTIME \
    -DWEBRTC_ANDROID

ifeq ($(USE_OPENSLES), 1) 
MY_WEBRTC_COMMON_DEFS += \
   	-DWEBRTC_ANDROID_OPENSLES
endif

ifeq ($(TARGET_ARCH),arm)
MY_WEBRTC_COMMON_DEFS += \
    -DWEBRTC_ARCH_ARM \
    -DWEBRTC_DETECT_ARM_NEON # only used in a build configuration without Neon
# TODO(kma): figure out if the above define could be moved to NDK build only.

# TODO(kma): test if the code under next two macros works with generic GCC compilers
ifeq ($(ARCH_ARM_HAVE_NEON),true)
MY_WEBRTC_COMMON_DEFS += \
    -DWEBRTC_ARCH_ARM_NEON
endif

ifneq (,$(filter -DWEBRTC_DETECT_ARM_NEON -DWEBRTC_ARCH_ARM_NEON, \
    $(MY_WEBRTC_COMMON_DEFS)))
WEBRTC_BUILD_NEON_LIBS := true
# TODO(kma): Use MY_ARM_CFLAGS_NEON for Neon libraies in AECM, NS, and iSAC.
MY_ARM_CFLAGS_NEON := \
    -mfpu=neon \
    -mfloat-abi=softfp \
    -flax-vector-conversions
endif

ifeq ($(ARCH_ARM_HAVE_ARMV7A),true)
MY_WEBRTC_COMMON_DEFS += \
    -DWEBRTC_ARCH_ARM_V7A
endif

endif # ifeq ($(TARGET_ARCH),arm)

# webrtc deps
export ZALO_CFLAGS += $(MY_ARM_CFLAGS_NEON)
export ZALO_DEPS += $(MY_WEBRTC_COMMON_DEFS) -DWEBRTC_ARCH_ARM_V7
include $(call all-subdir-makefiles)