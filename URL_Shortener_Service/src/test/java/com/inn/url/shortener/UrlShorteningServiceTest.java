package com.inn.url.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.inn.url.shortener.Custom_Exception.UrlNotFoundException;
import com.inn.url.shortener.entity.Url;
import com.inn.url.shortener.repository.UrlRepository;
import com.inn.url.shortener.service.UrlShorteningService;

public class UrlShorteningServiceTest {

	@Mock
	private UrlRepository urlRepository;

	@InjectMocks
	private UrlShorteningService urlShorteningService;

	public UrlShorteningServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testShortenUrl() {
		String longUrl = "http://example.com";
		String shortUrl = "shortUrl123";

		Url url = new Url();
		url.setShortUrl(shortUrl);
		url.setLongUrl(longUrl);
		url.setCreatedAt(LocalDateTime.now());
		url.setExpiresAt(LocalDateTime.now().plusMonths(10));

		when(urlRepository.save(any(Url.class))).thenReturn(url);

		String result = urlShorteningService.shortenUrl(longUrl);

		assertTrue(result.contains(shortUrl));
	}

	@Test
	public void testGetLongUrl() {
		String shortUrl = "shortUrl123";
		String longUrl = "http://example.com";

		Url url = new Url();
		url.setShortUrl(shortUrl);
		url.setLongUrl(longUrl);
		url.setCreatedAt(LocalDateTime.now());
		url.setExpiresAt(LocalDateTime.now().plusMonths(10));

		when(urlRepository.findByShortUrl(shortUrl)).thenReturn(url);

		String result = urlShorteningService.getLongUrl(shortUrl);

		assertEquals(longUrl, result);
	}

	@Test
	public void testGetLongUrl_NotFound() {
		String shortUrl = "shortUrl123";

		when(urlRepository.findByShortUrl(shortUrl)).thenReturn(null);

		assertThrows(UrlNotFoundException.class, () -> {
			urlShorteningService.getLongUrl(shortUrl);
		});
	}

	@Test
	public void testUpdateShortUrl() {
		String shortUrl = "shortUrl123";
		String longUrl = "http://example.com";
		String newLongUrl = "http://newexample.com";

		Url url = new Url();
		url.setShortUrl(shortUrl);
		url.setLongUrl(longUrl);
		url.setCreatedAt(LocalDateTime.now());
		url.setExpiresAt(LocalDateTime.now().plusMonths(10));

		when(urlRepository.findByShortUrl(shortUrl)).thenReturn(url);

		Boolean result = urlShorteningService.updateShortUrl(shortUrl, newLongUrl);

		assertTrue(result);
	}

	@Test
	public void testUpdateExpiry() {
		String shortUrl = "shortUrl123";
		String longUrl = "http://example.com";
		int daysToAdd = 30;

		Url url = new Url();
		url.setShortUrl(shortUrl);
		url.setLongUrl(longUrl);
		url.setCreatedAt(LocalDateTime.now());
		url.setExpiresAt(LocalDateTime.now().plusMonths(10));

		when(urlRepository.findByShortUrl(shortUrl)).thenReturn(url);

		Boolean result = urlShorteningService.updateExpiry(shortUrl, daysToAdd);

		assertTrue(result);
	}
}
