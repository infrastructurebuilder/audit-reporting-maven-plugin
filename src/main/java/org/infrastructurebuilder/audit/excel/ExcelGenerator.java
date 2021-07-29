/**
 * Copyright © 2019 admin (admin@infrastructurebuilder.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infrastructurebuilder.audit.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.audit.auditor.model.AuditResult;
import org.infrastructurebuilder.audit.auditor.model.AuditorInputSource;
import org.infrastructurebuilder.audit.auditor.model.AuditorResults;
import org.infrastructurebuilder.audit.auditor.model.io.xpp3.AuditorResultsModelXpp3ReaderEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public final class ExcelGenerator {

  private final static Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

  public static void generate(Path input, Path output) throws IOException, XmlPullParserException {

    XSSFWorkbook workbook = new XSSFWorkbook();

    List<AuditorResults> results;
    try (InputStream in = java.nio.file.Files.newInputStream(input)) {
      results = new AuditorResultsModelXpp3ReaderEx().read(in, true, new AuditorInputSource()).getAudits();
    }
    requireNonNull(results);
    log.info("Generating excel file");

    /*
     * sheets
     */
    results.stream().forEach(resultsSet -> {
      String sheetName = resultsSet.getName();
      XSSFSheet sheet = workbook.createSheet(sheetName);

      /*
       * headers
       */
      List<String> headers = new ArrayList<>();
      headers.addAll(resultsSet.getDescriptionHeaders());
      int currentHeaderCellNumber = 0;
      Row headerRow = sheet.createRow(0);
      for (String header : headers) {
        Cell cell = headerRow.createCell(currentHeaderCellNumber++);
        cell.setCellValue(header);
      }

      /*
       * descriptions
       */
      int currentDescriptionRowNumber = 1;
      for (AuditResult result : resultsSet.getResults()) {
        Row descriptionRow = sheet.createRow(currentDescriptionRowNumber);
        int currentDescriptionCellNumber = 0;
        for (String description : result.getDescriptions()) {
          Cell cell = descriptionRow.createCell(currentDescriptionCellNumber);
          cell.setCellValue(description);
          currentDescriptionCellNumber++;
        }
        currentDescriptionRowNumber++;
      }
    });

    try (FileOutputStream out = new FileOutputStream(String.valueOf(output))) {
      log.info("Writing excel file");
      workbook.write(out);
      workbook.close();
      log.info("Excel file written successfully!");
    }
  }
}