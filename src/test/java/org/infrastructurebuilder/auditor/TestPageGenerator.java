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
package org.infrastructurebuilder.auditor;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.infrastructurebuilder.auditor.model.AuditResult;
import org.infrastructurebuilder.auditor.model.AuditorResults;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestPageGenerator {
  @Test
  public void testPageGeneration() {
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
        r.setResults(Arrays.asList(k));
        return Arrays.asList(r);
      }
    });
    NoopSink testSink = new NoopSink();
    new PageGenerator(map).generate(" ", testSink);

    testSink.getElements().forEach(s -> System.out.println(s));
    assertTrue("Audit failure statement is in text elements", testSink.getElements().contains("Audit Failed"));
    results.stream().forEach(r -> {
      assertTrue(String.format("Results statement %s is in text elements", r), testSink.getElements().contains(r));
    });

  }
}
