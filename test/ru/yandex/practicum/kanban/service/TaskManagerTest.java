package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    // да, можно обойтись без дженереков, но это было требование ТЗ
    // * Чтобы избежать дублирования кода, необходим базовый класс с тестами на каждый метод из интерфейса
    // abstract class TaskManagerTest<T extends TaskManager>.

    @Test
    void createTask_shouldCreateNewTask() {
        List<Task> expected = List.of(Task.builder().withId(1).withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().build());
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual);
    }

    @Test
    void createEpic_shouldCreateNewEpic() {
        List<Epic> expected = List.of(Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).build());
        taskManager.createEpic(Epic.builder().withId(1).withName("task").build());
        taskManager.createEpic(Epic.builder().build());
        List<Epic> actual = taskManager.getAllEpics();

        assertEquals(expected, actual);
    }

    @Test
    void createSubtask_shouldCreateNewSubtask() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().build());
        taskManager.createSubtask(Subtask.builder().withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(5).build());
        taskManager.createSubtask(Subtask.builder().withName("task").withStatus(TaskStatus.NEW).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T03:15:00.0")).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T08:15:00.0")).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T08:15:00.0")).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T13:15:00.0")).withEpicId(3).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T13:15:00.0")).withEpicId(3).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.IN_PROGRESS).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T18:15:00.0")).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.IN_PROGRESS).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T18:15:00.0")).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.IN_PROGRESS).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T18:15:00.0")).withEpicId(4).build());

        List<Subtask> expected1 = List.of(
                Subtask.builder().withId(5).withName("task").withStatus(TaskStatus.NEW).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-06-29T03:15:00.0")).withEpicId(1).build(),
                Subtask.builder().withId(6).withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withEpicId(1).build(),
                Subtask.builder().withId(7).withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-06-29T08:15:00.0")).withEpicId(2).build(),
                Subtask.builder().withId(8).withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T08:15:00.0")).withEpicId(2).build(),
                Subtask.builder().withId(9).withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-06-29T13:15:00.0")).withEpicId(3).build(),
                Subtask.builder().withId(10).withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T13:15:00.0")).withEpicId(3).build(),
                Subtask.builder().withId(11).withName("sub").withStatus(TaskStatus.IN_PROGRESS).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-06-29T18:15:00.0")).withEpicId(4).build(),
                Subtask.builder().withId(12).withName("sub").withStatus(TaskStatus.IN_PROGRESS).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T18:15:00.0")).withEpicId(4).build()
        );
        Epic expected2 = Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).withSubtaskIds(List.of(5, 6))
                .withDuration(500).withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).build();
        Epic expected3 = Epic.builder().withId(2).withName("task").withStatus(TaskStatus.IN_PROGRESS).withSubtaskIds(List.of(7, 8))
                .withDuration(500).withStartTime(LocalDateTime.parse("2015-05-29T08:15:00.0")).build();
        Epic expected4 = Epic.builder().withId(3).withName("task").withStatus(TaskStatus.DONE).withSubtaskIds(List.of(9, 10))
                .withDuration(500).withStartTime(LocalDateTime.parse("2015-05-29T13:15:00.0")).build();
        Epic expected5 = Epic.builder().withId(4).withName("task").withStatus(TaskStatus.IN_PROGRESS).withSubtaskIds(List.of(11, 12))
                .withDuration(500).withStartTime(LocalDateTime.parse("2015-05-29T18:15:00.0")).build();
        List<Subtask> actual1 = taskManager.getAllSubtasks();
        Epic actual2 = taskManager.getEpicById(1);
        Epic actual3 = taskManager.getEpicById(2);
        Epic actual4 = taskManager.getEpicById(3);
        Epic actual5 = taskManager.getEpicById(4);

        assertAll("Subtask creation test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2),
                () -> assertEquals(expected3, actual3),
                () -> assertEquals(expected4, actual4),
                () -> assertEquals(expected5, actual5));
    }

    @Test
    void findTaskById_shouldReturnTaskFoundById() {
        Task expected = Task.builder().withId(1).withName("task").build();
        taskManager.createTask(Task.builder().withId(1).withName("task").build());
        Task actual1 = taskManager.getTaskById(1);
        Task actual2 = taskManager.getTaskById(5);

        assertAll("Find task test",
                () -> assertEquals(expected, actual1),
                () -> assertNull(actual2));
    }

    @Test
    void findEpicById_shouldReturnEpicFoundById() {
        Epic expected = Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).build();
        taskManager.createEpic(Epic.builder().withId(1).withName("task").build());
        Epic actual1 = taskManager.getEpicById(1);
        Epic actual2 = taskManager.getEpicById(5);

        assertAll("Find task test",
                () -> assertEquals(expected, actual1),
                () -> assertNull(actual2));
    }

    @Test
    void findSubtaskById_shouldReturnSubtaskFoundById() {
        taskManager.createEpic(Epic.builder().withId(1).withName("task").build());
        Subtask expected = Subtask.builder().withId(2).withName("task").withStatus(TaskStatus.NEW).withEpicId(1).build();
        taskManager.createSubtask(Subtask.builder().withId(2).withName("task").withStatus(TaskStatus.NEW).withEpicId(1).build());
        Subtask actual1 = taskManager.getSubtaskById(2);
        Subtask actual2 = taskManager.getSubtaskById(5);

        assertAll("Find task test",
                () -> assertEquals(expected, actual1),
                () -> assertNull(actual2));
    }

    @Test
    void getAllTasks_shouldReturnListOfAllTasks() {
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().build());
        taskManager.createTask(Task.builder().withName("task2").build());
        taskManager.createTask(Task.builder().withId(5).withName("task").build());

        List<Task> expected = List.of(
                Task.builder().withId(1).withName("task").build(),
                Task.builder().withId(2).withName("task2").build(),
                Task.builder().withId(5).withName("task").build()
        );
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual);
    }

    @Test
    void getAllEpics_shouldReturnListOfAllEpics() {
        List<Epic> expected = List.of(
                Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).build(),
                Epic.builder().withId(2).withName("task2").withStatus(TaskStatus.NEW).build(),
                Epic.builder().withId(5).withName("task").withStatus(TaskStatus.NEW).build()
        );
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().build());
        taskManager.createEpic(Epic.builder().withName("task2").build());
        taskManager.createEpic(Epic.builder().withId(5).withName("task").build());
        List<Epic> actual = taskManager.getAllEpics();

        assertEquals(expected, actual);
    }

    @Test
    void getAllSubtasks_shouldReturnListOfAllSubtasks() {
        taskManager.createEpic(Epic.builder().withId(1).withName("task").build());
        List<Subtask> expected = List.of(Subtask.builder().withId(2).withName("task")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("task")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().build());
        taskManager.createSubtask(Subtask.builder().withName("sub").build());
        List<Subtask> actual = taskManager.getAllSubtasks();

        assertEquals(expected, actual);
    }

    @Test
    void getPriorityTasks_shouldReturnListOfAllTasksPrioritizedByTime() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").withDuration(5000L)
                .withStartTime(LocalDateTime.parse("2015-08-29T03:15:00.0")).build());
        taskManager.createTask(Task.builder().withName("task")
                .withStartTime(LocalDateTime.parse("2015-07-29T03:15:00.0")).build());
        taskManager.createSubtask(Subtask.builder().withName("task").withStatus(TaskStatus.NEW).withEpicId(1)
                .withStartTime(LocalDateTime.parse("2015-06-15T03:16:00.0")).build());
        taskManager.createTask(Task.builder().withName("task")
                .withStartTime(LocalDateTime.parse("2015-05-29T03:45:00.0")).withDuration(50).build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").withDuration(5000L)
                .withStartTime(LocalDateTime.parse("2015-08-30T03:15:00.0")).build());
        taskManager.createTask(Task.builder().withName("task").withDuration(5000L)
                .withStartTime(LocalDateTime.parse("2015-08-30T13:15:00.0")).build());

        List<Task> expected = List.of(
                Task.builder().withId(5).withName("task")
                        .withStartTime(LocalDateTime.parse("2015-05-29T03:45:00.0")).withDuration(50).build(),
                Subtask.builder().withId(4).withName("task").withStatus(TaskStatus.NEW).withEpicId(1)
                        .withStartTime(LocalDateTime.parse("2015-06-15T03:16:00.0")).build(),
                Task.builder().withId(3).withName("task")
                        .withStartTime(LocalDateTime.parse("2015-07-29T03:15:00.0")).build(),
                Task.builder().withId(2).withName("task").withDuration(5000L)
                        .withStartTime(LocalDateTime.parse("2015-08-29T03:15:00.0")).build(),
                Task.builder().withId(6).withName("task").build(),
                Task.builder().withId(7).withName("task").build()
        );
        List<Task> actual = taskManager.getTasksPrioritizedByTime();

        assertEquals(expected, actual);
    }

    @Test
    void updateTask_shouldUpdateTaskWithSpecificId() {
        taskManager.createTask(Task.builder().withName("task").withDuration(150000L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).build());
        taskManager.updateTask(Task.builder().withId(1).withName("task1").withDuration(15L)
                .withStartTime(LocalDateTime.parse("2016-05-29T03:15:00.0")).build());
        taskManager.updateTask(Task.builder().withId(1).build());
        Task expected = Task.builder().withId(1).withName("task1").withDuration(15L)
                .withStartTime(LocalDateTime.parse("2016-05-29T03:15:00.0")).build();
        Task actual = taskManager.getTaskById(1);

        assertEquals(expected, actual);
    }

    @Test
    void updateEpic_shouldUpdateEpicWithSpecificId() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.updateEpic(Epic.builder().withId(1).withName("task").withDescription("epic").build());
        taskManager.updateEpic(Epic.builder().withId(1).build());

        Epic expected = Epic.builder().withId(1).withName("task").withDescription("epic").withStatus(TaskStatus.NEW).build();
        Epic actual = taskManager.getEpicById(1);

        assertEquals(expected, actual);
    }

    @Test
    void updateSubtask_shouldUpdateSubtaskWithSpecificId() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("task").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-07-29T03:15:00.0")).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("task").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T03:15:00.0")).withEpicId(2).build());
        taskManager.updateSubtask(Subtask.builder().withId(3).withName("task").withStatus(TaskStatus.IN_PROGRESS).withDuration(500L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withEpicId(2).build());

        Subtask expected1 = Subtask.builder().withId(3).withName("task").withStatus(TaskStatus.IN_PROGRESS).withDuration(500L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withEpicId(2).build();
        Epic expected2 = Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW)
                .withDuration(Duration.ZERO.toMinutes()).build();
        Epic expected3 = Epic.builder().withId(2).withName("task").withStatus(TaskStatus.IN_PROGRESS).withDuration(750L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withSubtaskIds(List.of(4, 3)).build();
        Subtask actual1 = taskManager.getSubtaskById(3);
        Epic actual2 = taskManager.getEpicById(1);
        Epic actual3 = taskManager.getEpicById(2);
        System.out.println(actual2);

        assertAll("Subtask updating test with migration between Epics:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2),
                () -> assertEquals(expected3, actual3));
    }

    @Test
    void deleteTaskById_shouldDeleteTaskById() {
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.deleteTaskById(2);

        List<Task> expected = List.of(
                Task.builder().withId(1).withName("task").build(),
                Task.builder().withId(3).withName("task").build()
        );
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual);
    }

    @Test
    void deleteEpicById_shouldDeleteEpicAndLinkedSubtasksByEpicId() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.deleteEpicById(2);

        List<Epic> expected1 = List.of(
                Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).withSubtaskIds(List.of(4, 5)).build(),
                Epic.builder().withId(3).withName("task").withStatus(TaskStatus.NEW).build()
        );
        List<Subtask> expected2 = List.of(
                Subtask.builder().withId(4).withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build(),
                Subtask.builder().withId(5).withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build()
        );
        List<Epic> actual1 = taskManager.getAllEpics();
        List<Subtask> actual2 = taskManager.getAllSubtasks();

        assertAll("Epic deleting test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2));
    }

    @Test
    void deleteSubtaskById_shouldDeleteSubtaskById() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-06-29T03:15:00.0")).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.DONE).withDuration(250L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());

        Epic expected = Epic.builder().withId(1).withName("task").withStatus(TaskStatus.IN_PROGRESS).withDuration(500L)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withSubtaskIds(List.of(3, 4)).build();
        Epic actual = taskManager.getEpicById(1);

        assertEquals(expected, actual);

        taskManager.deleteSubtaskById(4);

        List<Epic> expected1 = List.of(
                Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0")).withSubtaskIds(List.of(3)).build(),
                Epic.builder().withId(2).withName("task").withStatus(TaskStatus.NEW).withSubtaskIds(List.of(5, 6)).build()
        );
        List<Subtask> expected2 = List.of(
                Subtask.builder().withId(3).withName("sub").withStatus(TaskStatus.NEW).withDuration(250L)
                        .withStartTime(LocalDateTime.parse("2015-06-29T03:15:00.0")).withEpicId(1).build(),
                Subtask.builder().withId(5).withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build(),
                Subtask.builder().withId(6).withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build()
        );
        List<Epic> actual1 = taskManager.getAllEpics();
        List<Subtask> actual2 = taskManager.getAllSubtasks();

        assertAll("Subtask deleting test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2));
    }

    @Test
    void deleteAllTasks_shouldDeleteAllTasks() {
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.deleteAllTasks();

        List<Task> expected = List.of();
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(expected, actual);
    }

    @Test
    void deleteAllEpics_shouldDeleteAllEpics() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.deleteAllEpics();

        List<Epic> expected1 = List.of();
        List<Subtask> expected2 = List.of();
        List<Epic> actual1 = taskManager.getAllEpics();
        List<Subtask> actual2 = taskManager.getAllSubtasks();

        assertAll("Epic deleting test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2));
    }

    @Test
    void deleteAllSubtasks_shouldDeleteAllSubtasks() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.deleteAllSubtasks();

        List<Subtask> expected1 = List.of();
        List<Epic> expected2 = List.of(
                Epic.builder().withId(1).withName("task").withStatus(TaskStatus.NEW).withDuration(Duration.ZERO.toMinutes()).build(),
                Epic.builder().withId(2).withName("task").withStatus(TaskStatus.NEW).withDuration(Duration.ZERO.toMinutes()).build(),
                Epic.builder().withId(3).withName("task").withStatus(TaskStatus.NEW).withDuration(Duration.ZERO.toMinutes()).build()
        );
        List<Subtask> actual1 = taskManager.getAllSubtasks();
        List<Epic> actual2 = taskManager.getAllEpics();

        assertAll("Epic deleting test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2));
    }

    @Test
    void getSubtasksByEpicId_shouldGetListOfSubtasksLinkedToEpic() {
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createEpic(Epic.builder().withName("task").build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());
        taskManager.createSubtask(Subtask.builder().withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build());

        List<Subtask> expected1 = List.of(
                Subtask.builder().withId(4).withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build(),
                Subtask.builder().withId(5).withName("sub").withStatus(TaskStatus.NEW).withEpicId(1).build()
        );
        List<Subtask> expected2 = List.of(
                Subtask.builder().withId(6).withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build(),
                Subtask.builder().withId(7).withName("sub").withStatus(TaskStatus.NEW).withEpicId(2).build()
        );
        List<Subtask> expected3 = List.of();
        List<Subtask> actual1 = taskManager.getSubtasksByEpicId(1);
        List<Subtask> actual2 = taskManager.getSubtasksByEpicId(2);
        List<Subtask> actual3 = taskManager.getSubtasksByEpicId(3);
        List<Subtask> actual4 = taskManager.getSubtasksByEpicId(5);

        assertAll("Epic deleting test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2),
                () -> assertEquals(expected3, actual3),
                () -> assertNull(actual4));
    }

    @Test
    void getHistory_shouldReturnHistory() {
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.createTask(Task.builder().withName("task").build());
        taskManager.getTaskById(1);
        taskManager.getTaskById(3);
        taskManager.getTaskById(5);
        taskManager.getTaskById(1);

        List<Task> expected = List.of(
                Task.builder().withId(3).withName("task").build(),
                Task.builder().withId(5).withName("task").build(),
                Task.builder().withId(1).withName("task").build()
        );
        List<Task> actual = taskManager.getHistory();

        assertEquals(expected, actual);
    }
}