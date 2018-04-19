LOCAL_PATH := $(call my-dir)

########################
include $(CLEAR_VARS)

LOCAL_MODULE := com.sprint.internal.id.LauncherFacade.xml

LOCAL_MODULE_CLASS := ETC

LOCAL_MODULE_TAGS := optional

# This will install the file in /system/etc/permissions

LOCAL_MODULE_PATH := $(TARGET_OUT_ETC)/permissions

LOCAL_SRC_FILES := $(LOCAL_MODULE)

include $(BUILD_PREBUILT)

########################

# ====  jar ==================================

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := JAVA_LIBRARIES
LOCAL_MODULE := LauncherFacade
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_REQUIRED_MODULES := com.sprint.internal.id.LauncherFacade.xml

LOCAL_CERTIFICATE := platform
LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_JAVA_LIBRARY)