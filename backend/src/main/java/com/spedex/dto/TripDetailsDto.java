package com.spedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TripDetailsDto {
    public Long id;
    public String name;
    public String status;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("completed_at")
    public String completedAt;
    @JsonProperty("total_spend")
    public Double totalSpend;
    @JsonProperty("cash_spend")
    public Double cashSpend;
    @JsonProperty("card_online_spend")
    public Double cardOnlineSpend;
    @JsonProperty("category_breakdown")
    public List<CategoryBreakdownDto> categoryBreakdown;
    public List<TransactionDto> transactions;
}
