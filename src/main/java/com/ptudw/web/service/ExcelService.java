package com.ptudw.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {

    public List<List<String>> readExcelFile(MultipartFile file) throws IOException {
        List<List<String>> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming there is only one sheet

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                List<String> rowData = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        // Handle other cell types as needed
                        default:
                            break;
                    }
                }

                rows.add(rowData);
            }
        }

        return rows;
    }
}
