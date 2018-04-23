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

package org.activiti.runtime.api.events.impl;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.runtime.api.events.ProcessRuntimeEvent;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.springframework.stereotype.Component;

@Component
public class APIProcessInstanceStartedEventConverter implements EventConverter<ProcessRuntimeEvent> {

    private final APIProcessInstanceConverter processInstanceConverter;

    public APIProcessInstanceStartedEventConverter(APIProcessInstanceConverter processInstanceConverter) {
        this.processInstanceConverter = processInstanceConverter;
    }

    @Override
    public ProcessRuntimeEvent from(ActivitiEvent activitiEvent) {
        ExecutionEntity entity = (ExecutionEntity)
                ((ActivitiProcessStartedEvent) activitiEvent).getEntity();
        return new ProcessRuntimeEventImpl(
                processInstanceConverter.from(entity.getProcessInstance()));
    }

    @Override
    public ActivitiEventType getHandledEvent() {
        return ActivitiEventType.PROCESS_STARTED;
    }
}
