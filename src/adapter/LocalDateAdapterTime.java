package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapterTime extends TypeAdapter<LocalDateTime> {
    public LocalDateAdapterTime() {
    }

    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(localDateTime.format(formatterWriter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}
