

public class Task {
    protected Integer uid;
    protected String title;
    protected String description;
    protected StatusTask statusTask;

    public Task(String title, String description) {

        this.uid = Uid.getUid();
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.NEW;
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

    public Integer getUid() {
        return uid;
    }


    public StatusTask getStatus() {
        return statusTask;
    }

    public void setStatus(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusTask=" + statusTask +
                '}';
    }
}

