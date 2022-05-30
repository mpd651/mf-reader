package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.filter.asset.FactoryFilter;
import es.snc.mf.common.filter.asset.SectionFilter;
import es.snc.mf_setup_reader.dto.SectionAuxDto;

public interface ISectionService {
	
	SectionDto searchByCode (String token, SectionFilter sectionFilter, String factoryId);
	
	List<SectionDto> searchAll (String token, String factoryId);
	
	SectionDto create (String token, SectionDto dto, String factoryId);
	
	SectionAuxDto update (String token, SectionDto dto, String factoryId);
	
	SectionDto searchById(String token, String factoryId, String id);
}
