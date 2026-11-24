package com.eventSystem.events.domain.dtos;

import com.eventSystem.events.domain.entities.Event;
import com.eventSystem.events.domain.entities.EventStatusEnum;
import com.eventSystem.events.domain.entities.TicketType;
import com.eventSystem.events.domain.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventResponseDto {
    private UUID id;

    private String name;

    private LocalDateTime start;

    private LocalDateTime end;

    private String venue;

    private LocalDateTime salesStart;

    private LocalDateTime salesEnd;

    private EventStatusEnum status;

    private List<CreateTicketTypeResponseDto>  ticketTypes=new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
