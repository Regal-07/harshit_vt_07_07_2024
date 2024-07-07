package com.inn.url.shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.url.shortener.entity.Url;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}
