package es.snc.mf_setup_reader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import es.snc.mf_setup_reader.service.IService;



@SpringBootApplication(scanBasePackages = { "es.snc.mf_setup_reader" })
public class App 
{

	@Autowired
	private IService excelReaderService;

	@Bean
	public static RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
	public void init() {
		excelReaderService.startService();
	}

}
