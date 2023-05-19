package controller;

import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;
import servises.TaskManager;

import java.util.Scanner;

public class AppController {
    private static final int LOAD_NEW_TASK = 1;
    private static final int CHANGE_STATUS = 2;
    private static final int FIND_TASK = 3;
    private static final int CHANGE_TASK = 4;
    private static final int DELETE_TASK = 5;
    private static final int DELETE_ALL_TASKS = 6;
    private static final int SHOW_STATISTICS = 7;
    private static final int QUIT_APP = 0;

    public void startApp() {

        var userInput = new Scanner(System.in);
        var taskManager = new TaskManager();

        while (true) {
            printMainMenu();
            int id;
            int command = userInput.nextInt();
            userInput.nextLine();
            if (command == LOAD_NEW_TASK) {
                System.out.println("Задача - 1, Эпик - 2, Подзадача - 3");
                command = userInput.nextInt();
                userInput.nextLine();
                if (command == 1) {
                    taskManager.createNewTask(new Task(), userInput);
                } else if (command == 2) {
                    taskManager.createNewEpic(new EpicTask(), userInput);
                } else if (command == 3) {
                    taskManager.createSubTask(new SubTask(), userInput);
                }
            } else if (command == CHANGE_STATUS) {
                System.out.println("input task id");
                id = userInput.nextInt();
                userInput.nextLine();
                taskManager.changeTaskStatus(id, userInput);
            } else if (command == CHANGE_TASK) {
                System.out.println("Input ID");
                id = userInput.nextInt();
                userInput.nextLine();
                if (taskManager.findTaskByID(id) != null) {
                    taskManager.getTasksList()
                            .addNewTaskToList(id, taskManager.refreshTask(taskManager.findTaskByID(id), userInput));
                }
                if (taskManager.findEpicByID(id) != null) {
                    taskManager.getEpicTasksList()
                            .addEpicTaskToList(id, taskManager.refreshEpicTask(taskManager.findEpicByID(id),
                                    userInput));
                }
                if (taskManager.findSubTaskByID(id) != null) {
                    taskManager.getSubTasksList()
                            .addSubTaskToList(id, taskManager.refreshSubTask(taskManager.findSubTaskByID(id),
                                    userInput));
                    taskManager.getEpicTasksList()
                            .changeEpicStatus(taskManager.findSubTaskByID(id).getOwnedByEpic(),
                                    taskManager.getSubTasksList());
                }
            } else if (command == FIND_TASK) {
                System.out.println("Input ID");
                id = userInput.nextInt();
                Task task = taskManager.findTaskByID(id);
                EpicTask epic = taskManager.findEpicByID(id);
                SubTask subTask = taskManager.findSubTaskByID(id);
            } else if (command == DELETE_TASK) {
                System.out.println("Input ID");
                id = userInput.nextInt();
                taskManager.deleteTaskByID(id);
                taskManager.deleteEpicTaskByID(id);
                taskManager.deleteSubTaskByID(id);
            } else if (command == DELETE_ALL_TASKS) {
                taskManager.deleteAllTasks();
            } else if (command == SHOW_STATISTICS) {
                System.out.println(taskManager.getEpicTasksList());
                System.out.println(taskManager.getTasksList());
                System.out.println(taskManager.getSubTasksList());
            } else if (command == QUIT_APP) {
                System.out.println("Работа приложения успешно заввершена.");
                break;
            } else {
                System.out.println("Такой команды нет. Повторите ввод:");
            }
        }
        userInput.close();
    }

    private static void printMainMenu() {
        System.out.println("Трекер задачач v1.0");
        System.out.println("1 - Внести новую задачу.");
        System.out.println("2 - Поменять статус задачи.");
        System.out.println("3 - Поиск задач.");
        System.out.println("4 - Изменить задачу.");
        System.out.println("5 - Удалить задачу.");
        System.out.println("6 - Удалить все задачи");
        System.out.println("7 - Статистика задач.");
        System.out.println("0 - Выход.");
    }
}
