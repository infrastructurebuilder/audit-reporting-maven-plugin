/**
 * Copyright © 2019 admin (admin@infrastructurebuilder.org)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import org.infrastructurebuilder.audit.auditor.model.AuditorInputSource;
import org.infrastructurebuilder.audit.auditor.model.io.xpp3.AuditorResultsModelXpp3ReaderEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public final class ExcelGenerator {
  private final static Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

  public static void generate(Path input, Path output) throws IOException, XmlPullParserException {

    try (InputStream in = java.nio.file.Files.newInputStream(input);
         XSSFWorkbook workbook = new XSSFWorkbook()) {
      log.info("Generating excel file");

      /*
       * sheets
       */
      requireNonNull(new AuditorResultsModelXpp3ReaderEx().read(in, true, new AuditorInputSource()).getAudits()).stream().forEach(resultsSet -> {
        XSSFSheet sheet = workbook.createSheet(resultsSet.getName());

        /*
         * headers
         */
        AtomicInteger currentHeaderCellNumber = new AtomicInteger(0);
        Row headerRow = sheet.createRow(0);
        resultsSet.getDescriptionHeaders().stream().forEach(header -> {
          Cell cell = headerRow.createCell(currentHeaderCellNumber.getAndIncrement());
          cell.setCellValue(header);
        });

        /*
         * descriptions
         */
        AtomicInteger currentDescriptionRowNumber = new AtomicInteger(1);
        resultsSet.getResults().stream().forEach(result -> {
          Row descriptionRow = sheet.createRow(currentDescriptionRowNumber.getAndIncrement());
          AtomicInteger currentDescriptionCellNumber = new AtomicInteger(0);
          result.getDescriptions().stream().forEach(description -> {
            Cell cell = descriptionRow.createCell(currentDescriptionCellNumber.getAndIncrement());
            cell.setCellValue(description);
          });
        });
      });

      try (OutputStream out = Files.newOutputStream(output)) {
        log.info("Writing excel file");
        workbook.write(out);
        log.info("Excel file written successfully!");
      }
    }
  }
}
