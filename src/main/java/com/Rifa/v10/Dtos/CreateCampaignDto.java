package com.Rifa.v10.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateCampaignDto(@NotEmpty String name, Double price, @NotEmpty String description, @NotNull boolean isOnline, @NotNull int quantityTickets, @NotNull List<Integer> numbersWinning) {
}
