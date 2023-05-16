package servises;

import data.storage.EpicTasksList;
import data.storage.SubTasksList;
import data.storage.TasksList;
import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;

import java.util.Scanner;

public class TaskManager {

    private int taskID;
    TasksList tasksList = new TasksList();
    SubTasksList subTasksList = new SubTasksList();
    EpicTasksList epicTasksList = new EpicTasksList();

    public void createNewTask(Scanner userInput) {
        Task task = feelTaskDetails(userInput);
        tasksList.addNewTaskToList(getTaskID(), task);
    }

    public void createNewEpic(Scanner userInput) {
        Task task = feelTaskDetails(userInput);
        EpicTask epic = EpicTask.toEpic(task);
        epicTasksList.addEpicTaskToList(getTaskID(), epic);
    }

    public void createSubTask(Scanner userInput) {
        Task task = feelTaskDetails(userInput);
        System.out.println("Введите ID эпика");
        int epicID = inputCommand(userInput);
        if (epicTasksList.getTaskByID(epicID) != null) {
            SubTask sub = SubTask.toSubTask(task);
            sub.setOwnedByEpic(epicID);
            EpicTask epic = epicTasksList.getTaskByID(epicID);
            epic.addToSubTasksList(getTaskID());
            epicTasksList.addEpicTaskToList(epicID, epic);
            subTasksList.addSubTaskToList(getTaskID(), sub);
            epicTasksList.changeEpicStatus(sub.getOwnedByEpic(), subTasksList);
        } else {
            System.out.println("Такого эпика нет. Подзадача переведена в задачи");
            tasksList.addNewTaskToList(getTaskID(), task);
        }
    }

    public Task feelTaskDetails(Scanner userInput){
        Task task = new Task();

        setTaskID();

        System.out.println("Введите название задачи:");
        task.setTaskName(userInput.nextLine());

        System.out.println("Введите описание задачи:");
        task.setDescription(userInput.nextLine());

        task.setStatus(inputTaskStatus(userInput));

        return task;
    }

    public void moveSubTaskToTask(int subID) {
        EpicTask epic = epicTasksList.getTaskByID(subTasksList.getTaskByID(subID).getOwnedByEpic());
        epic.deleteSubTaskFromList(subID);
        tasksList.addNewTaskToList(subID, subTasksList.getTaskByID(subID));
        epicTasksList.addEpicTaskToList(subTasksList.getTaskByID(subID).getOwnedByEpic(), epic);
        subTasksList.deleteTaskByID(subID);
    }

    public void moveTaskToSubTask(Scanner userInput, int taskID) {
        SubTask subTask;
        subTask = SubTask.toSubTask(tasksList.getTaskByID(taskID));
        System.out.println("Ввести ID");
        int epicID = userInput.nextInt();
        userInput.nextLine();
        subTask.setOwnedByEpic(epicID);
        tasksList.deleteTaskByID(taskID);
        subTasksList.addSubTaskToList(taskID, subTask);
        epicTasksList.addSubTaskByID(epicID, taskID);
    }

    public void moveTaskToEpic(int taskID) {
        EpicTask epic;
        epic = EpicTask.toEpic(tasksList.getTaskByID(taskID));
        tasksList.deleteTaskByID(taskID);
        epicTasksList.addEpicTaskToList(taskID, epic);
    }

    public void changeTaskStatus(int taskID, Scanner userInput) {
        if (subTasksList.getTaskByID(taskID) != null) {
            SubTask sub = subTasksList.getTaskByID(taskID);
            EpicTask epic = epicTasksList.getTaskByID(sub.getOwnedByEpic());
            epic.addToSubTasksList(taskID);
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

    public void findTaskByID(int taskID) {
        if (getTasksList().getTaskByID(taskID) != null) {
            System.out.println(getTasksList().getTaskByID(taskID));
        }
        if (getEpicTasksList().getTaskByID(taskID) != null) {
            System.out.println(getEpicTasksList().getTaskByID(taskID));
        }
        if (getSubTasksList().getTaskByID(taskID) != null) {
            System.out.println(getSubTasksList().getTaskByID(taskID));
        }
    }

    public void deleteAnyTaskByID(int taskID) {
        if (getTasksList().getTaskByID(taskID) != null) {
            getTasksList().deleteTaskByID(taskID);
        }
        if (getEpicTasksList().getTaskByID(taskID) != null) {
            getEpicTasksList().deleteTaskByID(taskID);
        }
        if (getSubTasksList().getTaskByID(taskID) != null) {
            getSubTasksList().deleteTaskByID(taskID);
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

    private String inputTaskStatus(Scanner userInput) {
        while (true) {
            System.out.println("Укажите статус задачи. Доступны варианты New, In_Progress, Done. Регистр не важен");
            String status = userInput.nextLine().toUpperCase();
            if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
                return status;
            }
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
