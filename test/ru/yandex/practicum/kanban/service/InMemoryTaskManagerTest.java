package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    public void shouldCreateTask() {
        createTask();
    }

    @Test
    public void shouldCreateEpic() {
        createEpic();
    }

    @Test
    public void shouldCreateSubtaskAndCheckEpicChanges() {
        createSubtask();
    }

    @Test
    public void shouldFindTaskById() {
        findTaskById();
    }

    @Test
    public void shouldFindEpicById() {
        findEpicById();
    }

    @Test
    public void shouldFindSubtaskById() {
        findSubtaskById();
    }

    @Test
    public void shouldGetAllTasks() {
        getAllTasks();
    }

    @Test
    public void shouldGetAllEpics() {
        getAllEpics();
    }

    @Test
    public void shouldGetAllSubtasks() {
        getAllSubtasks();
    }

    @Test
    public void shouldGetPriorityTasks() {
        getPriorityTasks();
    }

    @Test
    public void shouldUpdateTask() {
        updateTask();
    }

    @Test
    void shouldUpdateEpic() {
        updateEpic();
    }

    @Test
    void shouldUpdateSubtask() {
        updateSubtask();
    }

    @Test
    void shouldDeleteTaskById() {
        deleteTaskById();
    }

    @Test
    void shouldDeleteEpicById() {
        deleteEpicById();
    }

    @Test
    void shouldDeleteSubtaskById() {
        deleteSubtaskById();
    }

    @Test
    void shouldDeleteAllTasks() {
        deleteAllTasks();
    }

    @Test
    void shouldDeleteAllEpics() {
        deleteAllEpics();
    }

    @Test
    void shouldDeleteAllSubtasks() {
        deleteAllSubtasks();
    }

    @Test
    void shouldGetSubtasksByEpicId() {
        getSubtasksByEpicId();
    }

    @Test
    void shouldGetHistory() {
        getHistory();
    }
}