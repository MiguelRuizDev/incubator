package org.activiti.incubator.taskservice.resource;

import org.springframework.hateoas.Resource;

public class MessageResource extends Resource<String> {

    public MessageResource(String content) {
        super(content);
    }
}
