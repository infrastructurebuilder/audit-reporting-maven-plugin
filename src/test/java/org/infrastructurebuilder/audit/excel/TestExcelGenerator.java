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
import org.infrastructurebuilder.audit.auditor.model.AuditorInputSource;
import org.infrastructurebuilder.audit.auditor.model.AuditorResults;
import org.infrastructurebuilder.audit.auditor.model.io.xpp3.AuditorResultsModelXpp3ReaderEx;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestExcelGenerator {
  private final TestingPathSupplier wps = new TestingPathSupplier();
  private Path path;

  @Before
  public void setUp() throws Exception {
    path = wps.getTestClasses().resolve("testFile.xml");
  }

  @After
  public void teardown() {
    wps.finalize();
  }

  @Test
  public void testExcelGeneration() throws IOException, XmlPullParserException {
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      final List<AuditorResults> results;
      Path input = path;
      Path output = Paths.get("target/testAuditsExcelFile.xlsx");

      try (InputStream in = java.nio.file.Files.newInputStream(path)) {
        results = new AuditorResultsModelXpp3ReaderEx().read(in, true, new AuditorInputSource()).getAudits();
      }
      ExcelGenerator.generate(input, output);

      results.stream().forEach(result -> {
        String sheetName = result.getName();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        assertNotNull("results exist", results);
        assertNotNull("names exist", result.getName());
        assertNotNull("headers exist", result.getDescriptionHeaders());
        assertEquals("sheetName should be Testing Audit", "Testing Audit", "Testing Audit");

        List<String> eHeaders = new ArrayList<>(result.getDescriptionHeaders());
        assertNotNull("eHeaders exist", eHeaders);
        int headerCellNum = 0;
        Row headerRow = sheet.createRow(0);
        for (String eHeader : eHeaders) {
          Cell cell = headerRow.createCell(headerCellNum++);
          cell.setCellValue(eHeader);
          assertNotNull("cell exists", cell);
        }
      });
      try (FileOutputStream out = new FileOutputStream(String.valueOf(output))) {
        workbook.write(out);
        assertNotNull("excel file exists", out);
      } catch (IOException e) {
        fail("Threw an exception while attempting to write the workbook!");
        e.printStackTrace();
      }
    } catch (IOException e) {
      fail("Threw an exception while attempting to write the workbook!");
      e.printStackTrace();
    }
  }
}

