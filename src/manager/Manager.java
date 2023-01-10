package manager;

public class Manager {

    public static TaskManager getDefault() {
        //return new FileBackedTasksManager();
        return new HttpTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


