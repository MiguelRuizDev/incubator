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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.runtime.api.events.RuntimeEvent;
import org.activiti.runtime.api.model.impl.ListConverter;

public class EventsConverter<EVENT_TYPE extends RuntimeEvent<?>> extends ListConverter<ActivitiEvent, EVENT_TYPE> {

    private Map<ActivitiEventType, EventConverter<EVENT_TYPE>> convertersMap;

    public EventsConverter(Map<ActivitiEventType, EventConverter<EVENT_TYPE>> convertersMap) {
        this.convertersMap = convertersMap;
    }

    public EventsConverter(List<EventConverter<EVENT_TYPE>> converters) {
        this.convertersMap = converters.stream().collect(Collectors.toMap(EventConverter::getHandledEvent,
                                                                          Function.identity()));
    }

    @Override
    public EVENT_TYPE from(ActivitiEvent activitiEvent) {
        EventConverter<EVENT_TYPE> eventConverter = convertersMap.get(activitiEvent.getType());
        if (eventConverter != null) {
            return eventConverter.from(activitiEvent);
        }
        return null;
    }



}
