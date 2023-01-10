package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DurationAdapter extends TypeAdapter<Duration> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        jsonWriter.value(duration.toString());
    }


    @Override
    public Duration read( JsonReader jsonReader) throws IOException {
        return Duration.parse( (CharSequence) jsonReader.nextString()) ;
    }
}
