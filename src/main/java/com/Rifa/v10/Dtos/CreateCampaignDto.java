package com.Rifa.v10.Dtos;

import java.util.List;

public record CreateCampaignDto(String name, String description, boolean isOnline, int quantityTickets, List<Integer> numbersWinning) {
}
