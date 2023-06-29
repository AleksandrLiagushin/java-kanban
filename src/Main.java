import ru.yandex.practicum.kanban.service.FileBackedTaskManager;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        taskManager.load();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("******************************");
        System.out.println(taskManager.getHistory());
    }
}
