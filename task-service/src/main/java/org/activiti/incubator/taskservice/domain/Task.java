package org.activiti.incubator.taskservice.domain;

import org.hibernate.annotations.GenericGenerator;
import java.sql.Timestamp;
import javax.persistence.*;
import java.util.UUID;


@Entity
public class Task {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String title;

    private State state = State.ACTIVE;

    //@Temporal(TemporalType.TIMESTAMP) //make sure this is a DATE+TIME in the database
    private Timestamp creationDate;

    //@Temporal(TemporalType.TIMESTAMP)
    private Timestamp dueDate;

    private String assignedUser;

    private int priority; //from 1 down to 3 (DESC)

    private Long  parent; //stores parent's id; if there is no parent, id = null

    private String content;

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public Timestamp getCreationDate() { return creationDate; }

    public void setCreationDate(Timestamp creationDate) { this.creationDate = creationDate; }

    public Timestamp getDueDate() {return dueDate; }

    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }

    public String getAssignedUser() { return assignedUser; }

    public void setAssignedUser(String assignedUser) { this.assignedUser = assignedUser; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority; }

    public Long getParent() { return parent; }

    public void setParent(Long parent) { this.parent = parent; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", assignedUser='" + assignedUser + '\'' +
                ", priority=" + priority +
                ", parent=" + parent +
                ", content='" + content + '\'' +
                '}';
    }

}
