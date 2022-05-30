package es.snc.mf_setup_reader.mf.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.snc.mf.common.dto.bom.ConsumableDto;
import es.snc.mf.common.dto.bom.MaterialGenericDto;
import es.snc.mf_setup_reader.mf.IConsumableService;

@Service
@PropertySource({ "classpath:application.properties" })
public class ConsumableServiceImpl implements IConsumableService{

	private @Value("${mf.api.url}") String mfApiUrl;
	private @Value("${client.id}") String clientId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumableServiceImpl.class);
	private static final String DEFAULT_CLIENTS = "clients";
	private static final String DEFAULT_CONSUMABLES = "consumables";
	private static final String DEFAULT_AUTH_BEARER = "Bearer ";
	private static final String DEFAULT_AUTH_HEADER_NAME = "Authorization";

	private final RestTemplate restTemplate;

	@Autowired
	public ConsumableServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public List<ConsumableDto> searchAll(String token) {

		List<ConsumableDto> results= new ArrayList<>();
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_CONSUMABLES);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<List<ConsumableDto>> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<List<ConsumableDto>>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. ConsumableService searchAll. ");
		}
		
		return results;
	}

	@Override
	public ConsumableDto create(String token, ConsumableDto dto) {
		ConsumableDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_CONSUMABLES);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<ConsumableDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<ConsumableDto>(dto, headers), 
					new ParameterizedTypeReference<ConsumableDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Consumable with "+dto.getCode()+" created");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. ConsumableService create. ");
		}
		
		return result;
	}

	@Override
	public ConsumableDto update(String token, ConsumableDto dto) {
		ConsumableDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_CONSUMABLES)
					.pathSegment(dto.getId().toString());

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<ConsumableDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.PUT, 
					new HttpEntity<ConsumableDto>(dto, headers), 
					new ParameterizedTypeReference<ConsumableDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Consumable with "+dto.getCode()+" updated");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. ConsumableService update. ");
		}
		
		return result;
	}

}
