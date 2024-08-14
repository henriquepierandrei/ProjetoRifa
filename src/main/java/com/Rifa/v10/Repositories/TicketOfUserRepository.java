package com.Rifa.v10.Repositories;

import com.Rifa.v10.Models.TicketOfUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketOfUserRepository extends JpaRepository<TicketOfUserModel,Long> {
    List<TicketOfUserModel> findByIdUser(long id);

    TicketOfUserModel findByIdAndIdCampaign(long id, long idCampaing);
}
