package com.eventSystem.events.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTypeRequest {
    private String name;
    private String description;
    private Double price;
    private Integer totalAvailable;

}
