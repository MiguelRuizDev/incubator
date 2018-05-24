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

package org.activiti.runtime.api.conf;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.conf.impl.TaskRuntimeConfigurationImpl;
import org.activiti.runtime.api.event.impl.APITaskAssignedEventConverter;
import org.activiti.runtime.api.event.impl.APITaskCreatedEventConverter;
import org.activiti.runtime.api.event.internal.TaskAssignedEventListenerDelegate;
import org.activiti.runtime.api.event.internal.TaskCreatedEventListenerDelegate;
import org.activiti.runtime.api.event.listener.TaskRuntimeEventListener;
import org.activiti.runtime.api.impl.TaskRuntimeImpl;
import org.activiti.runtime.api.model.FluentTask;
import org.activiti.runtime.api.model.impl.APITaskConverter;
import org.activiti.runtime.api.model.impl.APIVariableInstanceConverter;
import org.activiti.runtime.api.model.impl.FluentTaskImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskRuntimeAutoConfiguration {

    //this bean will be automatically injected inside boot's ObjectMapper
    @Bean
    public Module customizeTaskRuntimeObjectMapper() {
        SimpleModule module = new SimpleModule("mapTaskRuntimeInterfaces",
                                               Version.unknownVersion());
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(FluentTask.class,
                            FluentTaskImpl.class);

        module.setAbstractTypes(resolver);
        return module;
    }

    @Bean
    public TaskRuntime taskRuntime(TaskService taskService,
                                   APITaskConverter taskConverter,
                                   TaskRuntimeConfiguration configuration) {
        return new TaskRuntimeImpl(taskService,
                                   taskConverter,
                                   configuration);
    }

    @Bean
    public APITaskConverter apiTaskConverter(TaskService taskService,
                                             APIVariableInstanceConverter variableInstanceConverter) {
        return new APITaskConverter(taskService,
                                    variableInstanceConverter);
    }

    @Bean
    public TaskRuntimeConfiguration taskRuntimeConfiguration(RuntimeService runtimeService,
                                                             @Autowired(required = false) List<TaskRuntimeEventListener> eventListeners,
                                                             TaskCreatedEventListenerDelegate taskCreatedEventListenerDelegate,
                                                             TaskAssignedEventListenerDelegate taskAssignedEventListenerDelegate) {
        return new TaskRuntimeConfigurationImpl(runtimeService,
                                                eventListeners != null ? eventListeners : Collections.emptyList(),
                                                taskCreatedEventListenerDelegate,
                                                taskAssignedEventListenerDelegate);
    }

    @Bean
    public TaskCreatedEventListenerDelegate taskCreatedEventListenerDelegate(@Autowired(required = false) List<TaskRuntimeEventListener> taskRuntimeEventListeners,
                                                                             APITaskCreatedEventConverter taskCreatedEventConverter) {
        return new TaskCreatedEventListenerDelegate(taskRuntimeEventListeners != null? taskRuntimeEventListeners : Collections.emptyList(),
                                                    taskCreatedEventConverter);
    }

    @Bean
    public TaskAssignedEventListenerDelegate taskAssignedEventListenerDelegate(@Autowired(required = false) List<TaskRuntimeEventListener> taskRuntimeEventListeners,
                                                                               APITaskAssignedEventConverter taskAssignedEventConverter) {
        return new TaskAssignedEventListenerDelegate(taskRuntimeEventListeners,
                                                     taskAssignedEventConverter);
    }

    @Bean
    public APITaskCreatedEventConverter apiTaskCreatedEventConverter(APITaskConverter taskConverter) {
        return new APITaskCreatedEventConverter(taskConverter);
    }

    @Bean
    public APITaskAssignedEventConverter apiTaskAssignedEventConverter(APITaskConverter taskConverter) {
        return new APITaskAssignedEventConverter(taskConverter);
    }
}
