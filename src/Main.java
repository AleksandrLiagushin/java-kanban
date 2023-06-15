import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTaskManager();

        taskManager.createTask(new Task.TaskBuilder("task1")
                .setDescription("task1")
                .setStatus(TaskStatus.NEW)
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("epic1")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub1", TaskStatus.NEW, 2)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Упаковать кошку", TaskStatus.NEW,2)
                .setDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Сказать слова прощания", TaskStatus.NEW,2)
                .setDescription("sub3")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("Важный эпик 2")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 6)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 6)
                .setDescription("sub2")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("Важный эпик 2")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.DONE, 9)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 9)
                .setDescription("sub2")
                .build());

        taskManager.findTaskById(1);
        taskManager.findEpicById(2);
        taskManager.findSubtaskById(3);

        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");

        taskManager.findTaskById(1);
        taskManager.findEpicById(2);
        taskManager.findSubtaskById(3);
        taskManager.findTaskById(1);
        taskManager.findEpicById(2);

        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");

        taskManager.findSubtaskById(3);
        taskManager.findEpicById(2);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findEpicById(6);
        taskManager.findSubtaskById(3);
        taskManager.findSubtaskById(3);
        taskManager.findTaskById(1);
        taskManager.findEpicById(2);
        System.out.println(taskManager.getHistory());
        System.out.println("_______________________________________1___");

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("-------------");

        taskManager.deleteSubtaskById(10);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.deleteEpicById(6);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getSubtasksByEpicId(2));

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.createTask(new Task.TaskBuilder("task1")
                .setDescription("task1")
                .setStatus(TaskStatus.NEW)
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("epic1")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("sub1", TaskStatus.NEW, 2)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Упаковать кошку", TaskStatus.NEW,2)
                .setDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Сказать слова прощания", TaskStatus.NEW,2)
                .setDescription("sub3")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("Важный эпик 2")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 6)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 6)
                .setDescription("sub2")
                .build());
        taskManager.createEpic(new Epic.EpicBuilder("Важный эпик 2")
                .setDescription("epic1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.DONE, 9)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 9)
                .setDescription("sub2")
                .build());

        taskManager.deleteAllSubtasks();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 13)
                .setDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.DONE, 13)
                .setDescription("sub1")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 14)
                .setDescription("sub2")
                .build());
        taskManager.createSubtask(new Subtask.SubtaskBuilder("Задача 1", TaskStatus.NEW, 14)
                .setDescription("sub2")
                .build());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.deleteTaskById(12);
        taskManager.deleteSubtaskById(17);
        taskManager.deleteEpicById(15);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.createTask(new Task.TaskBuilder("task1")
                .setDescription("task1")
                .setStatus(TaskStatus.NEW)
                .build());
        taskManager.createTask(new Task.TaskBuilder("task1")
                .setDescription("task1")
                .setStatus(TaskStatus.NEW)
                .build());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
