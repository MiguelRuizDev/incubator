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

package org.activiti.runtime.api.model.impl;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.builder.ProcessStarter;

public class ProcessStarterImpl implements ProcessStarter {

    private final StartProcessPayloadImpl payload;

    private final RuntimeService runtimeService;

    private final APIProcessInstanceConverter processInstanceConverter;

    public ProcessStarterImpl(RuntimeService runtimeService,
                              APIProcessInstanceConverter processInstanceConverter) {
        this.runtimeService = runtimeService;
        this.processInstanceConverter = processInstanceConverter;
        payload = new StartProcessPayloadImpl();
    }

    public ProcessStarterImpl processDefinitionKey(String processDefinitionKey){
        payload.setProcessDefinitionKey(processDefinitionKey);
        return this;
    }

    public ProcessStarterImpl processDefinitionId(String processDefinitionId){
        payload.setProcessDefinitionId(processDefinitionId);
        return this;
    }

    public ProcessStarterImpl variables(Map<String, Object> variables){
        payload.setVariables(variables);
        return this;
    }

    @Override
    public <T> ProcessStarter variable(String key,
                                       T value) {
        payload.addVariable(key, value);
        return this;
    }

    public ProcessStarterImpl businessKey(String businessKey){
        payload.setBusinessKey(businessKey);
        return this;
    }

    @Override
    public ProcessInstance start() {
        return processInstanceConverter.from(runtimeService
                .createProcessInstanceBuilder()
                .processDefinitionId(payload.getProcessDefinitionId())
                .processDefinitionKey(payload.getProcessDefinitionKey())
                .businessKey(payload.getBusinessKey())
                .variables(payload.getVariables())
                .start());
    }
}
