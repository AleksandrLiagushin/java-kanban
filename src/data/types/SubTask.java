package data.types;

public class SubTask extends Task {
    private int ownedByEpic;

    public int getOwnedByEpic() {
        return ownedByEpic;
    }

    public void setOwnedByEpic(int epicID) {
        this.ownedByEpic = epicID;
    }

    public static SubTask toSubTask (Task task) {
        SubTask sub = new SubTask();
        sub.setTaskName(task.getTaskName());
        sub.setDescription(task.getDescription());
        sub.setStatus(task.getStatus());
        sub.setCreationDate();
        return sub;
    }
}
