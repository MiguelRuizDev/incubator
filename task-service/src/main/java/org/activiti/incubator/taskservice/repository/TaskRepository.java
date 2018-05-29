package org.activiti.incubator.taskservice.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;

@RepositoryRestResource (exported = false)
public interface TaskRepository extends PagingAndSortingRepository <Task, String> {

    Page <Task> findByState(@Param("state") State state, Pageable page);

    //methods not used by service yet

    List <Task> findByTitle(@Param("title") String title);

    List<Task> findByCreationDate(@Param("creationDate") Date creation_date);

    List<Task> findByDueDate(@Param("dueDate") Date due_date);

    List<Task> findByAssignedUser(@Param("assignedUser") String name);

    List<Task> findByPriority(@Param("priority") int priority);

    List<Task> findByParent(@Param("parent") Long parent_id);

    List<Task> findByAssignedUserOrderByPriorityDesc(@Param("assignedUser") String assignedUser);

    @Query("select t from Task t where t.parent = ?1")
    List<Task> findAllChildren(@Param("children") Long id);
}


