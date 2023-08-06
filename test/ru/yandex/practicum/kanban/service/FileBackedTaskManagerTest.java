package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    public void createFileBackedManager() {
        Path resources = Paths.get("resources", "TasksTest.csv");

        try {
            Files.writeString(resources, "");
        } catch (IOException e) {
            throw new ManagerSaveException("Error. Data have not been written.", e.getCause());
        }

        taskManager = Managers.getBackedTaskManager(resources);
    }

    @Test
    void save_shouldSaveAllTasksToFile() throws IOException {
        taskManager.createTask(Task.builder().withName("task1").withDescription("task1").withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0")).withDuration(250L).build());
        taskManager.createTask(Task.builder().withName("task2").withDescription("task2").withStatus(TaskStatus.DONE)
                .withStartTime(LocalDateTime.parse("2015-05-29T08:30:00.0")).build());
        taskManager.createTask(Task.builder().withName("task3").withDescription("task3")
                .withStatus(TaskStatus.IN_PROGRESS).withDuration(250L).build());

        taskManager.createEpic(Epic.builder().withName("epic1").withDescription("epic1").build());
        taskManager.createEpic(Epic.builder().withName("epic2").withDescription("epic2").build());
        taskManager.createEpic(Epic.builder().withName("epic3").withDescription("epic3").build());

        taskManager.createSubtask(Subtask.builder().withName("sub1").withDescription("sub1").withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2016-05-29T03:30:00.0")).withDuration(250L).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("sub2").withDescription("sub2").withStatus(TaskStatus.DONE)
                .withStartTime(LocalDateTime.parse("2015-05-29T08:30:00.0")).withDuration(250L).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("sub3").withDescription("sub3")
                .withStatus(TaskStatus.NEW).withEpicId(5).build());
        taskManager.createSubtask(Subtask.builder().withName("sub4").withDescription("sub4")
                .withStatus(TaskStatus.NEW).withEpicId(5).build());
        taskManager.createSubtask(Subtask.builder().withName("sub5").withDescription("sub5")
                .withStatus(TaskStatus.DONE).withEpicId(6).build());
        taskManager.createSubtask(Subtask.builder().withName("sub6").withDescription("sub6")
                .withStatus(TaskStatus.DONE).withEpicId(6).build());

        List<String> expected = Files.readAllLines(Paths.get("resources", "TasksBenchmark.csv"));
        List<String> actual = Files.readAllLines(Paths.get("resources", "TasksTest.csv"));

        assertEquals(expected, actual);
    }

    @Test
    void load_shouldLoadAllTasksFromFile() {
        taskManager = Managers.getBackedTaskManager(Paths.get("resources", "TasksBenchmark.csv"));
        // метод loadFromFile() вызывается здесь
        // public static FileBackedTaskManager getBackedTaskManager(Path path) {
        //        return FileBackedTaskManager.loadFromFile(path);
        // }
        //  в одном из прошлых тз его нужно было создать, но по факту до этого момента вызов метода не использовался

        List<Task> expectedTasks = List.of(
                Task.builder().withId(1).withName("task1").withDescription("task1").withStatus(TaskStatus.NEW)
                        .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0")).withDuration(250L).build(),
                Task.builder().withId(2).withName("task2").withDescription("task2").withStatus(TaskStatus.DONE)
                        .withStartTime(LocalDateTime.parse("2015-05-29T08:30:00.0")).withDuration(0).build(),
                Task.builder().withId(3).withName("task3").withDescription("task3")
                        .withStatus(TaskStatus.IN_PROGRESS).withDuration(250L).build()
        );

        List<Epic> expectedEpics = List.of(
                Epic.builder().withId(4).withName("epic1").withDescription("epic1").withDuration(500L)
                        .withStartTime(LocalDateTime.parse("2015-05-29T08:30:00.0"))
                        .withStatus(TaskStatus.IN_PROGRESS).withSubtaskIds(List.of(7, 8)).build(),
                Epic.builder().withId(5).withName("epic2").withDescription("epic2").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.NEW).withSubtaskIds(List.of(9, 10)).build(),
                Epic.builder().withId(6).withName("epic3").withDescription("epic3").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.DONE).withSubtaskIds(List.of(11, 12)).build()
        );

        List<Subtask> expectedSubtasks = List.of(
                Subtask.builder().withId(7).withName("sub1").withDescription("sub1").withStatus(TaskStatus.NEW)
                        .withStartTime(LocalDateTime.parse("2016-05-29T03:30:00.0"))
                        .withDuration(250L).withEpicId(4).build(),
                Subtask.builder().withId(8).withName("sub2").withDescription("sub2").withStatus(TaskStatus.DONE)
                        .withStartTime(LocalDateTime.parse("2015-05-29T08:30:00.0"))
                        .withDuration(250L).withEpicId(4).build(),
                Subtask.builder().withId(9).withName("sub3").withDescription("sub3").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.NEW).withEpicId(5).build(),
                Subtask.builder().withId(10).withName("sub4").withDescription("sub4").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.NEW).withEpicId(5).build(),
                Subtask.builder().withId(11).withName("sub5").withDescription("sub5").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.DONE).withEpicId(6).build(),
                Subtask.builder().withId(12).withName("sub6").withDescription("sub6").withDuration(Duration.ZERO.toMinutes())
                        .withStatus(TaskStatus.DONE).withEpicId(6).build()
        );

        List<Task> actualTasks = taskManager.getAllTasks();
        List<Epic> actualEpics = taskManager.getAllEpics();
        List<Subtask> actualSubtasks = taskManager.getAllSubtasks();

        assertEquals(expectedTasks, actualTasks);
        assertEquals(expectedEpics, actualEpics);
        assertEquals(expectedSubtasks, actualSubtasks);
    }

    @Test
    void load_shouldLoadAllTasksFromHistory() {
        taskManager = FileBackedTaskManager
                .loadFromFile(Paths.get("resources", "TasksTestWithHistory.csv"));

        List<Task> expected = List.of(
                Task.builder().withName("task2").withId(5).withDescription("task5")
                        .withStatus(TaskStatus.DONE).withDuration(Duration.ZERO.toMinutes()).build(),
                Task.builder().withName("task1").withId(4).withDescription("task4")
                        .withStatus(TaskStatus.NEW).withDuration(Duration.ZERO.toMinutes()).build(),
                Task.builder().withName("task3").withId(3).withDescription("task3")
                        .withStatus(TaskStatus.IN_PROGRESS).withDuration(Duration.ZERO.toMinutes()).build(),
                Task.builder().withName("task2").withId(2).withDescription("task2")
                        .withStatus(TaskStatus.DONE).withDuration(Duration.ZERO.toMinutes()).build(),
                Task.builder().withName("task1").withId(1).withDescription("task1")
                        .withStatus(TaskStatus.NEW).withDuration(Duration.ZERO.toMinutes()).build(),
                Task.builder().withName("task3").withId(6).withDescription("task6")
                        .withStatus(TaskStatus.IN_PROGRESS).withDuration(Duration.ZERO.toMinutes()).build()
        );
        List<Task> actual = taskManager.getHistory();

        assertEquals(expected, actual);
    }
}
