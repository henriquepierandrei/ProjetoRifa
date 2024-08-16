package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.TicketOfUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketOfUserRepository extends JpaRepository<TicketOfUserModel,Long> {
    List<TicketOfUserModel> findByIdUser(long id);

    Optional<TicketOfUserModel> findByIdUserAndIdCampaign(long id, UUID uuid);

    List<TicketOfUserModel> findByIdAndIdCampaign(long id, UUID idCampaing);
}
