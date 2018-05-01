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

import org.activiti.engine.TaskService;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.config.TaskRuntimeConfiguration;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.impl.APITaskConverter;
import org.springframework.stereotype.Component;

@Component
public class TaskRuntimeImpl implements TaskRuntime {

    private final TaskService taskService;

    private final APITaskConverter taskConverter;

    private final TaskRuntimeConfiguration configuration;

    public TaskRuntimeImpl(TaskService taskService,
                           APITaskConverter taskConverter,
                           TaskRuntimeConfiguration configuration) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
        this.configuration = configuration;
    }

    @Override
    public TaskRuntimeConfiguration configuration() {
        return configuration;
    }

    @Override
    public Task task(String taskId) {
        return taskConverter.from(taskService.createTaskQuery().taskId(taskId).singleResult());
    }

    @Override
    public List<Task> tasks(int firstResult,
                            int maxResults) {
        return taskConverter.from(taskService.createTaskQuery().listPage(firstResult,
                                                                         maxResults));
    }
}
