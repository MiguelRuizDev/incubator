package org.activiti.incubator.taskservice.resource;

import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class TaskResource extends Resource <Task>  {

    public TaskResource(Task content, Link... links) {
        super(content, links);
    }

}
