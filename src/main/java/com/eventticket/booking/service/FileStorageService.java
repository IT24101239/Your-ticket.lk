package com.eventticket.booking.service;

import com.eventticket.booking.model.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileStorageService {
    private final String DATA_DIR = System.getProperty("user.dir") + File.separator + ".." + File.separator + ".." + File.separator + "data";
    private final String EVENTS_FILE = "events.json";

    private final String COUNTER_FILE = "counter.json";

    private final ObjectMapper objectMapper;

    public FileStorageService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Create data directory if it doesn't exist
        createDataDirectory();
    }

    private void createDataDirectory() {
        File directory = new File(DATA_DIR);
        System.out.println("Data directory path: " + directory.getAbsolutePath());
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("Data directory created: " + created);
        } else {
            System.out.println("Data directory already exists");
        }
    }

    public Map<Long, Event> loadEvents() {
        try {
            File file = new File(DATA_DIR + File.separator + EVENTS_FILE);
            if (!file.exists()) {
                return new ConcurrentHashMap<>();
            }

            Event[] events = objectMapper.readValue(file, Event[].class);
            Map<Long, Event> eventMap = new ConcurrentHashMap<>();
            for (Event event : events) {
                eventMap.put(event.getId(), event);
            }
            return eventMap;
        } catch (IOException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    public void saveEvents(Map<Long, Event> events) {
        try {
            File file = new File(DATA_DIR + File.separator + EVENTS_FILE);
            System.out.println("Saving events to: " + file.getAbsolutePath());
            System.out.println("Events to save: " + events.size());
            objectMapper.writeValue(file, events.values());
            System.out.println("Events saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public AtomicLong loadCounter() {
        try {
            File file = new File(DATA_DIR + File.separator + COUNTER_FILE);
            if (!file.exists()) {
                return new AtomicLong(1);
            }

            Long value = objectMapper.readValue(file, Long.class);
            return new AtomicLong(value);
        } catch (IOException e) {
            e.printStackTrace();
            return new AtomicLong(1);
        }
    }

    public void saveCounter(AtomicLong counter) {
        try {
            File file = new File(DATA_DIR + File.separator + COUNTER_FILE);
            objectMapper.writeValue(file, counter.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
