package model;

public class Epic extends Task {

    public Epic(String title, String description) {
        super(title, description);
        this.uid = Uid.getUid();
        this.statusTask = StatusTask.DONE;
    }

    public Epic(Integer uid, String title, String description, String status) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.valueOf(status);
    }

    public Integer getUid() {
        return uid;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return   uid +  ",EPIC," + title + "," + statusTask.toString() + "," + description;
    }
}
