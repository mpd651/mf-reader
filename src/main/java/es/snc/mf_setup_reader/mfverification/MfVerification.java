package es.snc.mf_setup_reader.mfverification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.asset.MachineGenericDto;
import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.dto.bom.ConsumableDto;
import es.snc.mf.common.dto.bom.MaterialGenericDto;
import es.snc.mf.common.filter.asset.FactoryFilter;
import es.snc.mf.common.filter.asset.SectionFilter;
import es.snc.mf_setup_reader.dto.MachineGenericAuxDto;
import es.snc.mf_setup_reader.dto.ProductionLineGenericAuxDto;
import es.snc.mf_setup_reader.dto.SectionAuxDto;
import es.snc.mf_setup_reader.mf.IConsumableService;
import es.snc.mf_setup_reader.mf.IFactoryService;
import es.snc.mf_setup_reader.mf.ILineService;
import es.snc.mf_setup_reader.mf.IMachineGenericService;
import es.snc.mf_setup_reader.mf.IMaterialGenericService;
import es.snc.mf_setup_reader.mf.ISectionService;
import es.snc.mf_setup_reader.security.dto.JwtTokenDto;

@Service
public class MfVerification {
	
	@Autowired
	private IMaterialGenericService materialGenericService;
	
	@Autowired
	private IConsumableService consumableService;
	
	@Autowired
	private IFactoryService factoryService;
	
	@Autowired
	private ISectionService sectionService;
	
	@Autowired
	private ILineService lineService;
	
	@Autowired
	private IMachineGenericService machineService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MfVerification.class);
	
	private Map<String, FactoryDto> mapFactoriesCodeAsKey;
	private Map<String, SectionAuxDto> mapSectionCodeAsKey;
	private Map<String, ProductionLineGenericDto> mapLinesCodeAsKey;
	
	
	public void FactoryVerification(JwtTokenDto tokenDto, List<FactoryDto> listFactories) {

		mapFactoriesCodeAsKey=new HashMap();
			
		String token = tokenDto.getToken();

		List<FactoryDto> FactoryMf = factoryService.searchAll(token);

		FactoryDto factoryFound = null;
		boolean found = false;
		
		for (FactoryDto dto : listFactories) {

			for (int i = 0; i < FactoryMf.size() && !found; i++) {

				if (FactoryMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					factoryFound = FactoryMf.get(i);
				}
			}

			if (found) {

				dto.setId(factoryFound.getId());
				dto.setVersionLock(factoryFound.getVersionLock());

				FactoryDto updated = factoryService.update(token, dto);
				
				if (updated == null) {
					LOGGER.error("Factory with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
					
				}else {
					mapFactoriesCodeAsKey.put(updated.getCode(), updated);
				}

			} else {

				FactoryDto created = factoryService.create(token, dto);
				
				if (created == null) {

					LOGGER.error("Factory with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
				}else {
					mapFactoriesCodeAsKey.put(created.getCode(), created);

				}
			}

			found = false; // Restart the boolean
		}
	}
	
	public void SectionVerification (JwtTokenDto tokenDto, List<SectionAuxDto> listSectionsDto) {

		mapSectionCodeAsKey=new HashMap();
		
		String token = tokenDto.getToken();

		SectionDto sectionFound = null;
		boolean found = false;
		
		for (SectionAuxDto dto : listSectionsDto) {
			
			FactoryDto factoryDto=mapFactoriesCodeAsKey.get(dto.getFactoryCode());
			
			dto.setFactoryId(factoryDto.getId());
//			dto.setFactoryCode(null);
			
			List<SectionDto> SectionMf = sectionService.searchAll(token, factoryDto.getId().toString());

			for (int i = 0; i < SectionMf.size() && !found; i++) {

				if (SectionMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					sectionFound = SectionMf.get(i);
				}
			}

			if (found) {

				dto.setId(sectionFound.getId());
				dto.setVersionLock(sectionFound.getVersionLock());

				SectionAuxDto updated = sectionService.update(token, dto, dto.getFactoryId().toString());
				
				if (updated == null) {

					LOGGER.error("Section with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
				}else {
					updated.setFactoryCode(factoryDto.getCode());
					mapSectionCodeAsKey.put(updated.getCode(), updated);

				}

			} else {
				
				SectionDto created = sectionService.create(token, dto, dto.getFactoryId().toString());
				
				if (created == null) {

					LOGGER.error("Section with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
				}else {
					
					mapSectionCodeAsKey.put(created.getCode(), (SectionAuxDto) created);

				}
			}

			found = false; // Restart the boolean
		}
	}


	public void MaterialVerification(JwtTokenDto tokenDto, List<MaterialGenericDto> listMaterialDto) {

		String token = tokenDto.getToken();

		List<MaterialGenericDto> materialGenericMf = materialGenericService.searchAll(token);

		MaterialGenericDto materialGenericFound = null;
		boolean found = false;

		for (MaterialGenericDto dto : listMaterialDto) {

			for (int i = 0; i < materialGenericMf.size() && !found; i++) {

				if (materialGenericMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					materialGenericFound = materialGenericMf.get(i);
				}
			}

			if (found) {

				dto.setId(materialGenericFound.getId());
				dto.setVersionLock(materialGenericFound.getVersionLock());

				MaterialGenericDto updated = materialGenericService.update(token, dto);

				if (updated == null) {

					LOGGER.error("Material type with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
				}

			} else {

				MaterialGenericDto created = materialGenericService.create(token, dto);

				if (created == null) {

					LOGGER.error("Material type with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
				}
			}

			found = false; // Restart the boolean
		}
	}

	public void LinesVerification(JwtTokenDto tokenDto, List<ProductionLineGenericAuxDto> listLines) {

		mapLinesCodeAsKey=new HashMap();
		
		String token = tokenDto.getToken();
		ProductionLineGenericDto lineFound = null;
		boolean found = false;
		
		for (ProductionLineGenericAuxDto dto : listLines) {
			
			SectionDto sectionDto=mapSectionCodeAsKey.get(dto.getSectionCode());
			
			dto.setSectionId(sectionDto.getId());
//			dto.setSectionCode(null);
						
			List<ProductionLineGenericDto> lineListMf = lineService.searchAll(token, sectionDto.getFactoryId().toString(), sectionDto.getId().toString());

			for (int i = 0; i < lineListMf.size() && !found; i++) {

				if (lineListMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					lineFound = lineListMf.get(i);
				}
			}

			if (found) {

				dto.setId(lineFound.getId());
				dto.setVersionLock(lineFound.getVersionLock());

				ProductionLineGenericDto updated = lineService.update(token, dto, sectionDto.getFactoryId().toString(), sectionDto.getId().toString());
				
				if (updated == null) {

					LOGGER.error("Line with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
				}else {
					mapLinesCodeAsKey.put(updated.getCode(), updated);

				}

			} else {

				ProductionLineGenericDto created = lineService.create(token, dto, sectionDto.getFactoryId().toString(), sectionDto.getId().toString());
				
				if (created == null) {

					LOGGER.error("Line with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
				}else {
					mapLinesCodeAsKey.put(created.getCode(), created);

				}
			}

			found = false; // Restart the boolean
		}
	}

	public void MachinesVerification(JwtTokenDto tokenDto, List<MachineGenericAuxDto> listMachines, List<ProductionLineGenericAuxDto> listLines) {
		
		
//		mapLinesCodeAsKey=new HashMap();
		
		String token = tokenDto.getToken();
		MachineGenericDto machineFound = null;
		boolean found = false;
		
		for (MachineGenericAuxDto dto : listMachines) {
			
			ProductionLineGenericDto lineDto=mapLinesCodeAsKey.get(dto.getLineCode());
			ProductionLineGenericAuxDto lineAuxDto=(ProductionLineGenericAuxDto) listLines.stream().filter(l->l.getCode().equals(dto.getLineCode()));
			SectionDto sectionDto=mapSectionCodeAsKey.get(lineAuxDto.getSectionCode());
			
			
			dto.setProductionLineId(lineDto.getId());
//			dto.setLineCode(null);
						
			List<MachineGenericDto> machineListMf = machineService.searchAll(token, sectionDto.getFactoryId().toString(), lineDto.getSectionId().toString(), lineDto.getId().toString());

			for (int i = 0; i < machineListMf.size() && !found; i++) {

				if (machineListMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					machineFound = machineListMf.get(i);
				}
			}

			if (found) {

				dto.setId(machineFound.getId());
				dto.setVersionLock(machineFound.getVersionLock());

				MachineGenericDto updated = machineService.update(token, dto, sectionDto.getFactoryId().toString(), lineDto.getSectionId().toString(), lineDto.getId().toString());
				
				if (updated == null) 
					LOGGER.error("Machine with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
				

			} else {

				MachineGenericDto created = machineService.create(token, dto, sectionDto.getFactoryId().toString(), lineDto.getSectionId().toString(), lineDto.getId().toString());
				
				if (created == null) 
					LOGGER.error("Machine with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
					
			}

			found = false; // Restart the boolean
		}
	}

	public void ConsumableVerification(JwtTokenDto tokenDto, List<ConsumableDto> listConsumable) {

		String token = tokenDto.getToken();

		List<ConsumableDto> consumablesMf = consumableService.searchAll(token);

		ConsumableDto consumableFound = null;
		boolean found = false;

		for (ConsumableDto dto : listConsumable) {

			for (int i = 0; i < consumablesMf.size() && !found; i++) {

				if (consumablesMf.get(i).getCode().equals(dto.getCode())) {

					found = true;
					consumableFound = consumablesMf.get(i);
				}
			}

			if (found) {

				dto.setId(consumableFound.getId());
				dto.setVersionLock(consumableFound.getVersionLock());

				ConsumableDto updated = consumableService.update(token, dto);

				if (updated == null) {

					LOGGER.error("Consumable with code " + dto.getCode().toUpperCase()
							+ " was not updated, something went wrong");
				}

			} else {

				ConsumableDto created = consumableService.create(token, dto);

				if (created == null) {

					LOGGER.error("Consumable with code " + dto.getCode().toUpperCase()
							+ " was not created, something went wrong");
				}
			}

			found = false; // Restart the boolean
		}
	}

}
