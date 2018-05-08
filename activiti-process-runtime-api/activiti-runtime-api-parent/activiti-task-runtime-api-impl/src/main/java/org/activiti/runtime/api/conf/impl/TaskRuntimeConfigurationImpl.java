/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.runtime.api.conf.impl;

import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.runtime.api.conf.TaskRuntimeConfiguration;
import org.activiti.runtime.api.event.internal.TaskAssignedEventListenerDelegate;
import org.activiti.runtime.api.event.internal.TaskCreatedEventListenerDelegate;
import org.activiti.runtime.api.event.listener.TaskRuntimeEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskRuntimeConfigurationImpl implements TaskRuntimeConfiguration {


    @Autowired
    private RuntimeService runtimeService;

    @Autowired(required = false)
    private List<TaskRuntimeEventListener> eventListeners;

    @Autowired
    private TaskCreatedEventListenerDelegate taskCreatedEventListenerDelegate;

    @Autowired
    private TaskAssignedEventListenerDelegate taskAssignedEventListenerDelegate;

    @PostConstruct
    private void registerEventListeners() {
        runtimeService.addEventListener(taskCreatedEventListenerDelegate, ActivitiEventType.TASK_CREATED);
        runtimeService.addEventListener(taskAssignedEventListenerDelegate, ActivitiEventType.TASK_ASSIGNED);
    }

    @Override
    public List<TaskRuntimeEventListener> eventListeners() {
        return Collections.unmodifiableList(eventListeners);
    }
}