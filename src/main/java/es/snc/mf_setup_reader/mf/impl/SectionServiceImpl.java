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

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.filter.asset.FactoryFilter;
import es.snc.mf.common.filter.asset.SectionFilter;
import es.snc.mf_setup_reader.dto.SectionAuxDto;
import es.snc.mf_setup_reader.mf.ISectionService;

@Service
@PropertySource({ "classpath:application.properties" })
public class SectionServiceImpl implements ISectionService{

	private @Value("${mf.api.url}") String mfApiUrl;
	private @Value("${client.id}") String clientId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SectionServiceImpl.class);
	private static final String DEFAULT_CLIENTS = "clients";
	private static final String DEFAULT_FACTORIES = "factories";
	private static final String DEFAULT_SECTIONS = "sections";
	private static final String DEFAULT_SEARCH = "search";
	private static final String DEFAULT_AUTH_BEARER = "Bearer ";
	private static final String DEFAULT_AUTH_HEADER_NAME = "Authorization";

	private final RestTemplate restTemplate;

	@Autowired
	public SectionServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public List<SectionDto> searchAll(String token, String id) {

		List<SectionDto> results= new ArrayList<>();
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_FACTORIES)
					.pathSegment(id)
					.pathSegment(DEFAULT_SECTIONS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<List<SectionDto>> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<List<SectionDto>>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. SectionService searchAll. ");
		}
		
		return results;
	}

	@Override
	public SectionDto create(String token, SectionDto dto, String id) {
		SectionDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_FACTORIES)
					.pathSegment(id)
					.pathSegment(DEFAULT_SECTIONS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<SectionDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<SectionDto>(dto, headers), 
					new ParameterizedTypeReference<SectionDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Section with "+dto.getCode()+" created");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. SectionService create. ");
		}
		
		return result;
	}

	@Override
	public SectionAuxDto update(String token, SectionDto dto, String id) {
		
		SectionAuxDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_FACTORIES)
					.pathSegment(id)
					.pathSegment(DEFAULT_SECTIONS)
					.pathSegment(dto.getId().toString());

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<SectionAuxDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.PUT, 
					new HttpEntity<SectionDto>(dto, headers), 
					new ParameterizedTypeReference<SectionAuxDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Section with "+dto.getCode()+" updated");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. SectionService update. ");
		}
		
		return result;
	}

	@Override
	public SectionDto searchByCode(String token, SectionFilter sectionFilter, String id) {
		SectionDto result = null;
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);
			
			
			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_FACTORIES)
					.pathSegment(id)
					.pathSegment(DEFAULT_SECTIONS)
					.pathSegment(DEFAULT_SEARCH)
					.queryParam("page", 0)
					.queryParam("size", 1);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity <SectionDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<SectionFilter>(sectionFilter, headers), 
					new ParameterizedTypeReference<SectionDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. SectionService searchByCode. ");
		}
		
		return result;
	}

	@Override
	public SectionDto searchById(String token, String factoryId, String id) {

		SectionDto results= new SectionDto();
		
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set(DEFAULT_AUTH_HEADER_NAME, DEFAULT_AUTH_BEARER + token);

			UriComponentsBuilder uriBuilder = UriComponentsBuilder
					.fromHttpUrl(mfApiUrl)
					.pathSegment(DEFAULT_CLIENTS)
					.pathSegment(clientId)
					.pathSegment(DEFAULT_FACTORIES)
					.pathSegment(factoryId)
					.pathSegment(DEFAULT_SECTIONS)
					.pathSegment(id);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<SectionDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<SectionDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. SectionService searchById. ");
		}
		
		return results;
	}

}
