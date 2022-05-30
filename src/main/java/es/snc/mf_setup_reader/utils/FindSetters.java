package es.snc.mf_setup_reader.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.dto.bom.ConsumableDto;
import es.snc.mf.common.dto.bom.MaterialDto;
import es.snc.mf.common.dto.bom.MaterialGenericDto;
import es.snc.mf.common.filter.asset.FactoryFilter;
import es.snc.mf.common.type.ConsumableType;
import es.snc.mf_setup_reader.dto.MachineGenericAuxDto;
import es.snc.mf_setup_reader.dto.ProductionLineGenericAuxDto;
import es.snc.mf_setup_reader.dto.SectionAuxDto;
import es.snc.mf_setup_reader.mf.IFactoryService;
import es.snc.mf_setup_reader.security.dto.JwtTokenDto;


@Service
public class FindSetters {

	@Autowired
	private IFactoryService factoryService;

	private static final Logger LOGGER = LoggerFactory.getLogger(FindSetters.class);

	public static String valueDateOrStringCells(XSSFCell cell) {

		String stringValue = null;

		if (cell.getCellType().equals(CellType.NUMERIC)) {

			LocalDateTime date = cell.getLocalDateTimeCellValue();
			
			if (date != null) {

				stringValue = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
			}
			
		} else if (cell.getCellType().equals(CellType.STRING)) {

			String dateString = cell.getStringCellValue();
			
			LocalDate date = null;
			
			if (dateString.contains("-")) {
				
				date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			} else if (dateString.contains("/")) {
				
				date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			}
			
			stringValue = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}

		return stringValue;
	}
	
	public SectionAuxDto sectionDtoSetters(SectionAuxDto dto, XSSFCell cell, XSSFCell cellFirstRow) {
		if (cell != null) {
			
			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Fábrica de referencia*":
				String factoryCode = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					factoryCode = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					factoryCode = cell.getStringCellValue().trim();
				}

				dto.setFactoryCode(factoryCode);
				break;

			case "Nombre*":
				String name= "";
				
				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;
				
				
				
			case "Código*":
				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;
				
			case "Descripción":
				String description= "";
				
				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;
				
				
			}
			
		}

		return dto;
	}
	
	public FactoryDto factoryDtoSetters(FactoryDto dto, XSSFCell cell, XSSFCell cellFirstRow) {

		if (cell != null) {
			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Nombre*":
				String name = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;


			case "Código*":
				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;

			case "Descripción":
				String description = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;

			}
		}

		return dto;
	}
	
	public MaterialGenericDto materialDtoSetters(MaterialGenericDto dto, XSSFCell cell, XSSFCell cellFirstRow, int line) {

		if (cell != null) {

			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Código*":

				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;

			case "Nombre*":
				String name = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;

			case "Descripción":
				String description = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;

			case "Código externo":
				String externalCode = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					externalCode = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					externalCode = cell.getStringCellValue().trim();
				}

				dto.setExternalCode(externalCode);
				break;

			case "Observaciones":
				String observations = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					observations = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					observations = cell.getStringCellValue().trim();
				}

				dto.setObservations(observations);
				break;

			case "Virtual":
				if (!cell.getCellType().equals(CellType.STRING)) {
					LOGGER.error("Error. Need a string value in cell 'Virtual'. Line "+ line);
					break;
				}
				
				boolean virtual = (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setIsVirtual(virtual);
				break;

			case "Materia prima":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Materia prima'. Line "+ line);
					break;
				}
				
				boolean rawMaterial= (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setIsRawMaterial(rawMaterial);
				break;

			case "Semi-terminado":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Semi-terminado'. Line "+ line);
					break;
				}
				
				boolean halfFinished= (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setIsSemifinished(halfFinished);
				break;
				
			case "Terminado":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Terminado'. Line "+ line);
					break;
				}
				
				boolean finished = (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setIsFinished(finished);
				break;
			
			case "Creado":
				dto.setCreatedAt(valueDateOrStringCells(cell));
				break;
			}
		}

		return dto;
	}

	public ProductionLineGenericAuxDto lineDtoSetters(ProductionLineGenericAuxDto dto, XSSFCell cell,
			XSSFCell cellFirstRow) {
		
		if (cell != null) {
			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Seccion De Referencia*":				
				String sectionCode = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					sectionCode = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					sectionCode = cell.getStringCellValue().trim();
				}

				dto.setSectionCode(sectionCode);
				break;

			case "Nombre*":
				String name = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;
				
			case "Código*":
				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;
				
			case "Descripción":
				String description = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;
				
			}
			
			
		}

		return dto;
	}

	public MachineGenericAuxDto machineDtoSetters(MachineGenericAuxDto dto, XSSFCell cell, XSSFCell cellFirstRow, int line) {
		
		if (cell != null) {
			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Linea De Referencia*":
				String lineCode = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					lineCode = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					lineCode = cell.getStringCellValue().trim();
				}

				dto.setLineCode(lineCode);
				break;
				
			case "Código*":
				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;
				
			case "Nombre*":
				String name = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;
				
			case "Descripción":
				String description = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;
				
			case "Fabricante*":
				String manufacturer = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					manufacturer = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					manufacturer = cell.getStringCellValue().trim();
				}

				dto.setManufacturer(manufacturer);
				break;
				
			case "Marca*":
				String brand = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					brand = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					brand = cell.getStringCellValue().trim();
				}

				dto.setBrand(brand);
				break;
				
			case "Modelo*":
				String model = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					model = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					model = cell.getStringCellValue().trim();
				}

				dto.setModel(model);
				break;
				
			case "Número de serie*":
				String serialNumber = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					serialNumber = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					serialNumber = cell.getStringCellValue().trim();
				}

				dto.setSerialNumber(serialNumber);
				break;
				
			case "Coste por defecto":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Coste por defecto'. Line "+ line);
					break;
				}
				
				BigDecimal defaultCost= BigDecimal.valueOf(cell.getNumericCellValue());
				dto.setDefaultCost(defaultCost);
				break;
				
			case "Último mantenimiento":
				dto.setMaintenanceAt(valueDateOrStringCells(cell));
				break;
				
			case "Última reparacion":
				dto.setRepairedAt(valueDateOrStringCells(cell));
				break;
				
			case "Recibido":
				dto.setReceivedAt(valueDateOrStringCells(cell));
				break;
				
			case "Calibrado":
				dto.setCalibratedAt(valueDateOrStringCells(cell));
				break;
				
			case "Homologado":
				dto.setHomologatedAt(valueDateOrStringCells(cell));
				break;
				
			case "Longitud":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Longitud'. Line "+ line);
					break;
				}
				
				dto.setLength((int)cell.getNumericCellValue());
				break;
				
			case "Altura":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Altura'. Line "+ line);
					break;
				}
				
				dto.setHeight((int)cell.getNumericCellValue());
				break;
			
			case "Anchura":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Anchura'. Line "+ line);
					break;
				}
				
				dto.setWidth((int)cell.getNumericCellValue());
				break;
				
			case "Consumo de energia":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Consumo de energia'. Line "+ line);
					break;
				}
				
				dto.setEnergyConsumption((int)cell.getNumericCellValue());
				break;
				
			case "Energia":
				if (!cell.getCellType().equals(CellType.NUMERIC)){
					LOGGER.error("Error. Need a numeric value in cell 'Energia'. Line "+ line);
					break;
				}
				
				dto.setPower((int)cell.getNumericCellValue());
				break;
				
			case "Comentarios":
				String comments = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					comments = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					comments = cell.getStringCellValue().trim();
				}

				dto.setComments(comments);
				break;
				
			case "Asignable*":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Asignable*'. Line "+ line);
					break;
				}
				
				boolean assignable = (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setAssignable(assignable);
				break;
			}
			
		}

		return dto;
	}

	public ConsumableDto consumableDtoSetters(ConsumableDto dto, XSSFCell cell, XSSFCell cellFirstRow, int line) {

		if (cell != null) {

			switch (cellFirstRow.getStringCellValue().trim()) {

			case "Código*":

				String code = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					code = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					code = cell.getStringCellValue().trim();
				}

				dto.setCode(code);
				break;

			case "Nombre*":
				String name = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					name = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					name = cell.getStringCellValue().trim();
				}

				dto.setName(name);
				break;

			case "Descripción":
				String description = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					description = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					description = cell.getStringCellValue().trim();
				}

				dto.setDescription(description);
				break;

			case "Código externo":
				String externalCode = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					externalCode = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					externalCode = cell.getStringCellValue().trim();
				}

				dto.setCode(externalCode);
				break;

			case "Observaciones":
				String observations = "";

				if (cell.getCellType().equals(CellType.NUMERIC)) {
					observations = String.valueOf((int) cell.getNumericCellValue()).trim();

				} else if (cell.getCellType().equals(CellType.STRING)) {
					observations = cell.getStringCellValue().trim();
				}

				dto.setObservations(observations);
				break;

			case "Virtual":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Virtual'. Line "+ line);
					break;
				}
				
				boolean virtual = (cell.getStringCellValue().equalsIgnoreCase("Si")) ? true : false;
				dto.setIsVirtual(virtual);
				break;

			case "Tipo":
				if (!cell.getCellType().equals(CellType.STRING)){
					LOGGER.error("Error. Need a string value in cell 'Tipo'. Line "+ line);
					break;
				}
				
				ConsumableType type= (cell.getStringCellValue().equalsIgnoreCase("Recambio")) ? ConsumableType.SPARE_PART : ConsumableType.CONSUMABLE_GENERIC;
				dto.setType(type);
				break;
			}
		}

		return dto;
	}
}
