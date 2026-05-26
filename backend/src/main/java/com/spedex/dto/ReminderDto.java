package com.spedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReminderDto {
    public Long id;
    public String title;
    public String subtitle;
    @JsonProperty("upi_handle")
    public String upiHandle;
    public Double amount;
    @JsonProperty("due_date")
    public String dueDate;
    @JsonProperty("autopay_enabled")
    public Boolean autopayEnabled;
    public String status;
}
