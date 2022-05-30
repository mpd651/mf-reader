package es.snc.mf_setup_reader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.snc.mf.common.dto.asset.SectionDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionAuxDto extends SectionDto {
	
	private static final long serialVersionUID = 1L;
	
	private String factoryCode;

}
