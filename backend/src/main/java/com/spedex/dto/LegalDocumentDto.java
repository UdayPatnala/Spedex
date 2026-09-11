package com.spedex.dto;

import java.time.LocalDateTime;

public class LegalDocumentDto {
    public String docType;
    public String title;
    public String content;
    public LocalDateTime lastUpdated;

    public LegalDocumentDto() {}

    public LegalDocumentDto(String docType, String title, String content, LocalDateTime lastUpdated) {
        this.docType = docType;
        this.title = title;
        this.content = content;
        this.lastUpdated = lastUpdated;
    }
}
