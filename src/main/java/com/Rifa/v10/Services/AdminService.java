package com.Rifa.v10.Services;

import com.Rifa.v10.Models.CampaingModel;
import com.Rifa.v10.Repositories.CampaingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired private CampaingRepository campaingRepository;


    public List<CampaingModel> findAllCampaings() {
        return this.campaingRepository.findAll();
    }
}
