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

package org.activiti.runtime.api.connector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

public class ExecutionContextImpl implements ExecutionContext {

    private Map<String, Object> inboundVariables;
    private Map<String, Object> outBoundVariables = new HashMap<>();
    private String processInstanceId;
    private String processDefinitionId;
    private String flowNodeId;

    public ExecutionContextImpl(DelegateExecution execution) {
        this.inboundVariables = execution.getVariables();
        this.processInstanceId = execution.getProcessInstanceId();
        this.processDefinitionId = execution.getProcessDefinitionId();
        this.flowNodeId = execution.getCurrentActivityId();
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    @Override
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    @Override
    public String getFlowNodeId() {
        return flowNodeId;
    }

    @Override
    public Map<String, Object> getInBoundVariables() {
        return Collections.unmodifiableMap(inboundVariables);
    }

    @Override
    public Map<String, Object> getOutBoundVariables() {
        return Collections.unmodifiableMap(outBoundVariables);
    }

    @Override
    public void addOutBoundVariable(String name,
                                    Object value) {
        outBoundVariables.put(name, value);
    }

    @Override
    public void addOutBoundVariables(Map<String, Object> variables) {
        outBoundVariables.putAll(variables);
    }
}
