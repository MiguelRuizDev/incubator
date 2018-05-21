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

package org.conf.activiti.runtime.api;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.conf.ProcessRuntimeConfiguration;
import org.activiti.runtime.api.conf.impl.ProcessRuntimeConfigurationImpl;
import org.activiti.runtime.api.event.impl.APIProcessStartedEventConverter;
import org.activiti.runtime.api.event.internal.ProcessStartedEventListenerDelegate;
import org.activiti.runtime.api.event.listener.ProcessRuntimeEventListener;
import org.activiti.runtime.api.impl.ProcessRuntimeImpl;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.builder.impl.ProcessStarterFactory;
import org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.activiti.runtime.api.model.impl.APIVariableInstanceConverter;
import org.activiti.runtime.api.model.impl.ProcessDefinitionImpl;
import org.activiti.runtime.api.model.impl.ProcessInstanceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessRuntimeAutoConfiguration {

    //this bean will be automatically injected inside boot's ObjectMapper
    @Bean
    public Module customizeProcessRuntimeObjectMapper() {
        SimpleModule module = new SimpleModule("mapProcessRuntimeInterfaces",
                                               Version.unknownVersion());
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ProcessInstance.class,
                            ProcessInstanceImpl.class);
        resolver.addMapping(ProcessDefinition.class,
                            ProcessDefinitionImpl.class);

        module.setAbstractTypes(resolver);
        return module;
    }

    @Bean
    @ConditionalOnMissingBean
    public ProcessRuntime processRuntime(RepositoryService repositoryService,
                                         APIProcessDefinitionConverter processDefinitionConverter,
                                         RuntimeService runtimeService,
                                         APIProcessInstanceConverter processInstanceConverter,
                                         ProcessRuntimeConfiguration processRuntimeConfiguration) {
        return new ProcessRuntimeImpl(repositoryService,
                                      processDefinitionConverter,
                                      runtimeService,
                                      processInstanceConverter,
                                      processRuntimeConfiguration);
    }

    @Bean
    public APIProcessDefinitionConverter apiProcessDefinitionConverter(ProcessStarterFactory processStarterFactory,
                                                                       RuntimeService runtimeService,
                                                                       APIProcessInstanceConverter processInstanceConverter) {
        return new APIProcessDefinitionConverter(processStarterFactory,
                                                 runtimeService,
                                                 processInstanceConverter);
    }

    @Bean
    public ProcessStarterFactory processStarterFactory(RuntimeService runtimeService,
                                                       APIProcessInstanceConverter processInstanceConverter) {
        return new ProcessStarterFactory(runtimeService,
                                         processInstanceConverter);
    }

    @Bean
    public APIProcessInstanceConverter apiProcessInstanceConverter(RuntimeService runtimeService,
                                                                   APIVariableInstanceConverter variableInstanceConverter) {
        return new APIProcessInstanceConverter(runtimeService,
                                               variableInstanceConverter);
    }

    @Bean
    public ProcessRuntimeConfiguration processRuntimeConfiguration(RuntimeService runtimeService,
                                                                   ProcessStartedEventListenerDelegate processStartedEventListenerDelegate,
                                                                   @Autowired(required = false) List<ProcessRuntimeEventListener> eventListeners) {
        return new ProcessRuntimeConfigurationImpl(runtimeService,
                                                   processStartedEventListenerDelegate,
                                                   eventListeners != null? eventListeners : Collections.emptyList());
    }

    @Bean
    public APIVariableInstanceConverter apiVariableInstanceConverter() {
        return new APIVariableInstanceConverter();
    }

    @Bean
    public ProcessStartedEventListenerDelegate processStartedEventListenerDelegate(
            @Autowired(required = false) List<ProcessRuntimeEventListener> listeners,
            APIProcessStartedEventConverter processStartedEventConverter) {
        return new ProcessStartedEventListenerDelegate(listeners != null ? listeners : Collections.emptyList(),
                                                       processStartedEventConverter);
    }

    @Bean
    public APIProcessStartedEventConverter apiProcessStartedEventConverter(APIProcessInstanceConverter processInstanceConverter) {
        return new APIProcessStartedEventConverter(processInstanceConverter);
    }
}
