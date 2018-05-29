package org.activiti.incubator.taskservice.domain;

public enum State {

    ACTIVE ("active"),
    ASSIGNED ("assigned"),
    SUSPENDED ("suspended"),
    COMPLETED("completed"),
    ANY("any"); //findAll case

    private String content;

    State (String content){
        this.content = content;
    }

    public String content(){
        return this.content;
    }
}


