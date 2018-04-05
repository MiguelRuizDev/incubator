package org.activiti.incubator.taskservice;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.jpa.repository.Query;

@RepositoryRestResource (/*collectionResourceRel = "tasks", path = "tasks") */ exported = false)
public interface TaskRepository extends PagingAndSortingRepository <Task, Long>

{

    List<Task> findByTitle(@Param("title") String title);

    List<Task> findByCreationDate(@Param("creationDate") Date creation_date); //not working in URL, don't know how to feed the proper data format
    List<Task> findByDueDate(@Param("dueDate") Date due_date);//same

    List<Task> findByAssignedUser(@Param("assignedUser") String name);

    List<Task> findByPriority(@Param("priority") int priority);

    List<Task> findByParent(@Param("parent") Long parent_id);

    List<Task> findByState(@Param("state") State state);

    //custom queries

    @Query("select t from Task t where t.parent = ?1")
    List<Task> findAllChildren(@Param("children") Long id);

    @Query("select t from Task t where t.assignedUser = ?1 order by priority desc") //we can achieve more functionality
    List<Task> findByPriorityUser(@Param("assignedUser") String assignedUser);
    //the same would be done by: findByAssignedUserOrderByPriorityDesc(@Param("assignedUser") String assignedUser);


}


