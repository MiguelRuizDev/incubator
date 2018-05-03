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

import java.util.ArrayList;
import java.util.List;

import org.activiti.runtime.api.event.listener.ProcessRuntimeEventListener;
import org.springframework.stereotype.Component;

@Component
public class DummyProcessStartedEventListener implements ProcessRuntimeEventListener {

    private List<String> startedProcesses = new ArrayList<>();

    public List<String> getStartedProcesses() {
        return startedProcesses;
    }

    public void clear() {
        startedProcesses.clear();
    }

    @Override
    public void onProcessStarted(ProcessStartedEvent event) {
        startedProcesses.add(event.getEntity().getId());
    }

    @Override
    public void onProcessSuspended(ProcessSuspendedEvent event) {

    }

    @Override
    public void onProcessResumed(ProcessResumedEvent event) {

    }

    @Override
    public void onProcessCompleted(ProcessCompletedEvent event) {

    }

    @Override
    public void onProcessCancelled(ProcessCancelledEvent event) {

    }

}
