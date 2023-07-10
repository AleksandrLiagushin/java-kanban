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
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));

        List<Task> expected = List.of(
                createTask(1),
                createTask(2),
                createTask(3)
        );
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldNotKeepDuplicates() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        List<Task> expected = List.of(
                createTask(1),
                createTask(2),
                createTask(3));
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveTaskFromHistory() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        historyManager.add(createTask(4));
        historyManager.add(createTask(5));
        historyManager.add(createTask(6));
        historyManager.remove(2);
        historyManager.remove(4);
        historyManager.remove(6);
        List<Task> expected = List.of(createTask(1), createTask(3), createTask(5));
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldMoveTaskToTheEnd_ifTaskAlreadyExistsInHistory() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        historyManager.add(createTask(3));
        historyManager.add(createTask(2));
        historyManager.add(createTask(1));
        List<Task> expected = List.of(createTask(3), createTask(2), createTask(1));
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveFirstElementFromLinkedList() {
        historyManager.add(createTask(1));
        historyManager.add(createTask(2));
        historyManager.add(createTask(3));
        historyManager.remove(1);
        List<Task> expected = List.of(createTask(2),
                createTask(3));
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    private static Task createTask(int id) {
        return Task.builder().withName("bla").withId(id).build();
    }
}

