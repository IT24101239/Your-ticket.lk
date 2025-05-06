package com.eventticket.booking.controller;

import com.eventticket.booking.model.Event;

import com.eventticket.booking.service.EventService;
import com.eventticket.booking.service.EventSorter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
@Tag(name = "Event Management", description = "Event Management APIs")
public class EventController {

    @Autowired
    private EventService eventService;

    @Operation(summary = "Get all events", description = "Returns a list of all events, optionally sorted by date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved events",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(
            @Parameter(description = "Sort criteria (date)") @RequestParam(required = false) String sortBy) {
        try {
            List<Event> events;
            if (sortBy != null) {
                EventSorter.SortCriteria criteria = EventSorter.SortCriteria.DATE;
                events = eventService.getSortedEvents(criteria);
            } else {
                events = eventService.getAllEvents();
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get event by ID", description = "Returns a single event by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the event",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @Parameter(description = "ID of the event to retrieve") @PathVariable Long id) {
        try {
            return eventService.getEventById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Create a new event", description = "Creates a new event with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the event",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Event> createEvent(
            @Parameter(description = "Event details", required = true) @RequestBody Event event) {
        try {
            Event createdEvent = eventService.createEvent(event);
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update an event", description = "Updates an existing event with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the event",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @Parameter(description = "ID of the event to update") @PathVariable Long id,
            @Parameter(description = "Updated event details", required = true) @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(id, event);
            if (updatedEvent != null) {
                return ResponseEntity.ok(updatedEvent);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Delete an event", description = "Deletes an event by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the event"),
        @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID of the event to delete") @PathVariable Long id) {
        try {
            if (eventService.deleteEvent(id)) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}