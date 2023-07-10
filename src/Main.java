import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.FileBackedManager;
import ru.yandex.practicum.kanban.service.FileBackedTaskManager;

import java.nio.file.Paths;

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
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub2")
                .withStatus(TaskStatus.NEW)
                .withEpicId(13)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub1")
                .withStatus(TaskStatus.DONE)
                .withEpicId(13)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub2")
                .withStatus(TaskStatus.NEW)
                .withEpicId(14)
                .build());
        taskManager.createSubtask(Subtask.builder()
                .withName("Задача 12")
                .withDescription("sub2")
                .withStatus(TaskStatus.NEW)
                .withEpicId(14)
                .build());
        taskManager.createEpic(Epic.builder()
                .withName("Важный эпик 2")
                .withDescription("epic1")
                .build());

        taskManager.findTaskById(128);
        taskManager.findEpicById(13);
        taskManager.findSubtaskById(160);
        taskManager.findTaskById(144);
        taskManager.findEpicById(14);
        taskManager.findSubtaskById(129);
        taskManager.findTaskById(128);
        taskManager.findEpicById(13);
        taskManager.findSubtaskById(130);
        taskManager.findTaskById(128);
        taskManager.findTaskById(144);

    }
}
