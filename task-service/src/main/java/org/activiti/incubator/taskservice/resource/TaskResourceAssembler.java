package org.activiti.incubator.taskservice.resource;

import org.activiti.incubator.taskservice.controller.TaskController;
import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TaskResourceAssembler implements ResourceAssembler <Task, TaskResource> {

    @Override
    public TaskResource toResource(Task entity) {

        Link selfRel = linkTo(methodOn(TaskController.class).findById(entity.getId())).withSelfRel();

        return new TaskResource(entity, selfRel);
    }
}

