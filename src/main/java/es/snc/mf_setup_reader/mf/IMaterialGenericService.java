package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.bom.MaterialGenericDto;

public interface IMaterialGenericService {

	List<MaterialGenericDto> searchAll (String token);
	
	MaterialGenericDto create (String token, MaterialGenericDto dto);
	
	MaterialGenericDto update (String token, MaterialGenericDto dto);

}
