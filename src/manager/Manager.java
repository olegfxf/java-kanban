package manager;

import adapter.DurationAdapter;
import adapter.LocalDateAdapterTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class Manager {

    public static TaskManager getDefault() {
        //return new FileBackedTasksManager();
        return new HttpTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public static Gson getGson() {
       return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    }
}


