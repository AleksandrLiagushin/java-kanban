package data.types;

import java.util.Date;
import java.util.Objects;

public class Task {
    private String taskName;
    private String description;
    private TaskStatus status;
    private Date creationDate;

    public String getTaskName() {
        return taskName;
    }

    public Task setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task setStatus(TaskStatus status) {
        this.status = status;
        return this;
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