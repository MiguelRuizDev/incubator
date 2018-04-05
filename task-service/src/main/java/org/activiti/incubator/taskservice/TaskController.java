package org.activiti.incubator.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;

import java.util.List;

@RestController
@RequestMapping(path = "/tasks/")
public class TaskController {

    private TaskRepository taskRepository;

    @Autowired
    private TaskAssembler taskAssembler;

    private PagedResourcesAssembler <Task> pagedResourcesAssembler;

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskAssembler taskAssembler, PagedResourcesAssembler pagedResourcesAssembler ) {
        this.taskRepository = taskRepository;
        this.taskAssembler = taskAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(path= "search/findByTitle/{title}", method = RequestMethod.GET)
    public List<Task> findByTitle(@PathVariable("title") String title){
        return taskRepository.findByTitle(title);
    }



    @RequestMapping(path = "{id}/suspend", method = RequestMethod.POST)
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

