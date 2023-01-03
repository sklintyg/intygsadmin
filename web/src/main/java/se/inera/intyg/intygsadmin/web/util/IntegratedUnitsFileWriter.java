/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsadmin.web.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.inera.intyg.infra.integreradeenheter.IntegratedUnitDTO;

public class IntegratedUnitsFileWriter {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ByteArrayOutputStream writeExcel(List<IntegratedUnitDTO> integratedUnitDTOList) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Integrerade enheter");

        int rowCount = 0;

        Row headerRow = sheet.createRow(rowCount++);
        int lastColumnIndex = writeHeader(headerRow, workbook);

        for (IntegratedUnitDTO integratedUnitDTO : integratedUnitDTOList) {
            Row row = sheet.createRow(rowCount++);
            writeIntegratedUnit(integratedUnitDTO, row);
        }

        for (int a = 0; a <= lastColumnIndex; a++) {
            sheet.autoSizeColumn(a);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream;
        }
    }

    private int writeHeader(Row row, XSSFWorkbook workbook) {
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

        return columnCount;
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

        final String skapadDatum = integratedUnitDTO.getSkapadDatum() != null ? formatter.format(integratedUnitDTO.getSkapadDatum()) : "";
        final String kontrollDatum = integratedUnitDTO.getSenasteKontrollDatum() != null
            ? formatter.format(integratedUnitDTO.getSenasteKontrollDatum()) : "";

        Cell cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getEnhetsId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getEnhetsNamn());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getVardgivarId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(integratedUnitDTO.getVardgivarNamn());
        cell = row.createCell(columnCount++);
        cell.setCellValue(skapadDatum);
        cell = row.createCell(columnCount);
        cell.setCellValue(kontrollDatum);
    }

}
