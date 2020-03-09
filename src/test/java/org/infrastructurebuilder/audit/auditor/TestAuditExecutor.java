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
package org.infrastructurebuilder.audit.auditor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.audit.AuditReporter;
import org.infrastructurebuilder.audit.auditor.AuditExecutor;
import org.infrastructurebuilder.audit.auditor.model.AuditResult;
import org.infrastructurebuilder.audit.auditor.model.AuditorInputSource;
import org.infrastructurebuilder.audit.auditor.model.AuditorResults;
import org.infrastructurebuilder.audit.auditor.model.AuditorResultsShell;
import org.infrastructurebuilder.audit.auditor.model.io.xpp3.AuditorResultsModelXpp3ReaderEx;
import org.infrastructurebuilder.util.config.WorkingPathSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAuditExecutor {
  private WorkingPathSupplier wps = new WorkingPathSupplier();
  private Path path;

  @Before
  public void setUp() throws Exception {
    path = wps.get().resolve("testWritten.xml");
  }

  @After
  public void teardown() {
    wps.finalize();
  }

  @Test
  public void testPageGeneration() throws IOException, XmlPullParserException {
    Map<String, AuditReporter> map = new HashMap<>();
    final List<String> results = Arrays.asList("resultA", "resultB", "resultC");
    map.put("Test Audit Reporter", new AuditReporter() {

      @Override
      public List<AuditorResults> get() {
        AuditResult k = new AuditResult();
        k.setTimestampEnd(new Date());
        k.setTimestampStart(new Date());
        k.setDescriptions(results);
        k.setReported(true);
        k.setAuditFailure(true);
        k.setErrored(false);
        AuditorResults r = new AuditorResults();
        r.setDescriptionHeaders(Arrays.asList("A", "B"));
        r.setTimestampStart(new Date());
        r.setTimestampEnd(new Date());
        r.setIntroduction("intro");
        r.setConfidentialityStatement("confidential!");
        r.setId(UUID.randomUUID().toString());
        r.setName("Auditor Results");
        r.setResults(Arrays.asList(k, k));
        return Arrays.asList(r);
      }
    });
    new AuditExecutor(map).execute(path);
    assertTrue("File should exist", path.toFile().exists());
    AuditorResultsShell writtenResults = new AuditorResultsModelXpp3ReaderEx().read(Files.newInputStream(path), true,
        new AuditorInputSource());
    assertEquals("Should have one audit", 1, writtenResults.getAudits().size());
    assertEquals("Should have two audit results", 2, writtenResults.getAudits().get(0).getResults().size());
  }
}