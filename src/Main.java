import ru.yandex.practicum.kanban.data.model.Epic;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;
import ru.yandex.practicum.kanban.data.model.TaskStatus;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTaskManager();
        taskManager.createTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 2));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 2));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 2));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 6));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 6));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 9));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 9));
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

        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");
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
        taskManager.createTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 13));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 13));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 13));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 17));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 12", "sub2", TaskStatus.NEW, 17));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 13", "sub1", TaskStatus.DONE, 20));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 14", "sub2", TaskStatus.NEW, 13));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.createTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 24));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 24));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 24));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 28));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 28));
        taskManager.createEpic(new Epic(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 31));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 31));
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 31));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 31));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 28));
        taskManager.createSubtask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 28));
        taskManager.deleteTaskById(23);
        taskManager.deleteSubtaskById(34);
        taskManager.deleteEpicById(28);
        taskManager.createTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.refreshTask(new Task(38, "notNew", "blablabla", TaskStatus.IN_PROGRESS));
        taskManager.refreshEpic(new Epic(24,"notNewEpic", "trololo"));
        taskManager.refreshSubtask(new Subtask(35, "notNewSub", "dykdatakidyk", TaskStatus.DONE, 24));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
