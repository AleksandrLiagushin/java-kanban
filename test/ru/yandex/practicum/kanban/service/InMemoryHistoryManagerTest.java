package ru.yandex.practicum.kanban.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void getHistory_shouldReturnEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void add_shouldSaveTaskToHistory() {
        Task task1 = new Task.TaskBuilder("bla").withId(1).build();
        Task task2 = new Task.TaskBuilder("bla").withId(2).build();
        Task task3 = new Task.TaskBuilder("bla").withId(3).build();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldNotKeepDuplicates() {
        Task task1 = new Task.TaskBuilder("bla").withId(1).build();
        Task task2 = new Task.TaskBuilder("bla").withId(2).build();
        Task task3 = new Task.TaskBuilder("bla").withId(3).build();
        Task task4 = new Task.TaskBuilder("bla").withId(1).build();
        Task task5 = new Task.TaskBuilder("bla").withId(2).build();
        Task task6 = new Task.TaskBuilder("bla").withId(3).build();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        List<Task> expected = List.of(task1, task2, task3);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveTaskFromHistory() {
        Task task1 = new Task.TaskBuilder("bla").withId(1).build();
        Task task2 = new Task.TaskBuilder("bla").withId(2).build();
        Task task3 = new Task.TaskBuilder("bla").withId(3).build();
        Task task4 = new Task.TaskBuilder("bla").withId(4).build();
        Task task5 = new Task.TaskBuilder("bla").withId(5).build();
        Task task6 = new Task.TaskBuilder("bla").withId(6).build();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.remove(2);
        historyManager.remove(4);
        historyManager.remove(6);
        List<Task> expected = List.of(task1, task3, task5);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldMoveTaskToTheEnd_ifTaskAlreadyExistsInHistory() {
        Task task1 = new Task.TaskBuilder("bla").withId(1).build();
        Task task2 = new Task.TaskBuilder("bla").withId(2).build();
        Task task3 = new Task.TaskBuilder("bla").withId(3).build();
        Task task4 = new Task.TaskBuilder("bla").withId(3).build();
        Task task5 = new Task.TaskBuilder("bla").withId(2).build();
        Task task6 = new Task.TaskBuilder("bla").withId(1).build();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        List<Task> expected = List.of(task3, task2, task1);
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }
}

