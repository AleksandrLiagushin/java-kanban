package controller;

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
                    taskManager.createNewTask(userInput);
                } else if (command == 2) {
                    taskManager.createNewEpic(userInput);
                } else if (command == 3) {
                    taskManager.createSubTask(userInput);
                }
            } else if (command == CHANGE_STATUS) {
                System.out.println("input task id");
                id = userInput.nextInt();
                userInput.nextLine();
                taskManager.changeTaskStatus(id, userInput);
            } else if (command == CHANGE_TASK) {
                System.out.println("Какую задачу переместить? 1 - задача 2 - подзадача");
                command = userInput.nextInt();
                userInput.nextLine();
                if (command == 1) {
                    id = userInput.nextInt();
                    userInput.nextLine();
                    Task task = taskManager.getTasksList().getTaskByID(id);
                    if (task != null) {
                        System.out.println("1 - переместить в эпик 2 - переместить в подзадачи");
                        command = userInput.nextInt();
                        userInput.nextLine();
                        if (command == 1) {
                            System.out.println("Ввести ID");
                            id = userInput.nextInt();
                            userInput.nextLine();
                            taskManager.moveTaskToEpic(id);
                        } else if (command == 2) {
                            System.out.println("Ввести ID");
                            id = userInput.nextInt();
                            userInput.nextLine();
                            taskManager.moveTaskToSubTask(userInput, id);
                        }
                    }
                } else if (command == 2) {
                    System.out.println("1 - переместить в эпик 2 - переместить в задачи");
                    command = userInput.nextInt();
                    userInput.nextLine();
                    if (command == 1) {
                        System.out.println("Ввести ID");
                        id = userInput.nextInt();
                        userInput.nextLine();
                        taskManager.moveSubTaskToTask(id);
                        taskManager.moveTaskToEpic(id);
                    } else if (command == 2) {
                        System.out.println("Ввести ID");
                        id = userInput.nextInt();
                        userInput.nextLine();
                        taskManager.moveSubTaskToTask(id);
                    }
                }
            } else if (command == FIND_TASK) {
                id = userInput.nextInt();
                taskManager.findTaskByID(id);
            } else if (command == DELETE_TASK) {
                id = userInput.nextInt();
                taskManager.deleteAnyTaskByID(id);
            } else if (command == DELETE_ALL_TASKS) {
                taskManager.getTasksList().deleteAllTasks();
                taskManager.getSubTasksList().deleteAllTasks();
                taskManager.getEpicTasksList().deleteAllTasks();
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
