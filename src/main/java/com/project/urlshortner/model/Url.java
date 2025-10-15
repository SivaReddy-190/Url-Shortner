package com.project.urlshortner.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Url {

    @Id
    @GeneratedValue
    private long id;
    @Lob
    private String originalUrl;
    private String shortLink;
    private LocalDateTime createdTime;
    private LocalDateTime expirationTime;



}
