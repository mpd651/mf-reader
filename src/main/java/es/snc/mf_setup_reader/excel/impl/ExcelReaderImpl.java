package es.snc.mf_setup_reader.excel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import es.snc.mf.common.dto.asset.FactoryDto;
import es.snc.mf.common.dto.asset.MachineGenericDto;
import es.snc.mf.common.dto.asset.ProductionLineGenericDto;
import es.snc.mf.common.dto.asset.SectionDto;
import es.snc.mf.common.dto.bom.ConsumableDto;
import es.snc.mf.common.dto.bom.MaterialDto;
import es.snc.mf.common.dto.bom.MaterialGenericDto;
import es.snc.mf_setup_reader.dto.MachineGenericAuxDto;
import es.snc.mf_setup_reader.dto.ProductionLineGenericAuxDto;
import es.snc.mf_setup_reader.dto.SectionAuxDto;
import es.snc.mf_setup_reader.excel.IExcelReader;
import es.snc.mf_setup_reader.mfverification.MfVerification;
import es.snc.mf_setup_reader.security.dto.JwtTokenDto;
import es.snc.mf_setup_reader.utils.ExcelInfo;
import es.snc.mf_setup_reader.utils.FindSetters;

@Service
@PropertySource({ "classpath:application.properties" })
public class ExcelReaderImpl implements IExcelReader {

	private @Value("${client.id}") String clientId;
	private @Value("${excelAbsolutePath}") String fileAbsolutePath;

	private final int MAX_EMPTY_ROWS = 50;
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReaderImpl.class);

	private JwtTokenDto token;

	@Autowired
	private MfVerification verification;

	@Autowired
	private FindSetters findSetters;

	private boolean factoriesSheetWithoutMistakes;
	private boolean sectionSheetWithoutMistakes;
	private boolean linesSheetWithoutMistakes;
	private boolean machinesSheetWithoutMistakes;
	private boolean materialSheetWithoutMistakes;
	private boolean consumableSheetWithoutMistakes;

	private List<FactoryDto> listFactories;
	private List<SectionAuxDto> listSections;
	private List<ProductionLineGenericAuxDto> listLines;
	private List<MachineGenericAuxDto> listMachines;
	private List<MaterialGenericDto> listMaterial;
	private List<ConsumableDto> listConsumable;

	@Override
	public void readExcelSheets(JwtTokenDto token) throws FileNotFoundException, IOException {

		this.token = token;

		FileInputStream fis = new FileInputStream(new File(fileAbsolutePath));

		XSSFWorkbook wb = new XSSFWorkbook(fis);

		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			XSSFSheet sheet = wb.getSheetAt(i);

			switch (sheet.getSheetName()) {

			case "Fábrica":
				readFactories(sheet);
				break;

			case "Seccion":
				readSection(sheet);
				break;

			case "Lineas":
				readLines(sheet);
				break;

			case "Maquinas":
				readMachines(sheet);
				break;

			case "Materiales":
				readMaterials(sheet);
				break;

			case "Consumibles":
				readConsumables(sheet);
				break;

			default:
				LOGGER.error("Unknown sheet: " + sheet.getSheetName());
				break;

			}

		}

		if (factoriesSheetWithoutMistakes && sectionSheetWithoutMistakes && linesSheetWithoutMistakes
				&& machinesSheetWithoutMistakes && materialSheetWithoutMistakes && consumableSheetWithoutMistakes) {
			verification.FactoryVerification(token, listFactories);
			verification.SectionVerification(token, listSections);
			verification.LinesVerification(token, listLines);
			verification.MachinesVerification(token, listMachines, listLines);
			verification.MaterialVerification(token, listMaterial);
			verification.ConsumableVerification(token, listConsumable);
		} else
			LOGGER.error("Wrong data, cannot upload excel");

		wb.close();

		fis.close();

	}

	public void readFactories(XSSFSheet sheet) {

		factoriesSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listFactories = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1 && !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null)) {

				FactoryDto dto = new FactoryDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.factoryDtoSetters(dto, cell, firstRow.get(i));

				}

				if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getName())) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						factoriesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						factoriesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (FactoryDto f : listFactories) {

						if (f.getCode().equals(dto.getCode())) {
							factoriesSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listFactories.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}

				lineReaded++;
			}

			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!factoriesSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());
	}

	public void readSection(XSSFSheet sheet) {

		sectionSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listSections = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1 && !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null
					&& row.getCell(3) == null)) {

				SectionAuxDto dto = new SectionAuxDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.sectionDtoSetters(dto, cell, firstRow.get(i));

				}

				if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getName())
						|| StringUtils.isBlank(dto.getFactoryCode())) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						sectionSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						sectionSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (SectionAuxDto s : listSections) {

						if (s.getCode().equals(dto.getCode())) {
							sectionSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listSections.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}
				lineReaded++;

			}
			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!sectionSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());

	}

	public void readLines(XSSFSheet sheet) {

		linesSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listLines = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1 && !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null
					&& row.getCell(3) == null)) {

				ProductionLineGenericAuxDto dto = new ProductionLineGenericAuxDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.lineDtoSetters(dto, cell, firstRow.get(i));

				}

				if (StringUtils.isBlank(dto.getSectionCode()) || StringUtils.isBlank(dto.getName())
						|| StringUtils.isBlank(dto.getCode())) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						linesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						linesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (ProductionLineGenericAuxDto l : listLines) {

						if (l.getCode().equals(dto.getCode())) {
							linesSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listLines.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}
				lineReaded++;

			}
			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!linesSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());

	}

	public void readMachines(XSSFSheet sheet) {

		machinesSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listMachines = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {

			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1 && !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null
					&& row.getCell(3) == null && row.getCell(4) == null && row.getCell(5) == null
					&& row.getCell(6) == null && row.getCell(7) == null && row.getCell(8) == null
					&& row.getCell(9) == null && row.getCell(10) == null && row.getCell(11) == null
					&& row.getCell(12) == null && row.getCell(13) == null && row.getCell(14) == null
					&& row.getCell(15) == null && row.getCell(16) == null && row.getCell(17) == null
					&& row.getCell(18) == null && row.getCell(19) == null && row.getCell(20) == null)) {

				MachineGenericAuxDto dto = new MachineGenericAuxDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.machineDtoSetters(dto, cell, firstRow.get(i), line);

				}

				if (StringUtils.isBlank(dto.getLineCode()) || StringUtils.isBlank(dto.getCode())
						|| StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getManufacturer())
						|| StringUtils.isBlank(dto.getBrand()) || StringUtils.isBlank(dto.getModel())
						|| StringUtils.isBlank(dto.getSerialNumber()) || dto.getAssignable() == null) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						machinesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						machinesSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (MachineGenericAuxDto m : listMachines) {

						if (m.getCode().equals(dto.getCode())) {
							machinesSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listMachines.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}
				lineReaded++;
			}

			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!machinesSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());

	}

	public void readMaterials(XSSFSheet sheet) {

		materialSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listMaterial = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1
					&& !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null
							&& row.getCell(3) == null && row.getCell(4) == null && row.getCell(5) == null
							&& row.getCell(6) == null)
					&& row.getCell(7) == null && row.getCell(8) == null && row.getCell(9) == null) {

				MaterialGenericDto dto = new MaterialGenericDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.materialDtoSetters(dto, cell, firstRow.get(i), line);

				}

				if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getName())) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						materialSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						materialSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (MaterialGenericDto mg : listMaterial) {

						if (mg.getCode().equals(dto.getCode())) {
							materialSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listMaterial.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}
				lineReaded++;
			}
			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!materialSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());

	}

	public void readConsumables(XSSFSheet sheet) {

		consumableSheetWithoutMistakes = true;
		List<XSSFCell> firstRow = ExcelInfo.getFirstRow(sheet);
		int cells = ExcelInfo.getNumberOfColumns(sheet);

		listConsumable = new ArrayList<>();

		int emptyAcumulated = 0;
		int line = 0;
		int lineReaded = 0;

		Iterator<Row> rowIterator = sheet.iterator();

		while (rowIterator.hasNext()) {
			XSSFRow row = (XSSFRow) rowIterator.next();

			if (line > 1 && !(row.getCell(0) == null && row.getCell(1) == null && row.getCell(2) == null
					&& row.getCell(3) == null && row.getCell(4) == null && row.getCell(5) == null
					&& row.getCell(6) == null)) {

				ConsumableDto dto = new ConsumableDto();

				dto.setClientId(Long.valueOf(clientId));

				for (int i = 0; i < cells; i++) {

					XSSFCell cell = row.getCell(i);

					dto = findSetters.consumableDtoSetters(dto, cell, firstRow.get(i), line);

				}

				if (StringUtils.isBlank(dto.getCode()) || StringUtils.isBlank(dto.getName())) {

					emptyAcumulated++;

					if (emptyAcumulated == 1) {
						consumableSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". From row: "
								+ (line + 1));
					}
				} else {

					if (emptyAcumulated != 0) {
						consumableSheetWithoutMistakes = false;
						LOGGER.error("Error, required fields are empty. Sheet: " + sheet.getSheetName() + ". To row: "
								+ line);
					}
					emptyAcumulated = 0;

					boolean duplicated = false;

					for (ConsumableDto c : listConsumable) {

						if (c.getCode().equals(dto.getCode())) {
							consumableSheetWithoutMistakes = false;
							duplicated = true;
							LOGGER.info("Required fields cannot be duplicated. Sheet: " + sheet.getSheetName()
									+ " (Error in line " + line + ")");
							break;
						}
					}

					if (!duplicated) {
						listConsumable.add(dto);
					}
				}

				if (emptyAcumulated == MAX_EMPTY_ROWS) {
					LOGGER.error("End of sheet: " + sheet.getSheetName() + ". Row: " + ((line + 1) - MAX_EMPTY_ROWS));
					break;
				}
				lineReaded++;

			}
			line++;
		}

		LOGGER.info("Rows readed from sheet " + sheet.getSheetName() + ": " + (lineReaded - 1));

		if (!consumableSheetWithoutMistakes)
			LOGGER.error("Wrong data, cannot upload " + sheet.getSheetName());

	}

}
