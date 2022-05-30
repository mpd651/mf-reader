package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.asset.MachineGenericDto;
import es.snc.mf.common.dto.asset.ProductionLineGenericDto;

public interface IMachineGenericService {

	List<MachineGenericDto> searchAll (String token, String factoryId, String sectionId, String lineId);
	
	MachineGenericDto create (String token, MachineGenericDto dto, String factoryId, String sectionId, String lineId);
	
	MachineGenericDto update (String token, MachineGenericDto dto, String factoryId, String sectionId, String lineId);

}
