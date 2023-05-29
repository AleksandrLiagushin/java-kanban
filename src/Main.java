import ru.yandex.practicum.kanban.data.model.EpicTask;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;
import ru.yandex.practicum.kanban.data.model.TaskStatus;
import ru.yandex.practicum.kanban.servise.Managers;
import ru.yandex.practicum.kanban.servise.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTaskManager();
        taskManager.createNewTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 2));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 2));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 2));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 6));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 6));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 9));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 9));
        taskManager.findTaskByID(1);
        taskManager.findEpicByID(2);
        taskManager.findSubTaskByID(3);
        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");
        taskManager.findTaskByID(1);
        taskManager.findEpicByID(2);
        taskManager.findSubTaskByID(3);
        taskManager.findTaskByID(1);
        taskManager.findEpicByID(2);
        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");
        taskManager.findSubTaskByID(3);
        taskManager.findEpicByID(2);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);
        taskManager.findEpicByID(6);

        System.out.println(taskManager.getHistory());
        System.out.println("__________________________________________");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println("-------------");
        taskManager.deleteSubTaskByID(10);
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.deleteEpicTaskByID(6);
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getSubTasksListOfEpicByID(2));
        taskManager.createNewTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 13));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 13));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 13));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 17));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 12", "sub2", TaskStatus.NEW, 17));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 13", "sub1", TaskStatus.DONE, 20));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 14", "sub2", TaskStatus.NEW, 13));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.createNewTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Переезд", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Собрать коробки", "sub1", TaskStatus.NEW, 24));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Упаковать кошку", "sub2", TaskStatus.NEW, 24));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Сказать слова прощания", "sub3", TaskStatus.NEW, 24));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 28));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 28));
        taskManager.createNewEpic(new EpicTask(taskManager.generateTaskID(),
                "Важный эпик 2", "epic1"));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 31));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 31));
        taskManager.deleteAllSubs();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.DONE, 31));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 31));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub1", TaskStatus.NEW, 28));
        taskManager.createSubTask(new Subtask(taskManager.generateTaskID(),
                "Задача 1", "sub2", TaskStatus.NEW, 28));
        taskManager.deleteTaskByID(23);
        taskManager.deleteSubTaskByID(34);
        taskManager.deleteEpicTaskByID(28);
        taskManager.createNewTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        taskManager.createNewTask(new Task(taskManager.generateTaskID(),
                "new", "newDescription", TaskStatus.NEW));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        taskManager.refreshTask(new Task(38, "notNew", "blablabla", TaskStatus.IN_PROGRESS));
        taskManager.refreshEpicTask(new EpicTask(24,"notNewEpic", "trololo"));
        taskManager.refreshSubTask(new Subtask(35, "notNewSub", "dykdatakidyk", TaskStatus.DONE, 24));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
    }
}
