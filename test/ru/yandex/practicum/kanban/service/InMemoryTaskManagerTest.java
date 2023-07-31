package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();
    }
}