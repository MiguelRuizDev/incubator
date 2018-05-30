package org.activiti.incubator.taskservice.controller;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.domain.Task;
import org.activiti.incubator.taskservice.exceptions.StateNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotFoundException;
import org.activiti.incubator.taskservice.repository.TaskRepository;
import org.activiti.incubator.taskservice.resource.MessageResource;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerIT {

    public static final String TASKS_PATH = "/tasks";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repository;

    @After
    public void cleanDatabase() throws Exception {
        repository.deleteAll();
    }

    private ResponseEntity<Task> createTask(Task inputTask) {
        ResponseEntity<Task> responseEntity = restTemplate.exchange(TASKS_PATH, HttpMethod.POST, new HttpEntity<>(inputTask), new ParameterizedTypeReference<Task>(){});
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        return responseEntity;
    }

    @Test
    public void shouldCreateATask() {
        //given
        Task inputTask = new Task();
        inputTask.setTitle("my task");

        //when
        ResponseEntity<Task> responseEntity = createTask(inputTask);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task retrievedTask = responseEntity.getBody();
        assertThat(retrievedTask).isNotNull();
        assertThat(retrievedTask.getTitle()).isEqualTo("my task");
        assertThat(retrievedTask.getId()).isNotNull();
    }

    @Test
    public void shouldFindAll(){
        //given
        Task task1 = new Task();
        task1.setTitle("Greeting");
        task1.setDescription("Good morning everyone!!");
        task1.setAssignedUser("Ryan");
        task1.setState(State.ASSIGNED);

        createTask(task1);

        Task task2 = new Task();
        task2.setTitle("Saluto");
        task2.setDescription("Buongiorno a tutti!!");

        createTask(task2);

        Task task3 = new Task();
        task3.setTitle("Cumprimento");
        task3.setDescription("Bom dia a todos!!");
        task3.setAssignedUser("Elias");
        task3.setState(State.ASSIGNED);

        createTask(task3);

        //when
        ResponseEntity<PagedResources<Task>> responseEntity = restTemplate.exchange(
                TASKS_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Task>>() {});

        //then
        assertThat(responseEntity.getBody().getContent())
                .extracting(Task::getTitle)
                .contains("Greeting", "Saluto", "Cumprimento");

    }

    @Test
    public void shouldFindByState(){

        //given
        Task task1 = new Task();
        task1.setTitle("Greeting");
        task1.setDescription("Good morning everyone!!");
        task1.setAssignedUser("Ryan");
        task1.setState(State.ASSIGNED);

        createTask(task1);

        Task task2 = new Task();
        task2.setTitle("Saluto");
        task2.setDescription("Buongiorno a tutti!!");

        createTask(task2);

        Task task3 = new Task();
        task3.setTitle("Cumprimento");
        task3.setDescription("Bom dia a todos!!");
        task3.setAssignedUser("Elias");
        task3.setState(State.ASSIGNED);

        createTask(task3);

        //when
        ResponseEntity<PagedResources<Task>> responseEntity = restTemplate.exchange(
                TASKS_PATH + "?state=assigned",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Task>>() {});

        //then
        assertThat(responseEntity.getBody().getContent())
                .extracting(Task::getTitle)
                .contains("Greeting", "Cumprimento");

        assertThat(responseEntity.getBody().getContent())
                .extracting(Task::getTitle)
                .doesNotContain("Saluto");
    }

    @Test
    public void shouldFindById(){

        //given
        Task task1 = new Task();
        task1.setTitle("Greeting");
        task1.setDescription("Good morning everyone!!");
        task1.setState(State.SUSPENDED);

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.getForEntity(TASKS_PATH + "/" + id, Task.class );

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getTitle()).isEqualTo("Greeting");
        assertThat(retrievedTask2.getState()).isEqualTo(State.SUSPENDED);
        assertThat(retrievedTask2.getDescription()).isEqualTo("Good morning everyone!!");
    }

    @Test
    public void shouldSuspendTask(){
        //given
        Task task1 = new Task();

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/suspend", HttpMethod.POST, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getState()).isEqualTo(State.SUSPENDED);
    }

    @Test
    public void shouldActivateTask(){
        //given
        Task task1 = new Task();
        task1.setState(State.SUSPENDED);

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/activate", HttpMethod.POST, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getState()).isEqualTo(State.ACTIVE);
    }

    @Test
    public void shouldCompleteTask(){
        //given
        Task task1 = new Task();
        task1.setState(State.ASSIGNED);

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/complete", HttpMethod.POST, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getState()).isEqualTo(State.COMPLETED);
    }

    @Test
    public void shouldAssignTask(){
        //given
        Task task1 = new Task();
        String user = "Francesco";

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/assign" + "?user=" + user, HttpMethod.POST, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getState()).isEqualTo(State.ASSIGNED);
        assertThat(retrievedTask2.getAssignedUser()).isEqualTo(user);
    }

    @Test
    public void shouldReleaseTask(){
        //given
        Task task1 = new Task();
        task1.setState(State.ASSIGNED);
        task1.setAssignedUser("Francesco");

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/release", HttpMethod.POST, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Task retrievedTask2 = responseEntity2.getBody();
        assertThat(retrievedTask2).isNotNull();
        assertThat(retrievedTask2.getState()).isEqualTo(State.ACTIVE);
        assertThat(retrievedTask2.getAssignedUser()).isEqualTo("");
    }

    @Test
    public void shouldDeleteTask(){
        //given
        Task task1 = new Task();

        //when
        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        ResponseEntity <Task> responseEntity2 = restTemplate.exchange(TASKS_PATH + "/" + id + "/delete", HttpMethod.DELETE, new HttpEntity<>(task1), new ParameterizedTypeReference<Task>(){});

        //then
        assertThat(responseEntity2.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity2.getBody()).isNull();
    }


    @Test
    public void shoudlReturnStateNotFoundException(){
        //given
        String nonExistentState = "foo";

        //when
        ResponseEntity<PagedResources<Task>> responseEntity = restTemplate.exchange(
                TASKS_PATH + "?state=" + nonExistentState,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Task>>() {
                });

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void shoudlReturnTaskNotModifiedException(){

        //given
        Task task1 = new Task();
        task1.setTitle("Greeting");
        task1.setDescription("Good morning everyone!!");
        task1.setAssignedUser("Ryan");
        task1.setState(State.ASSIGNED);

        ResponseEntity<Task> responseEntity1 = createTask(task1);
        Task retrievedTask1 = responseEntity1.getBody();
        String id = retrievedTask1.getId();

        //when
        ResponseEntity<PagedResources<Task>> responseEntity = restTemplate.exchange(
                TASKS_PATH + "/" + id +"/assign?user=Ryan",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Task>>() {
                });

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

    }

    @Test
    public void shoudlReturnTaskNotFoundException(){

        //given
        String nonExistentId = "asdf";

        //when
        ResponseEntity<PagedResources<Task>> responseEntity = restTemplate.exchange(
                TASKS_PATH + nonExistentId +"/assign?user=Ryan",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Task>>() {
                });

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}