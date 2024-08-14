package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.TicketsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketsRepository extends JpaRepository<TicketsModel, UUID> {
}
