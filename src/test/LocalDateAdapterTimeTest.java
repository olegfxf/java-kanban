package test;

import adapter.DurationAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Task;
import adapter.LocalDateAdapterTime;

import java.time.Duration;
import java.time.LocalDateTime;

class LocalDateAdapterTimeTest {
    Task task;
    String taskSerialized = "";

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapterTime())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    @BeforeEach
    public void beforeEach() {
        task = new Task("nameTask" + 1, "descriptionTask" + 1);
        task.setStartTimeDuration(LocalDateTime.of(2022, 2, 2,
                10, 0), Duration.ofSeconds(222));
    }

    @Test
    void writeRead() {
        taskSerialized = gson.toJson(task);
        System.out.println("Serialized task:\n" + taskSerialized);

        Task taskDeserialized = gson.fromJson(taskSerialized, Task.class);
        System.out.println("Deserialized task:\n" + taskDeserialized);

    }

}