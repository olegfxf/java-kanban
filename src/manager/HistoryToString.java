package manager;
import model.Task;

public class HistoryToString {
    static String historyToString(HistoryManager manager) {
        String historyIds = "";
        try { // сохраняем историю просмотров задач в строку
            for (Task task : manager.getHistory()) {
                historyIds += task.getUid() + ",";
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return historyIds;
    }

}
