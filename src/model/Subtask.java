package model;

import java.util.Objects;

public class Subtask extends Task {
    Integer idEpic;

    public Subtask(String title, String description) {
        super(title, description);
        this.uid = Uid.getUid();
        this.statusTask = StatusTask.NEW;
    }
    public Subtask(Integer uid, String title, String description, String status, Integer idEpic) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.statusTask = StatusTask.valueOf(status);
        this.idEpic = idEpic;
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

    ///2,SUBTASK,nameSubtask0,NEW,descriptionSubtask0,idEpic


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(idEpic, subtask.idEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEpic);
    }

    @Override
    public String toString() {
        return   uid +  ",SUBTASK," + title + ","+  statusTask.toString() + "," + description+"," + idEpic;

    }

}
