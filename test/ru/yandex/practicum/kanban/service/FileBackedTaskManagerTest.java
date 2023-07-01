package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {
    private final FileBackedTaskManager taskManager = new FileBackedTaskManager(Paths.get("resources", "TasksTest.csv"));

    @Test
    void save_shouldSaveAllTasksToFile() throws IOException {
        taskManager.createTask(new Task.TaskBuilder("task1")
                .withDescription("task1")
                .withStatus(TaskStatus.NEW)
                .build());
        taskManager.createTask(new Task.TaskBuilder("task2")
                .withDescription("task2")
                .withStatus(TaskStatus.DONE)
                .build());
        taskManager.createTask(new Task.TaskBuilder("task3")
                .withDescription("task3")
                .withStatus(TaskStatus.IN_PROGRESS)
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("epic1")
                .withDescription("epic1")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("epic2")
                .withDescription("epic2")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("epic3")
                .withDescription("epic3")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub1", TaskStatus.NEW, 4)
                .withDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub2", TaskStatus.DONE, 4)
                .withDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub3", TaskStatus.NEW, 5)
                .withDescription("sub3")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub4", TaskStatus.NEW, 5)
                .withDescription("sub4")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub5", TaskStatus.DONE, 6)
                .withDescription("sub5")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub6", TaskStatus.DONE, 6)
                .withDescription("sub6")
                .build());

        List<String> expected = Files.readAllLines(Paths.get("resources", "TasksBenchmark.csv"));
        List<String> actual = Files.readAllLines(Paths.get("resources", "TasksTest.csv"));

        assertEquals(expected, actual);
    }

    @Test
    void load_shouldLoadAllTasksFromFile() {
        taskManager.load();

        List<Task> expectedTasks = List.of(new Task.TaskBuilder("task1").withId(1)
                        .withDescription("task1").withStatus(TaskStatus.NEW).build(),
                new Task.TaskBuilder("task2").withId(2).withDescription("task2").withStatus(TaskStatus.DONE).build(),
                new Task.TaskBuilder("task3").withId(3).withDescription("task3").withStatus(TaskStatus.IN_PROGRESS).build());
        Epic epic1 = new Epic.EpicBuilder("epic1").withId(4).withDescription("epic1").withStatus(TaskStatus.IN_PROGRESS)
                .withSubtaskId(7).withSubtaskId(8).build();
        epic1.addSubtaskId(7);
        epic1.addSubtaskId(8);
        Epic epic2 = new Epic.EpicBuilder("epic2").withId(5).withDescription("epic2").withStatus(TaskStatus.NEW)
                .withSubtaskId(9).withSubtaskId(10).build();
        epic2.addSubtaskId(9);
        epic2.addSubtaskId(10);
        Epic epic3 = new Epic.EpicBuilder("epic3").withId(6).withDescription("epic3").withStatus(TaskStatus.DONE)
                        .withSubtaskId(11).withSubtaskId(12).build();
        epic3.addSubtaskId(11);
        epic3.addSubtaskId(12);
        List<Epic> expectedEpics = List.of(epic1, epic2, epic3);
        List<Subtask> expectedSubtasks = List.of(new Subtask.SubtaskBuilder("sub1", TaskStatus.NEW, 4)
                        .withId(7).withDescription("sub1").build(),
        new Subtask.SubtaskBuilder("sub2", TaskStatus.DONE, 4).withId(8).withDescription("sub2").build(),
        new Subtask.SubtaskBuilder("sub3", TaskStatus.NEW, 5).withId(9).withDescription("sub3").build(),
        new Subtask.SubtaskBuilder("sub4", TaskStatus.NEW, 5).withId(10).withDescription("sub4").build(),
        new Subtask.SubtaskBuilder("sub5", TaskStatus.DONE, 6).withId(11).withDescription("sub5").build(),
        new Subtask.SubtaskBuilder("sub6", TaskStatus.DONE, 6).withId(12).withDescription("sub6").build());

        List<Task> actualTasks = taskManager.getAllTasks();
        List<Epic> actualEpics = taskManager.getAllEpics();
        List<Subtask> actualSubtasks = taskManager.getAllSubtasks();

        assertEquals(expectedTasks,actualTasks);
        assertEquals(expectedEpics, actualEpics);
        assertEquals(expectedSubtasks, actualSubtasks);
    }
}
