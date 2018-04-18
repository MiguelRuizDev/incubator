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

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.runtime.api.events.RuntimeEvent;
import org.activiti.runtime.api.events.RuntimeEventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerDelegate<ENUM_TYPE extends Enum<ENUM_TYPE>,
        EVENT_TYPE extends RuntimeEvent<ENUM_TYPE, ?>> implements ActivitiEventListener {

    private final List<RuntimeEventListener<ENUM_TYPE, EVENT_TYPE>> eventListeners;

    private final EventsConverter<EVENT_TYPE> eventsConverter;

    public EventListenerDelegate(List<RuntimeEventListener<ENUM_TYPE, EVENT_TYPE>> eventListeners,
                                 EventsConverter<EVENT_TYPE> eventsConverter) {
        this.eventListeners = eventListeners;
        this.eventsConverter = eventsConverter;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        for (RuntimeEventListener<ENUM_TYPE, EVENT_TYPE> listener : eventListeners) {
            EVENT_TYPE runtimeEvent = eventsConverter.from(event);
            if (runtimeEvent != null && listener.getHandledEvent().equals(runtimeEvent.getType())) {
                listener.onEvent(runtimeEvent);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

}
