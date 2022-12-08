package model;

import java.util.Objects;

public class Task {
    protected Integer uid;
    protected String title;
    protected String description;
    protected StatusTask statusTask;

    public Task() {
    }

    public Task(String title, String description) {
        this.uid = Uid.getUid();
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.NEW;
    }

    // Конструктор для формирования задачи из данных файла
    public Task(Integer uid, String title, String description, String status) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.valueOf(status);
    }


    public Integer getUid() {
        return uid;
    }

    public void startTask() {
        statusTask = StatusTask.IN_PROGRESS;
    }

    public void finishTask() {
        statusTask = StatusTask.DONE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTask getStatus() {
        return statusTask;
    }

    public void setStatus(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(uid, task.uid) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && statusTask == task.statusTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, title, description, statusTask);
    }

    @Override
    public String toString() {
        return   uid +  ",TASK," + title + "," + statusTask.toString() + "," + description;
    }
}

