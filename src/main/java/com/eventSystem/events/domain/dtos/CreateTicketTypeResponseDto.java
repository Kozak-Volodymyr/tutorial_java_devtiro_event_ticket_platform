package com.eventSystem.events.domain.dtos;

import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.domain.entities.Ticket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeResponseDto {
    private UUID id;

    private String name;

    private Double price;

    private Integer totalAvailable;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
