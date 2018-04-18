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

import java.util.List;

import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.VariableInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ProcessRuntimeSignalIT {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Test
    public void processShouldTakeExceptionPathWhenSignalIsSent() {
        //given
        ProcessInstance processInstance = processRuntime
                .processDefinitionByKey("ProcessWithBoundarySignal")
                .start();

        //when
        processRuntime
                .sendSignalWith()
                .name("go")
                .variable("signalVar",
                          "mySignalVariable")
                .doIt();

        //then
        await().untilAsserted(() -> {

            List<Task> tasks = taskRuntime.tasks(0,
                                                 1000);
            assertThat(tasks)
                    .filteredOn(task -> task.getProcessInstanceId().equals(processInstance.getId()))
                    .extracting(Task::getName)
                    .contains("Boundary target");
        });

        //when
        List<VariableInstance> variables = processInstance.variables();

        //then
        assertThat(variables)
                .extracting(VariableInstance::getName,
                            VariableInstance::getValue)
                .contains(tuple("signalVar",
                                "mySignalVariable"));
    }
}
