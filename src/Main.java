import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.FileManager;
import ru.yandex.practicum.kanban.service.Managers;

public class Main {

    public static void main(String[] args) {

        FileManager taskManager = (FileManager) Managers.getBackedTaskManager();
        taskManager.load();

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
        taskManager.findTaskById(24);
        taskManager.findEpicById(31);
        taskManager.findSubtaskById(30);
        taskManager.findTaskById(24);
        taskManager.findEpicById(31);
        taskManager.findSubtaskById(30);
        taskManager.findTaskById(24);
        taskManager.findEpicById(31);
        taskManager.findSubtaskById(30);
        taskManager.findTaskById(26);
        taskManager.findTaskById(25);
    }
}
