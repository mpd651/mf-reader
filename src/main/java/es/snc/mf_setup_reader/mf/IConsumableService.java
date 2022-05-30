package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.asset.MachineGenericDto;
import es.snc.mf.common.dto.bom.ConsumableDto;

public interface IConsumableService {

	List<ConsumableDto> searchAll (String token);
	
	ConsumableDto create (String token, ConsumableDto dto);
	
	ConsumableDto update (String token, ConsumableDto dto);

}
