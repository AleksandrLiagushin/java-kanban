package servises;

import data.storage.EpicTasksList;
import data.storage.SubTasksList;
import data.storage.TasksList;
import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;
import data.types.TaskStatus;

import java.util.Scanner;

public class TaskManager {

    private int taskID;
    TasksList tasksList = new TasksList();
    SubTasksList subTasksList = new SubTasksList();
    EpicTasksList epicTasksList = new EpicTasksList();


    public void createNewTask(Task task, Scanner userInput) {
        setTaskID();
        task.setTaskName(userInput.nextLine())
                .setDescription(userInput.nextLine())
                .setStatus(inputTaskStatus(userInput))
                .setCreationDate();
        tasksList.addNewTaskToList(getTaskID(), task);
    }

    public void createNewEpic(EpicTask epic, Scanner userInput) {
        setTaskID();
        epic.setTaskName(userInput.nextLine())
                .setDescription(userInput.nextLine())
                .setStatus(TaskStatus.NEW)
                .setCreationDate();
        epicTasksList.addEpicTaskToList(getTaskID(), epic);
    }

    public Task refreshTask(Task task, Scanner userInput) {
        task.setTaskName(userInput.nextLine());
        task.setDescription(userInput.nextLine());
        task.setStatus(inputTaskStatus(userInput));
        return task;
    }

    public EpicTask refreshEpicTask(EpicTask epic, Scanner userInput) {
        epic.setTaskName(userInput.nextLine());
        epic.setDescription(userInput.nextLine());
        return epic;
    }

    public SubTask refreshSubTask(SubTask sub, Scanner userInput) {
        sub.setTaskName(userInput.nextLine());
        sub.setDescription(userInput.nextLine());
        sub.setStatus(inputTaskStatus(userInput));
        return sub;
    }

    public void createSubTask(SubTask sub, Scanner userInput) {
        setTaskID();
        sub.setTaskName(userInput.nextLine())
                .setDescription(userInput.nextLine())
                .setStatus(inputTaskStatus(userInput))
                .setCreationDate();
        System.out.println("Введите ID эпика");
        int epicID = inputCommand(userInput);
        if (epicTasksList.getTaskByID(epicID) != null) {
            sub.setOwnedByEpic(epicID);
            EpicTask epic = findEpicByID(epicID);
            epic.addSubIDToSubTasksList(getTaskID());
            epicTasksList.addEpicTaskToList(epicID, epic);
            subTasksList.addSubTaskToList(getTaskID(), sub);
            epicTasksList.changeEpicStatus(sub.getOwnedByEpic(), subTasksList);
        } else {
            System.out.println("Такого эпика нет. Подзадача переведена в задачи");
            tasksList.addNewTaskToList(getTaskID(), sub);
        }
    }

    public void changeTaskStatus(int taskID, Scanner userInput) {
        if (subTasksList.getTaskByID(taskID) != null) {
            SubTask sub = subTasksList.getTaskByID(taskID);
            EpicTask epic = epicTasksList.getTaskByID(sub.getOwnedByEpic());
            epic.addSubIDToSubTasksList(taskID);
            sub.setStatus(inputTaskStatus(userInput));
            subTasksList.addSubTaskToList(taskID, sub);
            epicTasksList.addEpicTaskToList(sub.getOwnedByEpic(), epic);
            epicTasksList.changeEpicStatus(sub.getOwnedByEpic(), subTasksList);
        }
        if (tasksList.getTaskByID(taskID) != null) {
            Task task = tasksList.getTaskByID(taskID);
            task.setStatus(inputTaskStatus(userInput));
            tasksList.addNewTaskToList(taskID, task);
        }
    }

    public Task findTaskByID(int taskID) {
        if (getTasksList().getTaskByID(taskID) != null) {
            return (getTasksList().getTaskByID(taskID));
        }
        return null;
    }

    public EpicTask findEpicByID(int epicID) {
        if (epicTasksList.getTaskByID(epicID) != null) {
            return (epicTasksList.getTaskByID(epicID));
        }
        return null;
    }

    public SubTask findSubTaskByID(int subID) {
        if (subTasksList.getTaskByID(subID) != null) {
            return (subTasksList.getTaskByID(subID));
        }
        return null;
    }

    public void deleteTaskByID(int taskID) {

            tasksList.deleteTaskByID(taskID);

    }

    public void deleteEpicTaskByID(int epicID) {

            epicTasksList.deleteTaskByID(epicID);

    }

    public void deleteSubTaskByID(int subID) {

            subTasksList.deleteTaskByID(subID);

    }

    public void deleteAllTasks() {
        tasksList.deleteAllTasks();
        epicTasksList.deleteAllTasks();
        subTasksList.deleteAllTasks();
    }


    private TaskStatus inputTaskStatus(Scanner userInput) {
        System.out.println("Укажите статус задачи. Доступны варианты 1 - New, 2 - In_Progress, 3 - Done.");
        int command = inputCommand(userInput);
        switch (command) {
            case 1:
                return TaskStatus.NEW;
            case 2:
                return TaskStatus.IN_PROGRESS;
            case 3:
                return TaskStatus.DONE;
            default:
                System.out.println("Такого статуса нет. Установлен статус NEW по умолчанию");
                return TaskStatus.NEW;
        }
    }

    private int inputCommand(Scanner userInput) {
        int command;
        if (userInput.hasNextInt()) {
            command = userInput.nextInt();
            userInput.nextLine();
            return command;
        } else {
            return -1;
        }
    }

    public int getTaskID() {
        return taskID;
    }

    public TasksList getTasksList() {
        return tasksList;
    }

    public SubTasksList getSubTasksList() {
        return subTasksList;
    }

    public EpicTasksList getEpicTasksList() {
        return epicTasksList;
    }

    public void setTaskID() {
        this.taskID += 1;
    }
}
