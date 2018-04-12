package org.activiti.incubator.taskservice;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.activiti.incubator.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private TaskRepository taskRepository;

    private TaskAssembler taskAssembler;

    private PagedResourcesAssembler <Task> pagedResourcesAssembler;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskAssembler taskAssembler, PagedResourcesAssembler pagedResourcesAssembler ) {
        this.taskRepository = taskRepository;
        this.taskAssembler = taskAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /*
    @RequestMapping(path= "search/findByTitle/{title}", method = RequestMethod.GET)
    public PagedResources <Resource <Task> >findByTitle(@PathVariable("title") String title){

        return pagedResourcesAssembler.toResource(taskRepository.findByTitle(title));
    }


    @RequestMapping(path = "demo", method = RequestMethod.GET)
    public PagedResources <Resource <Task> >findAll(){
        return pagedResourcesAssembler.toResource(taskRepository.findAll());
    }*/


    @RequestMapping(path = "/{id}/suspend", method = RequestMethod.POST)
    public Resource <Task> suspendTask(@PathVariable("id") Long id){

        Task task = taskRepository.findById(id).get();

        if(task.getState() == State.ACTIVE){
            task.setState(State.SUSPENDED);
            taskRepository.save(task);
        }else{

        }
        return taskAssembler.toResource(task);
    }


    @RequestMapping(path = "{id}/activate", method = RequestMethod.POST)
    public Resource <Task> activateTask (@PathVariable("id") Long id){

        Task task = taskRepository.findById(id).get();

        if(task.getState() == State.SUSPENDED){
            task.setState(State.ACTIVE);
            taskRepository.save(task);
        }else{

        }
        return taskAssembler.toResource(task);
    }


    @RequestMapping(path = "{id}/complete", method = RequestMethod.POST)
    public Resource <Task> completeTask (@PathVariable("id") Long id){

        Task task = taskRepository.findById(id).get();

        if(task.getState() == State.ACTIVE){
            task.setState(State.COMPLETED);
            taskRepository.save(task);
        }else{

        }
        return taskAssembler.toResource(task);
    }



}

