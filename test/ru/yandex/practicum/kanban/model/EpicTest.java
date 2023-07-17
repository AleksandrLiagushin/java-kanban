package ru.yandex.practicum.kanban.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private static Epic emptyEpic;
    private static Epic filledEpic;
    private static Epic epic;

    @BeforeAll
    public static void createEpics() {
        emptyEpic = Epic.builder().build();
        filledEpic = Epic.builder().build();
        epic = Epic.builder()
                .withId(1)
                .withName("epic1")
                .withDescription("epic")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0"))
                .withDuration(250L)
                .withSubtaskIds(List.of(5, 6, 7))
                .build();
    }

    @Test
    public void shouldGetSubtaskIds() {
        List<Integer> expected = List.of(5, 6, 7);
        List<Integer> actual = epic.getSubtaskIds();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddRemoveAndClearSubtaskIds() {
        List<Integer> expected1 = List.of(5, 6, 7);
        filledEpic.addSubtaskId(5);
        filledEpic.addSubtaskId(6);
        filledEpic.addSubtaskId(7);
        List<Integer> actual1 = filledEpic.getSubtaskIds();

        assertEquals(expected1, actual1);

        List<Integer> expected2 = List.of(5, 7);
        filledEpic.removeSubtaskId(6);
        List<Integer> actual2 = filledEpic.getSubtaskIds();

        assertEquals(expected2, actual2);

        List<Integer> expected3 = List.of();
        filledEpic.clearSubtaskIds();
        List<Integer> actual3 = filledEpic.getSubtaskIds();

        assertEquals(expected3, actual3);
    }

    @Test
    public void shouldSetStatus() {
        filledEpic.setStatus(TaskStatus.NEW);
        TaskStatus expected = TaskStatus.NEW;
        TaskStatus actual = filledEpic.getStatus();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddAndSubtractDuration() {
        Duration expected1 = Duration.ofMinutes(500L);
        filledEpic.addDuration(Duration.ofMinutes(500L));
        Duration actual1 = filledEpic.getDuration().orElse(null);

        assertEquals(expected1, actual1);

        Duration expected2 = Duration.ofMinutes(250L);
        filledEpic.subtractDuration(Duration.ofMinutes(250L));
        Duration actual2 = filledEpic.getDuration().orElse(null);

        assertEquals(expected2, actual2);
    }

    @Test
    public void shouldCompareEpics() {
        Task equalEpic = Epic.builder()
                .withId(1)
                .withName("epic1")
                .withDescription("epic")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:30:00.0"))
                .withDuration(250L)
                .withSubtaskIds(List.of(5, 6, 7))
                .build();


        assertAll("ComparingTasks test:",
                () -> assertEquals(equalEpic, epic),
                () -> assertNotEquals(equalEpic, emptyEpic),
                () -> assertEquals(epic, epic),
                () -> assertNotEquals(equalEpic, null));
    }

    @Test
    public void shouldConvertEpicToString() {
        String expected = "Epic{id=1, name='epic1', description='epic', " +
                "status=NEW, startTime=2015-05-29T03:30, duration=250subtaskIds=[5, 6, 7]}";
        String actual = epic.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldConvertEpicToCsvLine() {
        String expected = "1,EPIC,'\"epic1\"','\"epic\"',NEW,2015-05-29T03:30,250,[5, 6, 7]";
        String actual = epic.toCsvLine();

        assertEquals(expected, actual);
    }
}