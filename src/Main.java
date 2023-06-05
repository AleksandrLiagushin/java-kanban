import ru.yandex.practicum.kanban.data.model.Epic;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;
import ru.yandex.practicum.kanban.data.model.TaskStatus;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTaskManager();

        taskManager.createTask(new Task()
                .setName("werq")
                .setDescription("lsdkf")
                .setStatus(TaskStatus.NEW));
        taskManager.createEpic(new Epic()
                .setName("Переезд")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Собрать коробки")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(2));
        taskManager.createSubtask(new Subtask()
                .setName("Упаковать кошку")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(2));
        taskManager.createSubtask(new Subtask()
                .setName("Сказать слова прощания")
                .setDescription("sub3")
                .setStatus(TaskStatus.NEW)
                .setEpicId(2));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(6));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(6));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.DONE)
                .setEpicId(9));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(9));
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
        taskManager.createTask(new Task()
                .setName("new")
                .setDescription("newDescription")
                .setStatus(TaskStatus.NEW));
        taskManager.createEpic(new Epic()
                .setName("Переезд")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Собрать коробки")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(13));
        taskManager.createSubtask(new Subtask()
                .setName("Упаковать кошку")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(13));
        taskManager.createSubtask(new Subtask()
                .setName("Сказать слова прощания")
                .setDescription("sub3")
                .setStatus(TaskStatus.NEW)
                .setEpicId(13));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(17));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 12")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(17));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 13")
                .setDescription("sub1")
                .setStatus(TaskStatus.DONE)
                .setEpicId(20));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 14")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(13));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.createTask(new Task()
                .setName("new")
                .setDescription("newDescription")
                .setStatus(TaskStatus.NEW));
        taskManager.createEpic(new Epic()
                .setName("Переезд")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Собрать коробки")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(24));
        taskManager.createSubtask(new Subtask()
                .setName("Упаковать кошку")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(24));
        taskManager.createSubtask(new Subtask()
                .setName("Сказать слова прощания")
                .setDescription("sub3")
                .setStatus(TaskStatus.NEW)
                .setEpicId(24));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(28));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(28));
        taskManager.createEpic(new Epic()
                .setName("Важный эпик 2")
                .setDescription("epic1"));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.DONE)
                .setEpicId(31));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(31));
        taskManager.deleteAllSubtasks();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.DONE)
                .setEpicId(31));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(31));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub1")
                .setStatus(TaskStatus.NEW)
                .setEpicId(28));
        taskManager.createSubtask(new Subtask()
                .setName("Задача 1")
                .setDescription("sub2")
                .setStatus(TaskStatus.NEW)
                .setEpicId(28));
        taskManager.deleteTaskById(23);
        taskManager.deleteSubtaskById(34);
        taskManager.deleteEpicById(28);
        taskManager.createTask(new Task()
                .setName("new")
                .setDescription("newDescription")
                .setStatus(TaskStatus.NEW));
        taskManager.createTask(new Task()
                .setName("new")
                .setDescription("newDescription")
                .setStatus(TaskStatus.NEW));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.updateTask(new Task()
                .setId(38)
                .setName("notNew")
                .setDescription("blablabla")
                .setStatus(TaskStatus.IN_PROGRESS));
        taskManager.updatehEpic(new Epic()
                .setId(24)
                .setName("notNewEpic")
                .setDescription("trololo"));
        taskManager.updateSubtask(new Subtask()
                .setId(35)
                .setName("notNewSub")
                .setDescription("dykdatakidyk")
                .setStatus(TaskStatus.DONE)
                .setEpicId(24));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
