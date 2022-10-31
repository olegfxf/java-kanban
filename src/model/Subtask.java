package model;

public class Subtask extends Task {
    Integer idEpic;

    public Subtask(String title, String description) {
        super(title, description);
        this.uid = Uid.getUid();
        this.statusTask = StatusTask.NEW;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                ", idEpic=" + idEpic +
                ", uid=" + uid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusTask=" + statusTask +
                '}';
    }

}
