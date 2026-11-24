package com.eventSystem.events.repositories;

import com.eventSystem.events.domain.entities.QrCode;
import com.eventSystem.events.domain.entities.QrCodeStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID id, UUID ticketPurchaserId);
    Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);
}
