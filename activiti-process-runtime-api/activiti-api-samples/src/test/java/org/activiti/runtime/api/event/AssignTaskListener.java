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

package org.activiti.runtime.api.event;

import java.util.HashMap;
import java.util.Map;

import org.activiti.runtime.api.event.listener.TaskRuntimeEventListener;
import org.activiti.runtime.api.model.Task;
import org.springframework.stereotype.Component;

@Component
public class AssignTaskListener implements TaskRuntimeEventListener {

    public String getUsername() {
        return "listenerUser";
    }

    private Map<String, String> assignedTasks = new HashMap<>();

    @Override
    public void onTaskCreated(TaskCreatedEvent event) {
        Task task = event.getEntity();
        task.claim(getUsername());

    }

    @Override
    public void onTaskAssigned(TaskAssignedEvent event) {
        assignedTasks.put(event.getEntity().getId(), event.getEntity().getAssignee());
    }

    @Override
    public void onTaskSuspended(TaskSuspendedEvent event) {

    }

    @Override
    public void onTaskCompleted(TaskCompletedEvent event) {

    }

    public Map<String, String> getAssignedTasks() {
        return assignedTasks;
    }

    public void clear() {
        assignedTasks.clear();
    }
}
