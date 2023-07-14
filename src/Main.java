import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.FileBackedManager;
import ru.yandex.practicum.kanban.service.FileBackedTaskManager;

import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        FileBackedManager taskManager = FileBackedTaskManager.loadFromFile(Paths.get("resources", "Tasks.csv"));

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("******************************");
        System.out.println(taskManager.getHistory());

        taskManager.createTask(Task.builder()
                .withName("task1123")
                .withDescription("task2")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.now())
                .withDuration(315)
                .build());

        taskManager.createEpic(Epic.builder().withName("epic").withDescription("epic1").build());
        taskManager.createEpic(Epic.builder().withName("epic").withDescription("epic1").build());

        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub2")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2022-12-04T10:10:10.6546515"))
                .withDuration(234L)
                .withEpicId(2)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub2")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2022-12-01T10:14:10.6546515"))
                .withDuration(234L)
                .withEpicId(3)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("sub1")
                .withDescription("make")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2022-12-01T10:12:10.6546515"))
                .withDuration(234L)
                .withEpicId(2)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("sub1")
                .withDescription("make")
                .withStatus(TaskStatus.NEW)
                .withStartTime(LocalDateTime.parse("2022-12-01T10:10:10.6546515"))
                .withDuration(234L)
                .withEpicId(3)
                .build());

        System.out.println("*** New Test ***");

        for (Task task : taskManager.getPriorityTasks()) {
            System.out.println(task);
        }

        System.out.println("\n*** Update ***\n");

        taskManager.createTask(Task.builder()
                .withName("task3")
                .withDescription("task2")
                .withStatus(TaskStatus.NEW)
                .build());

        taskManager.updateTask(Task.builder()
                .withId(1)
                .withName("task1")
                .withDescription("task2")
                .withStatus(TaskStatus.NEW)
                .build());

        taskManager.updateTask(Task.builder()
                .withId(8)
                .withName("task8")
                .withDescription("task8")
                .withStatus(TaskStatus.NEW)
                .build());

        for (Task task : taskManager.getPriorityTasks()) {
            System.out.println(task);
        }
    }
}
