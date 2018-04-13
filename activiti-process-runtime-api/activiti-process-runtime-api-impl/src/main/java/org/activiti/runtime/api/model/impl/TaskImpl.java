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

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.activiti.engine.TaskService;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.VariableInstance;
import org.activiti.runtime.api.model.builder.CompleteTaskPayload;

public class TaskImpl implements Task {

    private final TaskService taskService;
    private final APIVariableInstanceConverter variableInstanceConverter;

    private String id;
    private String owner;
    private String assignee;
    private String name;
    private String description;
    private Date createdDate;
    private Date claimedDate;
    private Date dueDate;
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String parentTaskId;
    private TaskStatus status;

    public TaskImpl(TaskService taskService,
                    APIVariableInstanceConverter variableInstanceConverter,
                    String id,
                    String name,
                    TaskStatus status) {
        this.taskService = taskService;
        this.variableInstanceConverter = variableInstanceConverter;
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getClaimedDate() {
        return claimedDate;
    }

    public void setClaimedDate(Date claimedDate) {
        this.claimedDate = claimedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Task.TaskStatus getStatus() {
        return status;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public <T> void variable(String name,
                                               T value) {
        taskService.setVariable(getId(), name, value);
    }

    @Override
    public <T> void localVariable(String name,
                                  T value) {
        taskService.setVariableLocal(getId(), name, value);
    }

    @Override
    public List<VariableInstance> variables() {
        return variableInstanceConverter.from(taskService.getVariableInstances(getId()).values());
    }

    @Override
    public List<VariableInstance> localVariables() {
        return variableInstanceConverter.from(taskService.getVariableInstancesLocal(getId()).values());
    }

    @Override
    public void complete() {
        taskService.complete(getId());
    }

    @Override
    public CompleteTaskPayload completeWith() {
        return new CompleteTaskPayloadImpl(taskService,
                                           getId());
    }

    @Override
    public void claim(String username) {
        taskService.setAssignee(getId(),
                                username);
    }

    @Override
    public void release() {
        taskService.unclaim(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskImpl task = (TaskImpl) o;
        return priority == task.priority &&
                Objects.equals(id,
                               task.id) &&
                Objects.equals(owner,
                               task.owner) &&
                Objects.equals(assignee,
                               task.assignee) &&
                Objects.equals(name,
                               task.name) &&
                Objects.equals(description,
                               task.description) &&
                Objects.equals(createdDate,
                               task.createdDate) &&
                Objects.equals(claimedDate,
                               task.claimedDate) &&
                Objects.equals(dueDate,
                               task.dueDate) &&
                Objects.equals(processDefinitionId,
                               task.processDefinitionId) &&
                Objects.equals(processInstanceId,
                               task.processInstanceId) &&
                Objects.equals(parentTaskId,
                               task.parentTaskId) &&
                status == task.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id,
                            owner,
                            assignee,
                            name,
                            description,
                            createdDate,
                            claimedDate,
                            dueDate,
                            priority,
                            processDefinitionId,
                            processInstanceId,
                            parentTaskId,
                            status);
    }
}
