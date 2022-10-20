import java.util.HashMap;

public class Epic {
    protected Integer uid;
    protected String title;
    protected String description;
    protected StatusTask statusTask;
    HashMap<Integer, Task> listSubtask = new HashMap<>();


    public Epic(String title, String description) {
        this.title = title;
        this.description = description;
        this.uid = Uid.getUid();
        this.statusTask = StatusTask.NEW;
    }

    public Integer getUid() {
        return uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setListSubtask(HashMap<Integer, Task> listSubtask) {
        this.listSubtask = listSubtask;
        checkStatus();
    }

    public void clearSubtask() {
        listSubtask.clear();
        checkStatus();
    }

    public void removeSubtaskById(Integer id) {
        listSubtask.remove(id);
        checkStatus();
    }

    public Task getSubtaskById(Integer id) {
        return listSubtask.get(id);
    }

    public HashMap<Integer, Task> getAllSubtask() {
        return listSubtask;
    }


    public void updateSubtask(Integer id, Task subTask) {
        listSubtask.put(id, subTask);
        checkStatus();
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void checkStatus() {
        int statusNEW = 0;
        int statusIN_PROGRESS = 0;
        int statusDONE = 0;

        for (Integer iter : listSubtask.keySet()) {
            Task task = getSubtaskById(iter);
            if (task.getStatus() == StatusTask.NEW) {
                statusNEW++;
            } else if (task.getStatus() == StatusTask.IN_PROGRESS) {
                statusIN_PROGRESS++;
            } else if (task.getStatus() == StatusTask.DONE) {
                statusDONE++;
            }
        }
        if (statusDONE == 0 && statusIN_PROGRESS == 0 && listSubtask.size() != 0) {
            statusTask = StatusTask.NEW;
        } else if ((statusNEW == 0 && statusIN_PROGRESS == 0) || listSubtask.size() == 0) {
            statusTask = StatusTask.DONE;
        } else {
            statusTask = StatusTask.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic["
                + "uid=" + uid + ", "
                + "title=" + title + ", "
                + "description=" + description + ", "
                + "statusTask=" + statusTask + ", "
                + "listSubtask=" + listSubtask + ']';
    }
}
