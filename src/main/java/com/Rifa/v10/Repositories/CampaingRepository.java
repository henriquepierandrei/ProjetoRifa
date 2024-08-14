package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.CampaingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaingRepository extends JpaRepository<CampaingModel, UUID> {
}
