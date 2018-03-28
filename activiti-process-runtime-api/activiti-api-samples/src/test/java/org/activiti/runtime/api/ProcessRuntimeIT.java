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

import java.util.HashMap;

import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.StartProcessInstanceCommand;
import org.activiti.runtime.api.model.StartProcessInstanceCommandBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessRuntimeIT {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private StartProcessInstanceCommandBuilder startProcessInstanceCommandBuilder;

    @Test
    public void shouldStartProcessInstance() throws Exception {
        //given
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("firstName",
                      "John");
        variables.put("lastName",
                      "Doe");
        StartProcessInstanceCommand command = startProcessInstanceCommandBuilder
                .withProcessDefinitionKey("SimpleProcess")
                .withBusinessKey("myBusinessKey")
                .withVariables(variables)
                .getCommand();

        //when
        ProcessInstance processInstance = processRuntime.startProcess(command);

        //then
        assertThat(processInstance).isNotNull();
        assertThat(processInstance.getId()).isNotEmpty();
        assertThat(processInstance.getBusinessKey()).isEqualTo("myBusinessKey");
        assertThat(processInstance.getProcessDefinitionId()).isNotEmpty();
        assertThat(processInstance.getProcessDefinitionKey()).isEqualTo("SimpleProcess");
    }



}
