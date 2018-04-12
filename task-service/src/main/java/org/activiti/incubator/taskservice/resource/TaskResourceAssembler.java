package org.activiti.incubator.taskservice;

import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class TaskAssembler implements ResourceAssembler<Task, TaskResource> {

    @Override
    public TaskResource toResource(Task entity) {

        return new TaskResource(entity);
    }
}

