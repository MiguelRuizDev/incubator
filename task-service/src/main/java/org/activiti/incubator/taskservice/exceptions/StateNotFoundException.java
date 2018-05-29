package org.activiti.incubator.taskservice.exceptions;

public class StateNotFoundException extends RuntimeException{

    public String message;

    public StateNotFoundException(String message){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}