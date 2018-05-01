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

package org.activiti.runtime.api;

import org.activiti.runtime.api.events.AssignTaskListener;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TaskRuntimeListenersIT {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private AssignTaskListener assignTaskListener;

    @Before
    public void setUp() {
        assignTaskListener.clear();
    }

    @Test
    public void taskCreatedShouldTriggerOnCreateTask() {
        //when: the first task will be created as consequence of process start
        ProcessInstance processInstance = processRuntime
                .processDefinitionByKey("SimpleProcess")
                .start();

        //then: the task should be assigned by the listener
        Task currentTask = getCurrentTask(processInstance);

        assertThat(currentTask).isNotNull();
        assertThat(currentTask.getAssignee()).isEqualTo(assignTaskListener.getUsername());
    }

    @Test
    public void taskAssignedShouldTriggerOnAssignTask() {
        //when: the first task will be created as consequence of process start
        ProcessInstance processInstance = processRuntime
                .processDefinitionByKey("SimpleProcess")
                .start();

        //then: the task should be assigned by the listener
        Task currentTask = getCurrentTask(processInstance);
        currentTask.claim("jack");

        assertThat(assignTaskListener.getAssignedTasks()).containsEntry(currentTask.getId(), "jack");
    }

    private Task getCurrentTask(ProcessInstance processInstance) {
        return taskRuntime.tasks(0,
                                           500)
                    .stream()
                    .filter(task -> task.getProcessInstanceId().equals(processInstance.getId()))
                    .findFirst().orElse(null);
    }
}
