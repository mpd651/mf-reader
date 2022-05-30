package es.snc.mf_setup_reader.excel;

import java.io.FileNotFoundException;
import java.io.IOException;

import es.snc.mf_setup_reader.security.dto.JwtTokenDto;

public interface IExcelReader {
	
	public void readExcelSheets(JwtTokenDto token) throws FileNotFoundException, IOException;

}
