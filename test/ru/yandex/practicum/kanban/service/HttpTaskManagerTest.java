package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;

    @BeforeEach
    protected void createKVServer() throws Exception {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = createTaskManager();
    }

    @AfterEach
    void stopKvServer() {
        kvServer.stop();
    }

    private HttpTaskManager createTaskManager() {
        HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078");
        httpTaskManager.load();
        return httpTaskManager;
    }

    private void saveTasksToKVServer() {
        taskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        taskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        taskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Task").build());
        taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Task").build());
        taskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.NEW).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(4).build());
        taskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.NEW).withEpicId(5).build());
        taskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(5).build());
    }

    @Test
    public void shouldContainEqualTasksAfterUploadFromServer() {
        saveTasksToKVServer();
        HttpTaskManager httpTaskManager = createTaskManager();
        TaskManager inMemoryTaskManager = Managers.getDefault();
        inMemoryTaskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        inMemoryTaskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        inMemoryTaskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        inMemoryTaskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Task").build());
        inMemoryTaskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Task").build());
        inMemoryTaskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.NEW).withEpicId(4).build());
        inMemoryTaskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(4).build());
        inMemoryTaskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.NEW).withEpicId(5).build());
        inMemoryTaskManager.createSubtask(Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(5).build());
        assertAll(
                () -> assertEquals(inMemoryTaskManager.getAllTasks(), httpTaskManager.getAllTasks()),
                () -> assertEquals(inMemoryTaskManager.getAllEpics(), httpTaskManager.getAllEpics()),
                () -> assertEquals(inMemoryTaskManager.getAllSubtasks(), httpTaskManager.getAllSubtasks()));
    }

    @Test
    void taskManager_shouldSaveAndLoadDataFromKvServer() {
        HttpTaskManager httpTaskManager = createTaskManager();
        httpTaskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        httpTaskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        httpTaskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        httpTaskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        httpTaskManager.getTaskById(1);
        httpTaskManager.getTaskById(2);

        assertEquals(4, createTaskManager().getAllTasks().size());
        assertEquals(2, createTaskManager().getHistory().size());
    }
}