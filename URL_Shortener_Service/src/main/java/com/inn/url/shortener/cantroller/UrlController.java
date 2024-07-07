package com.inn.url.shortener.cantroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.url.shortener.dao.UpdateExpiryRequest;
import com.inn.url.shortener.dao.UpdateUrlRequest;
import com.inn.url.shortener.dao.UrlRequest;
import com.inn.url.shortener.service.UrlShorteningService;

@RestController
@RequestMapping("/api")
public class UrlController {
	   @Autowired
	    private UrlShorteningService urlShorteningService;

	    @PostMapping("/shorten")
	    public String shortenUrl(@RequestBody UrlRequest request) {
	        return urlShorteningService.shortenUrl(request.getLongUrl());
	    }

	    @GetMapping("/{shortUrl}")
	    public String redirect(@PathVariable String shortUrl) {
	        return urlShorteningService.getLongUrl(shortUrl);
	    }

	    @PutMapping("/update")
	    public Boolean updateShortUrl(@RequestBody UpdateUrlRequest request) {
	        return urlShorteningService.updateShortUrl(request.getShortUrl(), request.getLongUrl());
	    }

	    @PutMapping("/update-expiry")
	    public Boolean updateExpiry(@RequestBody UpdateExpiryRequest request) {
	        return urlShorteningService.updateExpiry(request.getShortUrl(), request.getDaysToAdd());
	    }
}
