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

import org.activiti.runtime.api.model.builder.CompleteTaskPayload;
import org.activiti.runtime.api.model.Task;

public interface TaskRuntime {

    void claim(String taskId, String userId);

    void release(String taskId);

    void complete(String taskId);

    void complete(CompleteTaskPayload payload);

    List<Task> getTasks(int firstResult, int maxResults);

}
