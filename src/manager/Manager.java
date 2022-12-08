package manager;

public class Manager {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


