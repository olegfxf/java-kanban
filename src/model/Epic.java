package model;

public class Epic extends Task {

    public Epic(String title, String description) {
        super(title, description);
        this.uid = Uid.getUid();
        this.statusTask = StatusTask.NEW;
    }
    public Integer getUid() {
        return uid;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public StatusTask getStatusTask() {
        return statusTask;
    }
    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "model.Epic["
                + "uid=" + uid + ", "
                + "title=" + title + ", "
                + "description=" + description + ", "
                + "statusTask=" + statusTask + ']';

    }
}
