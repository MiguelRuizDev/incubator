package org.activiti.incubator.taskservice.service;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.activiti.incubator.taskservice.exceptions.TaskNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotModifiedException;
import org.activiti.incubator.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Page<Task> findAll(State state, Pageable page){

        Page<Task> pages;
        if (state != State.ANY){
            pages = taskRepository.findByState(state, page);
        }else{
            pages = taskRepository.findAll(page);
        }
        return pages;
    }

    public Task findById(String id){

        Optional<Task> task = taskRepository.findById(id);

        if(task.isPresent()) {
            return task.get();
        }else{
            throw new TaskNotFoundException("There is no task with id: " + id);
        }
    }

    public Task saveTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask (String id){
        taskRepository.deleteById(id);
    }

    public Task suspendTask(String id){

        Task task = this.findById(id);

        if(task.getState() == State.ACTIVE || task.getState() == State.ASSIGNED ){

            task.setState(State.SUSPENDED);
            taskRepository.save(task);
            return task;

        }else{
            throw new TaskNotModifiedException("The task with id: " + task.getId() + " is already suspended.");
        }

    }

    public Task activateTask(String id){

        Task task = this.findById(id);

        if(task.getState() == State.SUSPENDED){

            task.setState(State.ACTIVE);
            taskRepository.save(task);
            return task;
        }else{
            throw new TaskNotModifiedException("The task with id: " + task.getId() + " could not be modified.");
        }

    }

    public Task completeTask(String id){

        Task task = this.findById(id);

        if(task.getState() == State.ASSIGNED ){

            task.setState(State.COMPLETED);
            taskRepository.save(task);
            return task;
        }else{
            throw new TaskNotModifiedException("The task with id: " + task.getId() + " could not be modified.");
        }
    }

    public Task assignTask (String id, String user){

        Task task = this.findById(id);

        if(task.getState() != State.ASSIGNED ){

            task.setAssignedUser(user);
            task.setState(State.ASSIGNED);
            taskRepository.save(task);
            return task;

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " is already assigned to " + task.getAssignedUser());
        }

    }

    public Task releaseTask (String id){

        Task task = this.findById(id);

        if(task.getState() == State.ASSIGNED ){

            task.setAssignedUser("");
            task.setState(State.ACTIVE);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " is not assigned. ");
        }
        return task;
    }

    @Scheduled(fixedRate = 10000)
    public void showNotAssignedTasks() {
        Pageable page = PageRequest.of(0, 10);
        //List <Task> list = taskRepository.findByState(State.ACTIVE, page).getContent();
        List <Task> list = taskRepository.findByState(State.ACTIVE,page).getContent();
        if(list.isEmpty()){
            log("There are no tasks to be assigned.");
        }else{
            log("There are tasks to be assigned: " + list.toString());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void showDueTasks(){

        Iterator<Task> all = taskRepository.findAll().iterator();

        List <Task> dueTasks = new ArrayList();

        while (all.hasNext()){
            Task currentTask = all.next();

            try{
                Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentTask.getDueDate());
                if (dueDate.getTime() < System.currentTimeMillis()){
                    dueTasks.add(currentTask);
                }
            }catch (ParseException ex){
                ex.printStackTrace();
            }
        }

        if(dueTasks.isEmpty()){
            log("There are no Due tasks.");
        }else{
            log("There are Due tasks: " + dueTasks.toString());
        }

    }

    protected void log(String message) {
    log.info(message);
}
}
