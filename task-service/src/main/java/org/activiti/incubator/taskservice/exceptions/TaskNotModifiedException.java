package org.activiti.incubator.taskservice.exceptions;

public class TaskNotModifiedException extends RuntimeException{

    public String message;

    public TaskNotModifiedException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }

}
