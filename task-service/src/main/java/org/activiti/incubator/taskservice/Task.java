package org.activiti.incubator.taskservice;

import java.util.Date;

import javax.persistence.*;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String title;

    private State state = State.ACTIVE;

    @Temporal(TemporalType.TIMESTAMP) //make sure this is a DATE+TIME in the database
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    private String assignedUser;
    private int priority; //from 1 down to 10 (DESC)

    private Long  parent; //stores parent's id; if there is no parent, id = null

    private String content;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public Date getCreationDate() { return creationDate; }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Date getDueDate() {return dueDate; }

    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getAssignedUser() { return assignedUser; }

    public void setAssignedUser(String assignedUser) { this.assignedUser = assignedUser; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }

    public Long getParent() { return parent; }

    public void setParent(Long parent) { this.parent = parent; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

}
