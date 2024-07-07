package com.inn.url.shortener.dao;

import lombok.Data;

@Data
public class UpdateExpiryRequest {
	private String shortUrl;
	private int daysToAdd;

}