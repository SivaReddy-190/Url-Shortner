package com.project.urlshortner.service;

import com.google.common.hash.Hashing;
import com.project.urlshortner.model.Url;
import com.project.urlshortner.model.UrlDto;
import com.project.urlshortner.repository.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UrlService {

    @Autowired
    private UrlRepository repository;

    public Url generateShortLink(UrlDto urlDto) {

        if(StringUtils.isNotEmpty(urlDto.getUrl())) {
            String encodeUrl = encodeUrl(urlDto.getUrl());
            Url urlToPersist = new Url();
            urlToPersist.setShortLink(encodeUrl);
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setCreatedTime(LocalDateTime.now());
            urlToPersist.setExpirationTime(getExpirationTime(urlDto.getExpirationDate(),urlToPersist.getCreatedTime()));
            Url urlToRet = persistshortLink(urlToPersist);

            if(urlToRet!=null) return urlToRet;
            return null;
        }
        return null;


    }

    private LocalDateTime getExpirationTime(String expirationDate, LocalDateTime createdTime) {
        if(StringUtils.isBlank(expirationDate)) return  createdTime.plusSeconds(60);

        LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
        return expirationDateToRet;
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed().hashString(time.toString(), StandardCharsets.UTF_8).toString();
        return encodedUrl;
    }

    public Url persistshortLink(Url url) {
        Url urlToRet = repository.save(url);
        return urlToRet;
    }

    public Url getEncodecUrl(String url) {
        return repository.findByShortLink(url);
    }

    public void deleteShortLink(Url url) {
        repository.delete(url);
    }

}
