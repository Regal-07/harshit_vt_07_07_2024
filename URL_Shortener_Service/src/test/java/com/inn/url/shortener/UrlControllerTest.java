package com.inn.url.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.inn.url.shortener.cantroller.UrlController;
import com.inn.url.shortener.dao.UpdateExpiryRequest;
import com.inn.url.shortener.dao.UpdateUrlRequest;
import com.inn.url.shortener.dao.UrlRequest;
import com.inn.url.shortener.service.UrlShorteningService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Mock
	private UrlShorteningService urlShorteningService;

	@InjectMocks
	private UrlController urlController;

	public UrlControllerTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testShortenUrl() {
		String longUrl = "http://example.com";
		String shortUrl = "http://localhost:" + port + "/api/shortUrl123";

		UrlRequest urlDao = new UrlRequest();
		urlDao.setLongUrl(longUrl);

		when(urlShorteningService.shortenUrl(longUrl)).thenReturn(shortUrl);

		HttpEntity<UrlRequest> request = new HttpEntity<>(urlDao);
		ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/shorten",
				HttpMethod.POST, request, String.class);

		assertEquals(shortUrl, response.getBody());
	}

	@Test
	public void testGetLongUrl() {
		String shortUrl = "shortUrl123";
		String longUrl = "http://example.com";

		when(urlShorteningService.getLongUrl(shortUrl)).thenReturn(longUrl);

		ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/api/" + shortUrl,
				HttpMethod.GET, null, String.class);

		assertEquals(longUrl, response.getBody());
	}

	@Test
	public void testUpdateShortUrl() {
		UpdateUrlRequest updateUrlRequest = new UpdateUrlRequest();
		updateUrlRequest.setShortUrl("shortUrl123");
		updateUrlRequest.setLongUrl("http://newexample.com");

		when(urlShorteningService.updateShortUrl(updateUrlRequest.getShortUrl(), updateUrlRequest.getLongUrl()))
				.thenReturn(true);

		HttpEntity<UpdateUrlRequest> request = new HttpEntity<>(updateUrlRequest);
		ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/update",
				HttpMethod.POST, request, Boolean.class);

		assertTrue(response.getBody());
	}

	@Test
	public void testUpdateExpiry() {
		UpdateExpiryRequest updateExpiryRequest = new UpdateExpiryRequest();
		updateExpiryRequest.setShortUrl("shortUrl123");
		updateExpiryRequest.setDaysToAdd(30);

		when(urlShorteningService.updateExpiry(updateExpiryRequest.getShortUrl(), updateExpiryRequest.getDaysToAdd()))
				.thenReturn(true);

		HttpEntity<UpdateExpiryRequest> request = new HttpEntity<>(updateExpiryRequest);
		ResponseEntity<Boolean> response = restTemplate.exchange("http://localhost:" + port + "/api/update-expiry",
				HttpMethod.POST, request, Boolean.class);

		assertTrue(response.getBody());
	}
}
