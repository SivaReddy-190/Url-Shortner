package com.project.urlshortner.controller;
import com.project.urlshortner.model.Url;
import com.project.urlshortner.model.UrlDto;
import com.project.urlshortner.model.UrlErrorResponseDto;
import com.project.urlshortner.model.UrlResponseDto;

import com.project.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;


@RestController
public class UrlShortnerController {

    @Autowired
    private UrlService service;


    @PostMapping("/generate")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url urlToRet = service.generateShortLink(urlDto);

        if(urlToRet != null) {
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
            urlResponseDto.setExpirationDate(urlToRet.getExpirationTime());
            urlResponseDto.setShortLink(urlToRet.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);
        }

        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("404");
        urlErrorResponseDto.setError("Error while processing your request, please try again");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);

    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setStatus("Invalid Url");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }
        Url urlToRet = service.getEncodecUrl(shortLink);

        if(urlToRet==null) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("404");
            urlErrorResponseDto.setStatus("Url doesn't exist or might have been expired");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        if(urlToRet.getExpirationTime().isBefore(LocalDateTime.now())) {
            service.deleteShortLink(urlToRet);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("200");
            urlErrorResponseDto.setStatus("Url expired");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto,HttpStatus.OK);
        }

        response.sendRedirect(urlToRet.getOriginalUrl());
        return null;
    }
}
