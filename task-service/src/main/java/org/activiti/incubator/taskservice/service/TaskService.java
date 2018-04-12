package org.activiti.incubator.taskservice.service;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.activiti.incubator.taskservice.exceptions.TaskNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotModifiedException;
import org.activiti.incubator.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Page<Task> findAll(String state,
                              Pageable page){

        Page<Task> pages;

        switch (state) {
            case "active":
                pages = taskRepository.findByState(State.ACTIVE, page);
                break;
            case "suspended":
                pages = taskRepository.findByState(State.SUSPENDED, page);
                break;
            case "completed":
                pages = taskRepository.findByState(State.COMPLETED, page);
                break;
            case "assigned":
                pages = taskRepository.findByState(State.ASSIGNED, page);
                break;
            default:
                pages = taskRepository.findAll(page);
        }
        return pages;
    }

    public Task findById(Long id){

        if(taskRepository.findById(id).isPresent()) {
            return taskRepository.findById(id).get();
        }else{
            throw new TaskNotFoundException("There is no task with id: " + id);
        }
    }

    public void saveTask(Task task){
        taskRepository.save(task);
    }

    public void deleteTask (Long id){
        taskRepository.deleteById(id);
    }

    public Task suspendTask(Long id){

        Task task = this.findById(id);

        if(task.getState() == State.ACTIVE || task.getState() == State.ASSIGNED ){

            task.setState(State.SUSPENDED);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " could not be modified.");
        }
        return task;
    }

    public Task activateTask(Long id){

        Task task = this.findById(id);

        if(task.getState() == State.SUSPENDED){

            task.setState(State.ACTIVE);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " could not be modified.");
        }
        return task;
    }

    public Task completeTask(Long id){

        Task task = this.findById(id);

        if(task.getState() == State.ASSIGNED ){

            task.setState(State.COMPLETED);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " could not be modified.");
        }
        return task;
    }

    public Task assignTask (Long id, String user){

        Task task = this.findById(id);

        if(task.getState() != State.ASSIGNED ){

            task.setAssignedUser(user);
            task.setState(State.ASSIGNED);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " is already assigned to " + task.getAssignedUser());
        }
        return task;
    }

    public Task releaseTask (Long id){

        Task task = this.findById(id);

        if(task.getState() == State.ASSIGNED ){

            task.setAssignedUser(null);
            task.setState(State.ACTIVE);
            taskRepository.save(task);

        }else{

            throw new TaskNotModifiedException("The task with id: " + task.getId() + " is not assigned. ");
        }
        return task;
    }
}
