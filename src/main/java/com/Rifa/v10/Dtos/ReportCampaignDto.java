package com.Rifa.v10.Dtos;

import com.Rifa.v10.Models.CampaingModel;

public record ReportCampaignDto(CampaingModel model, int quantityTicketMissing, int quantityBuyers) {
}
