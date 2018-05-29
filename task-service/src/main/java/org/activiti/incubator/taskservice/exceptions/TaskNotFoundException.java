package org.activiti.incubator.taskservice.exceptions;

public class TaskNotFoundException extends RuntimeException{


    public String message;

    public TaskNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}

