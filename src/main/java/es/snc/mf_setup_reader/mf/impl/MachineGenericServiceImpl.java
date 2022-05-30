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

import es.snc.mf.common.dto.asset.MachineGenericDto;
import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import es.snc.mf_setup_reader.mf.IMachineGenericService;

@Service
@PropertySource({ "classpath:application.properties" })
public class MachineGenericServiceImpl implements IMachineGenericService{

	private @Value("${mf.api.url}") String mfApiUrl;
	private @Value("${client.id}") String clientId;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineGenericServiceImpl.class);
	private static final String DEFAULT_CLIENTS = "clients";
	private static final String DEFAULT_FACTORIES = "factories";
	private static final String DEFAULT_SECTIONS = "sections";
	private static final String DEFAULT_PRODUCTION_LINE_GENERICS= "productionLines";
	private static final String DEFAULT_MACHINE_GENERICS="machineGenerics";
	private static final String DEFAULT_SEARCH = "search";
	private static final String DEFAULT_AUTH_BEARER = "Bearer ";
	private static final String DEFAULT_AUTH_HEADER_NAME = "Authorization";

	private final RestTemplate restTemplate;

	@Autowired
	public MachineGenericServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<MachineGenericDto> searchAll(String token, String factoryId, String sectionId, String lineId) {

		List<MachineGenericDto> results= new ArrayList<>();
		
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
					.pathSegment(lineId)
					.pathSegment(DEFAULT_MACHINE_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<List<MachineGenericDto>> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.GET, 
					new HttpEntity<>(headers), 
					new ParameterizedTypeReference<List<MachineGenericDto>>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				results = response.getBody();
			}

		} catch (Exception e) {

			LOGGER.error("Failed to connect with My Factory API. MachineService searchAll. ");
		}
		
		return results;
	}

	@Override
	public MachineGenericDto create(String token, MachineGenericDto dto, String factoryId, String sectionId,
			String lineId) {
		MachineGenericDto result = null;
		
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
					.pathSegment(lineId)
					.pathSegment(DEFAULT_MACHINE_GENERICS);

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<MachineGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.POST, 
					new HttpEntity<MachineGenericDto>(dto, headers), 
					new ParameterizedTypeReference<MachineGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Machine with "+dto.getCode()+" created");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. MachineService create. ");
		}
		
		return result;
	}

	@Override
	public MachineGenericDto update(String token, MachineGenericDto dto, String factoryId, String sectionId,
			String lineId) {
		
		MachineGenericDto result = null;
		
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
					.pathSegment(lineId)
					.pathSegment(DEFAULT_MACHINE_GENERICS)
					.pathSegment(dto.getId().toString());

			LOGGER.debug(uriBuilder.toUriString());

			ResponseEntity<MachineGenericDto> response = restTemplate.exchange(
					uriBuilder.toUriString(),
					HttpMethod.PUT, 
					new HttpEntity<MachineGenericDto>(dto, headers), 
					new ParameterizedTypeReference<MachineGenericDto>() {
					});

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				result = response.getBody();
				LOGGER.info("Machine with "+dto.getCode()+" updated");
			}

		} catch (Exception e) {
			LOGGER.error("Failed to connect with My factory API. MachineService update. ");
		}
		
		return result;
	}
	
	
}
