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

package org.activiti.runtime.api.query.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.activiti.runtime.api.query.ProcessInstanceQuery;

public class ProcessInstanceQueryImpl implements ProcessInstanceQuery {

    private final RuntimeService runtimeService;

    private final APIProcessInstanceConverter processInstanceConverter;

    private List<String> processDefinitionKeys = new ArrayList<>();

    public ProcessInstanceQueryImpl(RuntimeService runtimeService,
                                    APIProcessInstanceConverter processInstanceConverter) {
        this.runtimeService = runtimeService;
        this.processInstanceConverter = processInstanceConverter;
    }

    @Override
    public ProcessInstanceQuery filterOnKeys(List<String> processDefinitionKeys) {
        this.processDefinitionKeys.addAll(processDefinitionKeys);
        return this;
    }

    @Override
    public ProcessInstanceQuery filterOnKey(String processDefinitionKey) {
        this.processDefinitionKeys.add(processDefinitionKey);
        return this;
    }

    @Override
    public List<ProcessInstance> processInstances(int startIndex,
                                                  int maxResults) {
        org.activiti.engine.runtime.ProcessInstanceQuery internalQuery = runtimeService.createProcessInstanceQuery();
        if (!processDefinitionKeys.isEmpty()) {
            internalQuery.processDefinitionKeys(new HashSet<>(processDefinitionKeys));
        }
        return processInstanceConverter.from(internalQuery.listPage(startIndex,
                                                                    maxResults));
    }
}
