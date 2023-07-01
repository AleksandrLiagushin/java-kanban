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

        taskManager.createTask(new Task.TaskBuilder("task1123")
                .withDescription("task2")
                .withStatus(TaskStatus.NEW)
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 12", TaskStatus.NEW, 13)
                .withDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 12", TaskStatus.DONE, 13)
                .withDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 12", TaskStatus.NEW, 14)
                .withDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 12", TaskStatus.NEW, 14)
                .withDescription("sub2")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("Важный эпик 2")
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
