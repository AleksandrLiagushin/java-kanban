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
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());

        List<Task> expected = List.of(new Task.TaskBuilder("bla").withId(1).build(),
                new Task.TaskBuilder("bla").withId(2).build(),
                new Task.TaskBuilder("bla").withId(3).build());
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldNotKeepDuplicates() {
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        List<Task> expected = List.of(new Task.TaskBuilder("bla").withId(1).build(),
                new Task.TaskBuilder("bla").withId(2).build(),
                new Task.TaskBuilder("bla").withId(3).build());
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveTaskFromHistory() {
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(4).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(5).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(6).build());
        historyManager.remove(2);
        historyManager.remove(4);
        historyManager.remove(6);
        List<Task> expected = List.of(new Task.TaskBuilder("bla").withId(1).build(), new Task.TaskBuilder("bla").withId(3).build(), new Task.TaskBuilder("bla").withId(5).build());
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void add_shouldMoveTaskToTheEnd_ifTaskAlreadyExistsInHistory() {
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        List<Task> expected = List.of(new Task.TaskBuilder("bla").withId(3).build(), new Task.TaskBuilder("bla").withId(2).build(), new Task.TaskBuilder("bla").withId(1).build());
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldRemoveFirstElementFromLinkedList() {
        historyManager.add(new Task.TaskBuilder("bla").withId(1).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(2).build());
        historyManager.add(new Task.TaskBuilder("bla").withId(3).build());
        historyManager.remove(1);
        List<Task> expected = List.of(new Task.TaskBuilder("bla").withId(2).build(),
                new Task.TaskBuilder("bla").withId(3).build());
        List<Task> actual = historyManager.getHistory();

        assertEquals(expected, actual);
    }
}

