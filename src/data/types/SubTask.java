package data.types;

public class SubTask extends Task {
    private int ownedByEpic;

    public SubTask(String taskName, String description, TaskStatus status, int ownedByEpic) {
        super(taskName, description, status);
        this.ownedByEpic = ownedByEpic;
    }

    public SubTask(int id, String taskName, String description, TaskStatus status, int ownedByEpic) {
        super(id, taskName, description, status);
        this.ownedByEpic = ownedByEpic;
    }

    public int getOwnedByEpic() {
        return ownedByEpic;
    }

    public void setOwnedByEpic(int epicID) {
        ownedByEpic = epicID;
    }

}
