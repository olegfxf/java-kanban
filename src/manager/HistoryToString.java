package manager;
import model.Task;

public class HistoryToString {
    public static String historyToString(HistoryManager manager) {
        String historyIds = "";
        try {
            for (Task task : manager.getHistory()) {
                historyIds += task.getUid() + ",";
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return historyIds;
    }

}
