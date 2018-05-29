package org.activiti.incubator.taskservice.resource;

import org.activiti.incubator.taskservice.controller.TaskController;
import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TaskResourceAssembler implements ResourceAssembler <Task, TaskResource> {

    @Override
    public TaskResource toResource(Task entity) {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(TaskController.class).findById(entity.getId())).withSelfRel());
        links.add(linkTo(methodOn(TaskController.class).updateTask(entity)).withRel("Update"));
        links.add(linkTo(methodOn(TaskController.class).deleteTask(entity.getId())).withRel("Delete"));

        if(entity.getState() == State.ACTIVE){
            links.add(linkTo(methodOn(TaskController.class).suspendTask(entity.getId())).withRel("Suspend"));
            links.add(linkTo(methodOn(TaskController.class).completeTask(entity.getId())).withRel("Complete"));
            links.add(linkTo(methodOn(TaskController.class).assignTask(entity.getId(), null)).withRel("Assign"));
        }

        if(entity.getState() == State.ASSIGNED){
            links.add(linkTo(methodOn(TaskController.class).suspendTask(entity.getId())).withRel("Suspend"));
            links.add(linkTo(methodOn(TaskController.class).completeTask(entity.getId())).withRel("Complete"));
            links.add(linkTo(methodOn(TaskController.class).releaseTask(entity.getId())).withRel("Release"));
        }

        if(entity.getState() == State.SUSPENDED){
            links.add(linkTo(methodOn(TaskController.class).activateTask(entity.getId())).withRel("Activate"));
            links.remove(linkTo(methodOn(TaskController.class).updateTask(entity)).withRel("Update"));
        }

        if(entity.getState() == State.COMPLETED){
            links.remove(linkTo(methodOn(TaskController.class).updateTask(entity)).withRel("Update"));
        }

        return new TaskResource(entity, links);
    }
}

