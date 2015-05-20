###################################
# utility
LOCAL_PATH := $(call my-dir)/system_wrappers
include $(CLEAR_VARS)

LOCAL_MODULE    				:= system_wrappers
LOCAL_CPP_EXTENSION 			:= .cc

CLASSES_FILES 					:= \
								aligned_malloc.cc                \
								atomic32_posix.cc                \
								clock.cc                         \
								condition_variable.cc            \
								cpu_features.cc                  \
								cpu_info.cc                      \
								critical_section_posix.cc        \
								critical_section.cc              \
								data_log.cc                      \
								file_impl.cc                     \
								logcat_trace_context.cc          \
								logging.cc                       \
								metrics_default.cc               \
								rtp_to_ntp.cc                    \
								rw_lock_generic.cc               \
								rw_lock_posix.cc                 \
								rw_lock.cc                       \
								sleep.cc                         \
								sort.cc                          \
								thread_posix.cc                  \
								event.cc						 \
								event_posix.cc                   \
								thread.cc                        \
								tick_util.cc                     \
								timestamp_extrapolator.cc        \
								trace_impl.cc                    \
								trace_posix.cc

CLASSES_FILES  					:= $(patsubst  %, source/%,$(CLASSES_FILES))

LOCAL_SRC_FILES 				:= $(CLASSES_FILES)
LOCAL_C_INCLUDES				:= \
								$(JNI_DIR)

LOCAL_CPPFLAGS					:=  -DWEBRTC_THREAD_RR -DWEBRTC_CLOCK_TYPE_REALTIME
 
TARGET_ARFLAGS 					:= crsT

include $(BUILD_STATIC_LIBRARY)
###################################