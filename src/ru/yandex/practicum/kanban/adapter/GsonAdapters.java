package ru.yandex.practicum.kanban.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class GsonAdapters {

    public GsonAdapters() {
    }

    public static TypeAdapter<LocalDateTime> localDateTime() {
        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter writer, LocalDateTime time) throws IOException {
                writer.value(time != null ? time.toString() : null);
            }

            @Override
            public LocalDateTime read(JsonReader reader) throws IOException {
                return LocalDateTime.parse(reader.nextString());
            }
        };
    }

    public static TypeAdapter<Duration> duration() {
        return new TypeAdapter<>() {
            @Override
            public void write(JsonWriter writer, Duration duration) throws IOException {
                writer.value(duration != null ? duration.toMinutes() : null);
            }

            @Override
            public Duration read(JsonReader reader) throws IOException {
                return Duration.ofMinutes(reader.nextLong());
            }
        };
    }
}
