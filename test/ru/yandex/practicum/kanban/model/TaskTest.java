package ru.yandex.practicum.kanban.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private static Task emptyTask = null;
    private static Task filledTask = null;
    private static Task task = null;

    @BeforeAll
    static public void createTask() {
        emptyTask = Task.builder().build();
        filledTask = Task.builder().build();
        task = Task.builder()
                .withId(1)
                .withName("task1")
                .withDescription("task1, description")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0"))
                .withDuration(250L)
                .build();
    }

    @Test
    public void shouldGetId() {
        int expected1 = 1;
        int expected2 = 0;
        int actual1 = task.getId();
        int actual2 = emptyTask.getId();

        assertAll("GetId test:",
                () -> assertEquals(expected1, actual1),
                () -> assertEquals(expected2, actual2)
        );
    }

    @Test
    public void shouldGetName() {
        String expected1 = "task1";
        String actual1 = task.getName();
        String actual2 = emptyTask.getName();

        assertAll("GetName test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldGetDescription() {
        String expected1 = "task1, description";
        String actual1 = task.getDescription();
        String actual2 = emptyTask.getDescription();

        assertAll("GetDescription test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldGetStatus() {
        TaskStatus expected1 = TaskStatus.NEW;
        TaskStatus actual1 = task.getStatus();
        TaskStatus actual2 = emptyTask.getStatus();

        assertAll("GetStatus test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldGetStartTime() {
        LocalDateTime expected1 = LocalDateTime.parse("2015-05-29T03:15:00.0");
        LocalDateTime actual1 = task.getStartTime().orElse(null);
        LocalDateTime actual2 = emptyTask.getStartTime().orElse(null);

        assertAll("GetStartTime test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldGetDuration() {
        Duration expected1 = Duration.ofMinutes(250L);
        Duration actual1 = task.getDuration().orElse(null);
        Duration actual2 = emptyTask.getDuration().orElse(null);

        assertAll("GetDuration test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldGetEndTime() {
        LocalDateTime expected1 = LocalDateTime.parse("2015-05-29T03:15:00.0").plus(Duration.ofMinutes(250L));
        LocalDateTime actual1 = task.getEndTime().orElse(null);
        LocalDateTime actual2 = emptyTask.getEndTime().orElse(null);

        assertAll("GetEndTime test:",
                () -> assertEquals(expected1, actual1),
                () -> assertNull(actual2)
        );
    }

    @Test
    public void shouldSetId() {
        filledTask.setId(2);
        int expected = 2;
        int actual = filledTask.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetStatus() {
        filledTask.setStatus(TaskStatus.NEW);
        TaskStatus expected = TaskStatus.NEW;
        TaskStatus actual = filledTask.getStatus();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetStartTime() {
        filledTask.setStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0"));
        LocalDateTime expected = LocalDateTime.parse("2015-05-29T03:15:00.0");
        LocalDateTime actual = filledTask.getStartTime().orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetDuration() {
        filledTask.setDuration(250L);
        Duration expected = Duration.ofMinutes(250L);
        Duration actual = filledTask.getDuration().orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCompareTasks() {
        Task equalTask = Task.builder()
                .withId(1)
                .withName("task1")
                .withDescription("task1, description")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2015-05-29T03:15:00.0"))
                .withDuration(250L)
                .build();

        assertAll("ComparingTasks test:",
                () -> assertEquals(equalTask, task),
                () -> assertNotEquals(equalTask, emptyTask),
                () -> assertEquals(task, task),
                () -> assertNotEquals(equalTask, null));
    }

    @Test
    public void shouldConvertTaskToString() {
        String expected = "Task{id=1, name='task1', description='task1, description', status=NEW, " +
                "startTime=2015-05-29T03:15, duration=PT4H10M}";
        String actual = task.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldConvertTaskToCsvLine() {
        String expected = "1,TASK,'\"task1\"','\"task1, description\"',NEW,2015-05-29T03:15,250";
        String actual = task.toCsvLine();

        assertEquals(expected, actual);
    }
}