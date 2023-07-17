package ru.yandex.practicum.kanban.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static Subtask emptySubtask;
    private static Subtask filledSubtask;
    private static Subtask subtask;

    @BeforeAll
    public static void createSubtasks() {
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
    public void shouldGetEpicId() {
        int expected = 1;
        int actual = subtask.getEpicId();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetSubtaskId() {
        int expected = 2;
        filledSubtask.setId(2);
        int actual = filledSubtask.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCompareSubtasks() {
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
    public void shouldConvertSubtaskToString() {
        String expected = "Subtask{id=1, name='subtask1', description='subtask1', " +
                "status=NEW, startTime=2015-05-29T03:30, duration=250epicId=1[]}";
        String actual = subtask.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldConvertSubtaskToCsvLine() {
        String expected = "1,SUBTASK,'\"subtask1\"','\"subtask1\"',NEW,2015-05-29T03:30,250,1";
        String actual = subtask.toCsvLine();

        assertEquals(expected, actual);
    }
}