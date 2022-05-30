package es.snc.mf_setup_reader.mf;

import java.util.List;

import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.filter.asset.ProductionLineGenericFilter;
import es.snc.mf.common.filter.asset.SectionFilter;

public interface ILineService {
	ProductionLineGenericDto searchByCode (String token, ProductionLineGenericFilter lineFilter, String factoryId, String sectionId);
	
	List<ProductionLineGenericDto> searchAll (String token, String factoryId, String sectionId);
	
	ProductionLineGenericDto create (String token, ProductionLineGenericDto dto, String factoryId, String sectionId);
	
	ProductionLineGenericDto update (String token, ProductionLineGenericDto dto, String factoryId, String sectionId);

}
