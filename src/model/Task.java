package model;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Objects;

public class Task implements Comparable<Task>{
    protected Integer uid;
    protected String title;
    protected String description;
    protected StatusTask statusTask;

    protected LocalDateTime startTime;
    protected Duration duration;


    public Task() {
    }

    public Task(String title, String description) {
        this.uid = Uid.getUid();
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.NEW;
    }

    public Task(Integer uid, String title, String description, String status, LocalDateTime startTime, Duration duration) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.valueOf(status);
        this.startTime = startTime;
        this.duration = duration;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime(){
      return startTime.plus(duration);
    }

    public void setStartTimeDuration(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
    public int compareTo(Task task) {

        LocalDateTime startTimeThis  = this.startTime;
        LocalDateTime startTimeOther = task.getStartTime();
        if (task == null || startTimeThis == null || startTimeOther == null) return 1;
        if (startTimeThis.isAfter(startTimeOther)) {
            return 1;
        } else if (startTimeThis.isBefore(startTimeOther)) {
            return -1;
        } else {
            return 0;
        }
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
        return   uid +  ",TASK," + title + ","
                + statusTask.toString() + ","
                + description + ","
                + startTime + ","
                + duration;
    }
}

