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
import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.filter.asset.FactoryFilter;
import es.snc.mf.common.filter.asset.ProductionLineGenericFilter;
import es.snc.mf_setup_reader.mf.ILineService;


@Service
@PropertySource({ "classpath:application.properties" })
public class LineServiceImpl implements ILineService{

	private @Value("${mf.api.url}") String mfApiUrl;
	private @Value("${client.id}") String clientId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LineServiceImpl.class);
	private static final String DEFAULT_CLIENTS = "clients";
	private static final String DEFAULT_FACTORIES = "factories";
	private static final String DEFAULT_SECTIONS = "sections";
	private static final String DEFAULT_PRODUCTION_LINE_GENERICS= "productionLineGenerics";
	private static final String DEFAULT_SEARCH = "search";
	private static final String DEFAULT_AUTH_BEARER = "Bearer ";
	private static final String DEFAULT_AUTH_HEADER_NAME = "Authorization";

	private final RestTemplate restTemplate;

	@Autowired
	public LineServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public ProductionLineGenericDto searchByCode(String token, ProductionLineGenericFilter lineFilter,
			String factoryId, String sectionId) {

		ProductionLineGenericDto result = null;
		
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
					.pathSegment(sectionId)
					.pathSegment(DEFAULT_PRODUCTION_LINE_GENERICS)
					.pathSegment(DEFAULT_SEARCH)
					.queryParam("page", 0)
					.queryParam("size", 1);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity <ProductionLineGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<ProductionLineGenericFilter>(lineFilter, headers), 
					new ParameterizedTypeReference<ProductionLineGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. MfLineService searchByCode. ");
		}
		
		return result;
	
	}

	@Override
	public List<ProductionLineGenericDto> searchAll(String token, String factoryId, String sectionId) {

		List<ProductionLineGenericDto> results= new ArrayList<>();
		
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
					.pathSegment(sectionId)
					.pathSegment(DEFAULT_PRODUCTION_LINE_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<List<ProductionLineGenericDto>> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<List<ProductionLineGenericDto>>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. LineService searchAll. ");
		}
		
		return results;
	}

	@Override
	public ProductionLineGenericDto create(String token, ProductionLineGenericDto dto, String factoryId, String sectionId) {
		ProductionLineGenericDto result = null;
		
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
					.pathSegment(sectionId)
					.pathSegment(DEFAULT_PRODUCTION_LINE_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<ProductionLineGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<ProductionLineGenericDto>(dto, headers), 
					new ParameterizedTypeReference<ProductionLineGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Line with "+dto.getCode()+" created");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. LineService create. ");
		}
		
		return result;
	}

	@Override
	public ProductionLineGenericDto update(String token, ProductionLineGenericDto dto, String factoryId, String sectionId) {
		
		ProductionLineGenericDto result = null;
		
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
					.pathSegment(sectionId)
					.pathSegment(DEFAULT_PRODUCTION_LINE_GENERICS)
					.pathSegment(dto.getId().toString());

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<ProductionLineGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.PUT, 
					new HttpEntity<ProductionLineGenericDto>(dto, headers), 
					new ParameterizedTypeReference<ProductionLineGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Line with "+dto.getCode()+" updated");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. LineService update. ");
		}
		
		return result;
	}

}
