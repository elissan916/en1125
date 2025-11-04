package org.elissan916.en1125.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateTypeAdapterTest {

    @Test
    void testDeserialize() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
        LocalDate ld = gson.fromJson("\"2020-09-03\"", LocalDate.class);
        assertEquals(LocalDate.of(2020, 9, 3), ld);
    }
}

