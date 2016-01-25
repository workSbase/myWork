# Copyright (C) 2010 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_STATIC_JAVA_LIBRARIES :=commons-lang3-3.0-beta gson-2.2.1 android-support-v4 Msc plato-client-api-0.2
include $(BUILD_MULTI_PREBUILT)
LOCAL_MODULE_TAGS := LenovoRobotService   # Allows non-localized strings
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_PACKAGE_NAME := LenovoRobotService
include $(BUILD_PACKAGE)
include $(CLEAR_VARS)  
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := commons-lang3-3.0-beta:libs/commons-lang3-3.0-beta.jar \
plato-client-api-0.2:libs/plato-client-api-0.2.jar \
Msc:libs/Msc.jar \
gson-2.2.1:libs/gson-2.2.1.jar \
android-support-v4:libs/android-support-v4.jar 
LOCAL_MODULE_TAGS := optional  
include $(BUILD_MULTI_PREBUILT)  
# Use the following include to make our testapk.  
include $(callall-makefiles-under,$(LOCAL_PATH))  

