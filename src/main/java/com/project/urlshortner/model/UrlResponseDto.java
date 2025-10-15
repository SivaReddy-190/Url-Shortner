package com.project.urlshortner.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlResponseDto {

    private String originalUrl;
    private String shortLink;
    private LocalDateTime expirationDate;
}
