package com.ticketmanager.service;

import com.ticketmanager.model.Ticket;
import com.ticketmanager.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> getTicketById(String id) {
        return ticketRepository.findById(id);
    }

    public boolean checkTicketsByEvent(String eventId) {
        return ticketRepository.existsByEventId(eventId);
    }

    public List<Ticket> getTicketsByEvent(String eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    public Ticket updateTicket(String id, Ticket updatedTicket) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setCustomerName(updatedTicket.getCustomerName());
            ticket.setCustomerMail(updatedTicket.getCustomerMail());
            ticket.setBRLamount(updatedTicket.getBRLamount());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ingresso não encontrado"));
    }

    public void cancelTicket(String id) {
        ticketRepository.findById(id).ifPresent(ticket -> {
            ticket.setStatus("cancelado");
            ticketRepository.save(ticket);
        });
    }
    public void cancelTicketByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        for (Ticket ticket : tickets) {
            ticket.setStatus("cancelado");
            ticketRepository.save(ticket);
        }
}
}
