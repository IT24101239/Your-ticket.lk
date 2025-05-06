package com.eventticket.booking.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String venue;
    private BigDecimal price;

    private String imageUrl;

    public Event() {
    }

    public Event(String name, String description, LocalDateTime startDateTime,
                LocalDateTime endDateTime, String venue, BigDecimal price,
                String imageUrl) {
        this.name = name;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.venue = venue;
        this.price = price;

        this.imageUrl = imageUrl;
    }
}