package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    private KVServer kvServer;

    @BeforeEach
    protected void createKVServer() throws Exception {
        kvServer = new KVServer();
        kvServer.start();
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

    @Test
    void taskManager_shouldSaveAndLoadDataFromKvServer() {
        HttpTaskManager taskManager = createTaskManager();
        taskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        taskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        taskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        taskManager.createTask(Task.builder().withName("task").withDescription("task").build());
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);

        assertEquals(4, createTaskManager().getAllTasks().size());
        assertEquals(2, createTaskManager().getHistory().size());
    }
}