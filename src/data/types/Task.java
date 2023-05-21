package data.types;

import java.util.Date;
import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    private TaskStatus status;
    private Date creationDate;

    public Task(String taskName, String description, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setCreationDate() {
        this.creationDate = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return (taskName.equals(task.taskName)
                && description.equals(task.description)
                && status.equals(task.status)
                && creationDate.equals(task.creationDate));
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, description, status, creationDate);
    }

    @Override
    public String toString() {
        String info = "Task{";

        if (taskName != null) {
            info += "taskName='" + taskName + '\'';
        } else {
            info += "taskName='null'";
        }
        if (description != null) {
            info += ", description='" + description + '\'';
        } else {
            info += ", description='null'";
        }
        if (status != null) {
            info += ", status='" + status + '\'';
        } else {
            info += ", status='null'";
        }

        return (info + '}');
    }
}
