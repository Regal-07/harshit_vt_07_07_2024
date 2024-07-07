package com.inn.url.shortener.dao;

import lombok.Data;

@Data
public class UpdateUrlRequest {
	private String shortUrl;
	private String longUrl;

}
