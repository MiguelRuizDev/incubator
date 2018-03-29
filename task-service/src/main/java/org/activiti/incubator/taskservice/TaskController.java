package org.activiti.incubator.taskservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    TaskRepository db; //injection

    @RequestMapping(path = "/tasks/{id}/suspend", method = RequestMethod.POST) //whenever you don't know which one to use
    public Task suspendTask(@PathVariable("id") Long id){

        Task task = db.findById(id).get(); //java8 optionals

        //change staste
        if(task.getState() == State.ACTIVE){
            task.setState(State.SUSPENDED);
        }else{

        }
        //save modified object in db
        db.save(task);

        return task;
    }


    @RequestMapping(path = "/tasks/{id}/activate", method = RequestMethod.POST)
    public Task activateTask (@PathVariable("id") Long id){

        Task task = db.findById(id).get();

        if(task.getState() == State.SUSPENDED){
            task.setState(State.ACTIVE);
        }else{

        }

        db.save(task);

        return task;
    }


    @RequestMapping(path = "/taks/{id}/complete", method = RequestMethod.POST)
    public Task completeTask (@PathVariable("id") Long id){

        Task task = db.findById(id).get();

        if(task.getState() == State.ACTIVE){
            task.setState(State.COMPLETED);
        }else{

        }

        db.save(task);

        return task;
    }



}

