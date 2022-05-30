package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.bom.MaterialGenericDto;
import es.snc.mf.common.filter.asset.FactoryFilter;

public interface IFactoryService {
	
	FactoryDto searchByCode (String token, FactoryFilter factoryFilter);
	
	List<FactoryDto> searchAll (String token);
	
	FactoryDto create (String token, FactoryDto dto);
	
	FactoryDto update (String token, FactoryDto dto);
}
