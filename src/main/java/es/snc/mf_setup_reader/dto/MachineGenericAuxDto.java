package es.snc.mf_setup_reader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.snc.mf.common.dto.asset.MachineGenericDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineGenericAuxDto extends MachineGenericDto {

	private static final long serialVersionUID = 1L;

	private String lineCode;
}
