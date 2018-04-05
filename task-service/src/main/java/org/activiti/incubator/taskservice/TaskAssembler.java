package org.activiti.incubator.taskservice;

import org.springframework.hateoas.ResourceAssembler;

public class TaskAssembler implements ResourceAssembler<Task, TaskResource> {

    @Override
    public TaskResource toResource(Task entity) {

        return new TaskResource(entity);
    }
}

