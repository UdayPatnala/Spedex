package com.spedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TripDto {
    public Long id;
    public String name;
    public String status;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("completed_at")
    public String completedAt;
}
