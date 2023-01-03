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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.inera.intyg.intygsadmin.web.integration.model.PrivatePractitioner;

public class PrivatePractitionerFileWriter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ByteArrayOutputStream writeExcel(List<PrivatePractitioner> privatePractitionerList) throws IOException {
        final var workbook = new XSSFWorkbook();
        final var sheet = workbook.createSheet("Privatläkare");

        int rowCount = 0;

        final var headerRow = sheet.createRow(rowCount++);
        int lastColumnIndex = writeHeader(headerRow, workbook);

        for (PrivatePractitioner privatePractitioner : privatePractitionerList) {
            final var row = sheet.createRow(rowCount++);
            writePrivatePractitioner(privatePractitioner, row);
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
        final var style = createHeaderStyle(workbook);

        int columnCount = 0;

        Cell cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Personnummer");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("HSA-id");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Namn");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Bolagsnamn");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("E-post");
        cell = row.createCell(columnCount++);
        cell.setCellStyle(style);
        cell.setCellValue("Registreringsdatum");

        return columnCount;
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        final var style = workbook.createCellStyle();
        final var font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void writePrivatePractitioner(PrivatePractitioner privatePractitioner, Row row) {
        int columnCount = 0;

        final var registrationDate =
            privatePractitioner.getRegistrationDate() != null ? formatter.format(privatePractitioner.getRegistrationDate()) : "";

        Cell cell = row.createCell(columnCount++);
        cell.setCellValue(privatePractitioner.getPersonId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(privatePractitioner.getHsaId());
        cell = row.createCell(columnCount++);
        cell.setCellValue(privatePractitioner.getName());
        cell = row.createCell(columnCount++);
        cell.setCellValue(privatePractitioner.getCareproviderName());
        cell = row.createCell(columnCount++);
        cell.setCellValue(privatePractitioner.getEmail());
        cell = row.createCell(columnCount++);
        cell.setCellValue(registrationDate);
    }

}
