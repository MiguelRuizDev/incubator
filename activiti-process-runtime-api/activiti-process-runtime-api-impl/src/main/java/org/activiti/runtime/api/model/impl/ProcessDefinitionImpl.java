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

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.builder.ProcessStarter;
import org.activiti.runtime.api.model.builder.impl.ProcessStarterFactory;

public class ProcessDefinitionImpl implements ProcessDefinition {

    private final ProcessStarterFactory processStarterFactory;
    private final RuntimeService runtimeService;
    private final APIProcessInstanceConverter processInstanceConverter;
    private String id;
    private String name;
    private String description;
    private int version;
    private String key;

    public ProcessDefinitionImpl(ProcessStarterFactory processStarterFactory,
                                 RuntimeService runtimeService,
                                 APIProcessInstanceConverter processInstanceConverter,
                                 String id,
                                 String name,
                                 String description,
                                 int version) {
        this.processStarterFactory = processStarterFactory;
        this.runtimeService = runtimeService;
        this.processInstanceConverter = processInstanceConverter;
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public ProcessStarter startProcessWith() {
        return processStarterFactory.createNewInstance(getId());
    }

    @Override
    public ProcessInstance start() {
        return startProcessWith().doIt();
    }

    @Override
    public List<ProcessInstance> processInstances(int startIndex,
                                                  int maxResults) {
        return processInstanceConverter.from(runtimeService
                .createProcessInstanceQuery()
                .processDefinitionId(getId())
                .listPage(startIndex, maxResults));
    }
}