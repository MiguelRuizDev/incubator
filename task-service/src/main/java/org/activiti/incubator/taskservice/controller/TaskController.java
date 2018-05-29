package org.activiti.incubator.taskservice.controller;

import org.activiti.incubator.taskservice.exceptions.StateNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotModifiedException;
import org.activiti.incubator.taskservice.resource.*;
import org.activiti.incubator.taskservice.domain.Task;
import org.activiti.incubator.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.activiti.incubator.taskservice.domain.State;
import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private TaskService taskService;

    private TaskResourceAssembler taskResourceAssembler;

    private PagedResourcesAssembler <Task> pagedResourcesAssembler;

    @Autowired
    public TaskController(TaskService taskService,
                          TaskResourceAssembler taskResourceAssembler,
                          PagedResourcesAssembler pagedResourcesAssembler){

        this.taskService = taskService;
        this.taskResourceAssembler = taskResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TaskResource> createTask (@RequestBody Task task) {
        return new ResponseEntity<>(taskResourceAssembler.toResource(taskService.saveTask(task)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity <PagedResources<TaskResource>> findAll (@RequestParam(value="state", defaultValue= "any") String state,
                                                                                                                    Pageable page){
        try{
            Page <Task> pages = taskService.findAll(State.valueOf(state.toUpperCase()), page);

            return new ResponseEntity<>(pagedResourcesAssembler.toResource(pages,taskResourceAssembler), HttpStatus.OK );

        }catch (IllegalArgumentException ex){
            throw new StateNotFoundException("State " + state.toUpperCase() + " does not exist. ");
        }
    }

////    OLD ANGULAR COMPATIBLE VERSION
//
//    @GetMapping
//    public ResponseEntity <Collection<TaskResource>> findAll(@RequestParam(value="state", defaultValue= "any") String state,
//                                                                                                               Pageable page){
//        try{
//            Page <Task> pages = taskService.findAll(State.valueOf(state.toUpperCase()), page);
//
//            return new ResponseEntity<>(pagedResourcesAssembler.toResource(pages,taskResourceAssembler).getContent(), HttpStatus.OK );
//
//        }catch (IllegalArgumentException ex){
//            throw new StateNotFoundException("State " + state.toUpperCase() + " does not exist. ");
//        }
//    }

    @GetMapping (path = "/{id}")
    public TaskResource findById(@PathVariable("id") String id){

        Task task = taskService.findById(id);

        return taskResourceAssembler.toResource(task);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<TaskResource> updateTask (@RequestBody Task task) {
        return new ResponseEntity<>(taskResourceAssembler.toResource(taskService.saveTask(task)), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/suspend")
    public ResponseEntity<TaskResource> suspendTask(@PathVariable("id") String id) {

        Task task = taskService.suspendTask(id);

        return new ResponseEntity<>(taskResourceAssembler.toResource(task), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/activate")
    public ResponseEntity<TaskResource> activateTask (@PathVariable("id") String id){

        Task task = taskService.activateTask(id);
        return new ResponseEntity<>(taskResourceAssembler.toResource(task), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/complete")
    public ResponseEntity<TaskResource> completeTask (@PathVariable("id") String id){

        Task task = taskService.completeTask(id);
        return new ResponseEntity<>(taskResourceAssembler.toResource(task), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/assign")
    public ResponseEntity<TaskResource> assignTask (@PathVariable("id") String id, @RequestParam(value="user") String user){
        Task task = taskService.assignTask(id, user);
        return new ResponseEntity<>(taskResourceAssembler.toResource(task), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/release")
    public ResponseEntity<TaskResource> releaseTask (@PathVariable("id") String id){
        Task task = taskService.releaseTask(id);
        return new ResponseEntity<>(taskResourceAssembler.toResource(task), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}/delete")
    public ResponseEntity<TaskResource> deleteTask (@PathVariable("id") String id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResource handlerTaskNotFoundException(TaskNotFoundException ex) {
        return new MessageResource(ex.getMessage());
    }

    @ExceptionHandler(TaskNotModifiedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public MessageResource handlerTaskNotModifiedException(TaskNotModifiedException ex) {
        return new MessageResource(ex.getMessage());
    }

    @ExceptionHandler(StateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResource  handlerStateNotFoundException(StateNotFoundException ex){
        return new MessageResource(ex.getMessage());
    }
}