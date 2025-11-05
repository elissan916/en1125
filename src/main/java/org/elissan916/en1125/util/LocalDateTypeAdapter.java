package org.elissan916.en1125.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Gson type adapter (deserializer) for converting JSON date strings of the
 * form {@code yyyy-MM-dd} into {@link java.time.LocalDate} instances.
 *
 * <p>This adapter currently only implements deserialization since the project
 * only needs to parse dates from input files.
 */
public class LocalDateTypeAdapter implements JsonDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Parse a JSON string into a {@link LocalDate} using the configured
     * {@code yyyy-MM-dd} formatter.
     *
     * @param json the JSON element containing the date string
     * @param typeOfT the target type (LocalDate.class)
     * @param context Gson deserialization context
     * @return the parsed LocalDate
     * @throws JsonParseException if the JSON value cannot be parsed as a date
     */
    @Override
    public LocalDate deserialize(final JsonElement json, final Type typeOfT,
                                 final JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}
