package org.activiti.incubator.taskservice.resource;

import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.List;

public class TaskResource extends Resource <Task>  {

    public TaskResource(Task content, List<Link> links) {
        super(content, links);
    }

}
