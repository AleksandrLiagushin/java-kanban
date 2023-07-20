package ru.yandex.practicum.kanban.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubtaskTest {
    private Subtask emptySubtask;
    private Subtask filledSubtask;
    private Subtask subtask;

    @BeforeEach
    public void createSubtasks() {
        emptySubtask = Subtask.builder().build();
        filledSubtask = Subtask.builder().build();
        subtask = Subtask.builder()
                .withId(1)
                .withName("subtask1")
                .withDescription("subtask1")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0"))
                .withDuration(250L)
                .withEpicId(1)
                .build();
    }

    @Test
    public void getEpicId_shouldGetEpicId() {
        int expected = 1;
        int actual = subtask.getEpicId();

        assertEquals(expected, actual);
    }

    @Test
    public void setId_shouldSetSubtaskId() {
        int expected = 2;
        filledSubtask.setId(2);
        int actual = filledSubtask.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void compare_shouldCompareSubtasks() {
        Subtask equalTask = Subtask.builder()
                .withId(1)
                .withName("subtask1")
                .withDescription("subtask1")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0"))
                .withDuration(250L)
                .withEpicId(1)
                .build();

        assertAll("ComparingTasks test:",
                () -> assertEquals(equalTask, subtask),
                () -> assertNotEquals(equalTask, emptySubtask),
                () -> assertEquals(subtask, subtask),
                () -> assertNotEquals(equalTask, null));
    }

    @Test
    public void toString_shouldConvertSubtaskToString() {
        String expected = "Subtask{id=1, name='subtask1', description='subtask1', " +
                "status=NEW, startTime=2015-05-29T03:30, duration=250epicId=1}";
        String actual = subtask.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void toCsvLine_shouldConvertSubtaskToCsvLine() {
        String expected = "1,SUBTASK,'\"subtask1\"','\"subtask1\"',NEW,2015-05-29T03:30,250,1";
        String actual = subtask.toCsvLine();

        assertEquals(expected, actual);
    }
}