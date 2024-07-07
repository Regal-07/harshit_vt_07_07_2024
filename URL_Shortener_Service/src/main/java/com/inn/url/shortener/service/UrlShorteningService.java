package com.inn.url.shortener.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.inn.url.shortener.Custom_Exception.UrlNotFoundException;
import com.inn.url.shortener.entity.Url;
import com.inn.url.shortener.repository.UrlRepository;

@Service
public class UrlShorteningService {
	@Autowired
    private UrlRepository urlRepository;

    private static final String BASE_URL = "http://localhost:8080/";

    public String shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl();
        LocalDateTime now = LocalDateTime.now();

        Url url = new Url();
        url.setShortUrl(shortUrl);
        url.setLongUrl(longUrl);
        url.setCreatedAt(now);
        url.setExpiresAt(now.plusMonths(10));

        urlRepository.save(url);

        return BASE_URL + shortUrl;
    }

    @Cacheable(value = "urlCache", key = "#shortUrl")
    public String getLongUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null || url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("URL not found or expired");
        }
        return url.getLongUrl();
    }

    public Boolean updateShortUrl(String shortUrl, String newLongUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null || url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("URL not found or expired");
        }
        url.setLongUrl(newLongUrl);
        urlRepository.save(url);
        return true;
    }

    public Boolean updateExpiry(String shortUrl, int daysToAdd) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url == null || url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("URL not found or expired");
        }
        url.setExpiresAt(url.getExpiresAt().plusDays(daysToAdd));
        urlRepository.save(url);
        return true;
    }

    private String generateShortUrl() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
