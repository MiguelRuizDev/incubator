package org.activiti.incubator.taskservice.domain;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Entity
public class Task {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(nullable = false, updatable = false)
    private String id;

    private String title;

    private State state = State.ACTIVE;

    private Long creationDate = System.currentTimeMillis();

    private Long dueDate = System.currentTimeMillis() + addAWeek();

    private String assignedUser = "";

    private Priority priority = Priority.NORMAL;

    private String  parent = null; //stores parent's id; if there is no parent, id = null

    private String description;

    @ElementCollection
    private List<Entry> data = new ArrayList<Entry>(){{
        add(new Entry("a","b"));
        add(new Entry("c","d"));
    }};

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }

    public String getCreationDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(this.creationDate));
    }

    public void setCreationDate(String stringCreationDate) {
        try{
            Date creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringCreationDate);
            this.creationDate = creationDate.getTime();
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public String getDueDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(this.dueDate));
    }

    public void setDueDate(String stringDueDate) {
        try {
            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDueDate);
            this.dueDate = dueDate.getTime();
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public String getAssignedUser() { return assignedUser; }

    public void setAssignedUser(String assignedUser) { this.assignedUser = assignedUser; }

    public Priority getPriority() { return priority; }

    public void setPriority(Priority priority) { this.priority = priority; }

    public String getParent() { return parent; }

    public void setParent(String parent) { this.parent = parent; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List <Entry> getData() {
        return data;
    }

    public void setData(List <Entry> information) {
        this.data = information;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                ", assignedUser='" + assignedUser + '\'' +
                ", priority=" + priority +
                ", parent='" + parent + '\'' +
                ", description='" + description + '\'' +
                ", information=" + data +
                '}';
    }

    private long addAWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }

}
