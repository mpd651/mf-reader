package es.snc.mf_setup_reader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductionLineGenericAuxDto extends ProductionLineGenericDto{
	
	private static final long serialVersionUID = 1L;
	
	private String sectionCode;
}
