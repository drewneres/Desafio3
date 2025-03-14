package com.ticketmanager.repository;

import com.ticketmanager.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    boolean existsByEventId(String eventId);
    List<Ticket> findByEventId(String eventId);
}
