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

import org.activiti.engine.RepositoryService;
import org.activiti.runtime.api.NotFoundException;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter;
import org.springframework.stereotype.Component;

@Component
public class ProcessRuntimeImpl implements ProcessRuntime {

    private final RepositoryService repositoryService;

    private final APIProcessDefinitionConverter processDefinitionConverter;

    public ProcessRuntimeImpl(RepositoryService repositoryService,
                              APIProcessDefinitionConverter processDefinitionConverter) {
        this.repositoryService = repositoryService;
        this.processDefinitionConverter = processDefinitionConverter;
    }

    @Override
    public List<ProcessDefinition> processDefinitions() {
        return processDefinitionConverter.from(repositoryService.createProcessDefinitionQuery().list());
    }

    @Override
    public ProcessDefinition processDefinitionWithKey(String processDefinitionKey) {
        List<org.activiti.engine.repository.ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).list();

        if (processDefinitions == null || processDefinitions.isEmpty()) {
            throw new NotFoundException("No process definition found for key `" + processDefinitionKey + "`");
        }
        return processDefinitionConverter.from(processDefinitions.get(0));
    }
}
