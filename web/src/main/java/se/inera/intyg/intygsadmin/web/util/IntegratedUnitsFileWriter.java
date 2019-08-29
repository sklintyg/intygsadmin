package se.inera.intyg.intygsadmin.web.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.inera.intyg.intygsadmin.web.controller.dto.IntegratedUnitDTO;

public class IntegratedUnitsFileWriter {

    public ByteArrayOutputStream writeExcel(List<IntegratedUnitDTO> integratedUnitDTOList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Integrerade enheter");

        int rowCount = 0;

        Row headerRow = sheet.createRow(rowCount++);
        writeHeader(headerRow, workbook);

        for (IntegratedUnitDTO integratedUnitDTO : integratedUnitDTOList) {
            Row row = sheet.createRow(rowCount++);
            writeIntegratedUnit(integratedUnitDTO, row);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream;
        }
    }

    private void writeHeader(Row row, XSSFWorkbook workbook) {
        XSSFCellStyle style = createHeaderStyle(workbook);

        int columnCount = 0;

        Cell cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Enhet");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Enhetsnamn");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Vårdgivar-id");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Vårdgivar namn");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Tillagd");
        cell = row.createCell(columnCount);
        cell.setCellStyle(style);
        cell.setCellValue("Senast kontrollerad");
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void writeIntegratedUnit(IntegratedUnitDTO integratedUnitDTO, Row row) {
        int columnCount = 0;

        Cell cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getEnhetsId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getEnhetsNamn());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getVardgivarId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getVardgivarNamn());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getSkapadDatum() != null ? integratedUnitDTO.getSkapadDatum().toString() : "");
        cell = row.createCell(columnCount);
        cell.setCellValue(integratedUnitDTO.getSenasteKontrollDatum() != null
            ? integratedUnitDTO.getSenasteKontrollDatum().toString() : "");
    }

}
