package org.activiti.incubator.taskservice.service;

import org.activiti.incubator.taskservice.domain.State;
import org.activiti.incubator.taskservice.exceptions.TaskNotFoundException;
import org.activiti.incubator.taskservice.exceptions.TaskNotModifiedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.activiti.incubator.taskservice.repository.TaskRepository;
import org.activiti.incubator.taskservice.domain.Task;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.*;

public class TaskServiceTest {

    @Spy
    @InjectMocks
    private TaskService taskService;

    @Mock
    TaskRepository taskRepositoryMock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void findAllShouldReturnResultOfFindAllWhenStateIsAny() {

        //given
        PageRequest pageable = PageRequest.of(0, 10);

        PageImpl<Task> repositoryPage = new PageImpl<>(Arrays.asList(new Task()), pageable, 1);

        given(taskRepositoryMock.findAll(pageable)).willReturn(repositoryPage);

        //when
        Page<Task> retrievedPage = taskService.findAll(State.ANY, pageable);

        //then
        assertThat(retrievedPage).isEqualTo(repositoryPage);
    }

    @Test
    public void findAllShouldReturnResultOfFindAllByStateWhenStateIsNotAny(){
        //given
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<Task> repositoryPage = new PageImpl<>(Arrays.asList(new Task()), pageable, 1);

        given(taskRepositoryMock.findByState(State.ACTIVE, pageable)).willReturn(repositoryPage);
        //when
        Page <Task> retrievedPage = taskService.findAll(State.ACTIVE, pageable);

        //then
        assertThat(retrievedPage).isEqualTo(repositoryPage);
    }

    @Test
    public void findByIdShouldReturnTaskWithGivenId() {
        //given
        UUID id = UUID.randomUUID();
        Task expectedTask = new Task();
        expectedTask.setId(id);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(expectedTask));

        //when
        Task retrievedTask = taskService.findById(id);

        //then
        assertThat(retrievedTask).isEqualTo(expectedTask);

    }

//    @Test
//    public void findByIdShouldThrowAnExceptionIfTaskIdIsNotPresent() {
//        //given
//        UUID id = UUID.randomUUID();
//        TaskNotFoundException expectedException = new TaskNotFoundException("There is no task with id: " + id);
//        given(taskRepositoryMock.findById(id)).willReturn(Optional.ofNullable(null));
//
//        //when
//        try {
//            Task task = taskService.findById(id);
//            failBecauseExceptionWasNotThrown(TaskNotFoundException.class);
//        }catch (TaskNotFoundException ex){
//            TaskNotFoundException retrievedException = ex;
//
//            //then
//            assertThat(retrievedException.getMessage()).isEqualTo(expectedException.getMessage());
//        }
//
//    }

    @Test
    public void findByIdShouldThrowAnExceptionIfTaskIdIsNotPresent2() {
        //given
        UUID id = UUID.randomUUID();
        given(taskRepositoryMock.findById(id)).willReturn(Optional.ofNullable(null));

        //then
        expectedException.expect(TaskNotFoundException.class);
        expectedException.expectMessage("There is no task with id: " + id);

        //when
        taskService.findById(id);

    }


    @Test
    public void saveTaskShouldSaveGivenTask() {
        //given
        Task taskToBeSaved = new Task();
        UUID id = UUID.randomUUID();
        taskToBeSaved.setId(id);

        given(taskRepositoryMock.save(taskToBeSaved)).willReturn(taskToBeSaved);

        //when
        Task savedTask = taskService.saveTask(taskToBeSaved);

        //then
        assertThat(savedTask).isEqualTo(taskToBeSaved);

    }

    @Test
    public void suspendTaskShouldReturnSuspendedTaskWhenFoundUUID() {

        //given
        Task taskToBeSuspended = new Task();
        UUID id = UUID.randomUUID();
        taskToBeSuspended.setId(id);
        taskToBeSuspended.setState(State.ACTIVE);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeSuspended));

        //when
        Task retrievedTask = taskService.suspendTask(id);

        //then
        assertThat(State.SUSPENDED).isEqualTo(retrievedTask.getState());
    }


    @Test
    public void suspendTaskShouldThrowATaskNotModifiedExceptionWhenStateAlreadySuspended(){
        //given
        Task taskToBeSuspended = new Task();
        UUID id = UUID.randomUUID();
        taskToBeSuspended.setId(id);
        taskToBeSuspended.setState(State.SUSPENDED);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeSuspended));

        //then
        expectedException.expect(TaskNotModifiedException.class);
        expectedException.expectMessage("The task with id: " + taskToBeSuspended.getId() + " could not be modified.");

        //when
        taskService.suspendTask(id);
    }

    @Test
    public void activateTaskShouldReturnActiveTaskWhenFoundUUID() {
        //given
        Task taskToBeActive = new Task();
        UUID id = UUID.randomUUID();
        taskToBeActive.setId(id);
        taskToBeActive.setState(State.SUSPENDED);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeActive));

        //when
        Task retrievedTask = taskService.activateTask(id);

        //then
        assertThat(State.ACTIVE).isEqualTo(retrievedTask.getState());
    }

    @Test
    public void activateTaskShouldThrowATaskNotModifiedExceptionWhenStateNotSuspended(){
        //given
        Task taskToBeActive = new Task();
        UUID id = UUID.randomUUID();
        taskToBeActive.setId(id);
        taskToBeActive.setState(State.ACTIVE);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeActive));

        //then
        expectedException.expect(TaskNotModifiedException.class);
        expectedException.expectMessage("The task with id: " + taskToBeActive.getId() + " could not be modified.");

        //when
        taskService.activateTask(id);
    }

    @Test
    public void completeTaskShouldReturnActiveTaskWhenFoundUUID() {
        //given
        Task taskToBeComplete = new Task();
        UUID id = UUID.randomUUID();
        taskToBeComplete.setId(id);
        taskToBeComplete.setState(State.ASSIGNED);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeComplete));

        //when
        Task retrievedTask = taskService.completeTask(id);

        //then
        assertThat(State.COMPLETED).isEqualTo(retrievedTask.getState());
    }

    @Test
    public void completeTaskShouldThrowATaskNotModifiedExceptionWhenStateNotSuspended(){
        //given
        Task taskToBeActive = new Task();
        UUID id = UUID.randomUUID();
        taskToBeActive.setId(id);
        taskToBeActive.setState(State.ACTIVE);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeActive));

        //then
        expectedException.expect(TaskNotModifiedException.class);
        expectedException.expectMessage("The task with id: " + taskToBeActive.getId() + " could not be modified.");

        //when
        taskService.completeTask(id);
    }

    @Test
    public void assignTaskShouldReturnActiveTaskWhenFoundUUID() {
        //given
        Task taskToBeAssigned = new Task();
        UUID id = UUID.randomUUID();
        taskToBeAssigned.setId(id);
        taskToBeAssigned.setState(State.ACTIVE);
        String user = "Francesco";

        given (taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeAssigned));

        //when
        Task retrievedTask = taskService.assignTask(id, user);

        //then
        assertThat(State.ASSIGNED).isEqualTo(retrievedTask.getState());
        assertThat(user).isEqualTo(retrievedTask.getAssignedUser());
    }

    @Test
    public void assignTaskShouldThrowATaskNotModifiedExceptionWhenStateIsAlreadyAssigned(){
        //given
        Task taskToBeActive = new Task();
        UUID id = UUID.randomUUID();
        taskToBeActive.setId(id);
        taskToBeActive.setState(State.ASSIGNED);
        String user = "Francesco";

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeActive));

        //then
        expectedException.expect(TaskNotModifiedException.class);
        expectedException.expectMessage("The task with id: " + taskToBeActive.getId() + " is already assigned to " + taskToBeActive.getAssignedUser());

        //when
        taskService.assignTask(id, user);
    }

    @Test
    public void releaseTaskShouldReturnActiveTaskWhenFoundUUID() {
        //given
        Task taskToBeAssigned = new Task();
        UUID id = UUID.randomUUID();
        String user = "Francesco";
        taskToBeAssigned.setId(id);
        taskToBeAssigned.setState(State.ASSIGNED);
        taskToBeAssigned.setAssignedUser(user);

        given (taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeAssigned));

        //when
        Task retrievedTask = taskService.releaseTask(id);

        //then
        assertThat(State.ACTIVE).isEqualTo(retrievedTask.getState());
        assertThat("").isEqualTo(retrievedTask.getAssignedUser());
    }

    @Test
    public void releaseTaskShouldReturnThrowATaskNotModifiedExceptionWhenStateIsNotAssigned() {
        //given
        Task taskToBeReleased = new Task();
        UUID id = UUID.randomUUID();
        taskToBeReleased.setId(id);
        taskToBeReleased.setState(State.ACTIVE);

        given(taskRepositoryMock.findById(id)).willReturn(Optional.of(taskToBeReleased));

        //then
        expectedException.expect(TaskNotModifiedException.class);
        expectedException.expectMessage("The task with id: " + taskToBeReleased.getId() + " is not assigned.");

        //when
        taskService.releaseTask(id);
    }

    @Test
    public void showNotAssignedTasksShouldLogNoUnassignedTasks(){

        //given

        PageRequest page = PageRequest.of(0, 10);

        PageImpl<Task> retrievedPage = new PageImpl<>(Collections.emptyList());

        given(taskRepositoryMock.findByState(State.ACTIVE, page)).willReturn(retrievedPage);

        //when
        taskService.showNotAssignedTasks();

        //then
        verify(taskService).log("There are no tasks to be assigned.");

    }


    @Test
    public void showNotAssignedTasksShouldLogUnassignedTasks(){

        //given
        Task assignedTask = new Task();
        assignedTask.setState(State.ACTIVE);
        List <Task> allTasks = Arrays.asList(assignedTask);

        PageRequest page = PageRequest.of(0, 10);
        PageImpl<Task> retrievedPage = new PageImpl<>(allTasks);

        given(taskRepositoryMock.findByState(State.ACTIVE, page)).willReturn(retrievedPage);

        //when
        taskService.showNotAssignedTasks();

        //then
        verify(taskService).log("There are tasks to be assigned: " + allTasks.toString());


    }

    @Test
    public void showDueTasksShouldLogNotDueTasks(){

        //given
        Task notDueTask = new Task();
        notDueTask.setTitle("Not due");
        notDueTask.setDueDate(new Timestamp(System.currentTimeMillis() + 60000));
        List<Task> allTasks = Arrays.asList(notDueTask);

        given(taskRepositoryMock.findAll()).willReturn(allTasks);

        //when
        taskService.showDueTasks();

        //then
        verify(taskService).log("There are no Due tasks.");
    }


    @Test
    public void showDueTasksShouldLogDueTask(){

        //given
        Task dueTask = new Task();
        dueTask.setTitle("Due task");
        dueTask.setDueDate(new Timestamp(System.currentTimeMillis() - 100));

        Task nonDueTask = new Task();
        nonDueTask.setTitle("Not due");
        nonDueTask.setDueDate(new Timestamp(System.currentTimeMillis() + 60000));

        List<Task> allTasks = Arrays.asList(dueTask, nonDueTask);

        given(taskRepositoryMock.findAll()).willReturn(allTasks);

        //when
        taskService.showDueTasks();

        //then
        verify(taskService).log("There are Due tasks: " + Collections.singletonList(dueTask).toString());

    }
}