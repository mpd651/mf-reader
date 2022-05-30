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
import es.snc.mf_setup_reader.mf.IMaterialGenericService;

@Service
@PropertySource({ "classpath:application.properties" })
public class MaterialGenericServiceImpl implements IMaterialGenericService{

	private @Value("${mf.api.url}") String mfApiUrl;
	private @Value("${client.id}") String clientId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MaterialGenericServiceImpl.class);
	private static final String DEFAULT_CLIENTS = "clients";
	private static final String DEFAULT_MATERIAL_GENERICS = "materialGenerics";
	private static final String DEFAULT_AUTH_BEARER = "Bearer ";
	private static final String DEFAULT_AUTH_HEADER_NAME = "Authorization";

	private final RestTemplate restTemplate;

	@Autowired
	public MaterialGenericServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<MaterialGenericDto> searchAll(String token) {

		List<MaterialGenericDto> results= new ArrayList<>();
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_MATERIAL_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<List<MaterialGenericDto>> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<List<MaterialGenericDto>>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. MaterialService searchAll. ");
		}
		
		return results;
	}

	@Override
	public MaterialGenericDto create(String token, MaterialGenericDto dto) {
		MaterialGenericDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_MATERIAL_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<MaterialGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<MaterialGenericDto>(dto, headers), 
					new ParameterizedTypeReference<MaterialGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Material with "+dto.getCode()+" created");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. MaterialService create. ");
		}
		
		return result;
	}

	@Override
	public MaterialGenericDto update(String token, MaterialGenericDto dto) {
		MaterialGenericDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_MATERIAL_GENERICS)
					.pathSegment(dto.getId().toString());

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<MaterialGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.PUT, 
					new HttpEntity<MaterialGenericDto>(dto, headers), 
					new ParameterizedTypeReference<MaterialGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Material with "+dto.getCode()+" updated");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. MaterialService update. ");
		}
		
		return result;
	}
}
