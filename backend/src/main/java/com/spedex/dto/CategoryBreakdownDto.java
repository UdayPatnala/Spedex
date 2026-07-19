package com.spedex.dto;

public class CategoryBreakdownDto {
    public String category;
    public Double amount;
    public Double percentage;

    public CategoryBreakdownDto() {
    }

    public CategoryBreakdownDto(String category, Double amount, Double percentage) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
    }
}
