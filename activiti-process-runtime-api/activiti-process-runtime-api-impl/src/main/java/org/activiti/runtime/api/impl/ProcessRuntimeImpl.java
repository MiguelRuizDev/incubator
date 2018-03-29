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

package org.activiti.runtime.api.impl;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.builder.ProcessStarter;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.activiti.runtime.api.model.impl.ProcessStarterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessRuntimeImpl implements ProcessRuntime {

    private final RuntimeService runtimeService;

    private final APIProcessInstanceConverter processInstanceConverter;

    @Autowired
    public ProcessRuntimeImpl(RuntimeService runtimeService,
                              APIProcessInstanceConverter processInstanceConverter) {
        this.runtimeService = runtimeService;
        this.processInstanceConverter = processInstanceConverter;
    }

    @Override
    public ProcessStarter startProcessWith() {
        return new ProcessStarterImpl(runtimeService,
                                      processInstanceConverter);
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        return null;
    }

    @Override
    public List<ProcessInstance> getProcessInstances(int firstResult,
                                                     int maxResults) {
        return null;
    }
}
