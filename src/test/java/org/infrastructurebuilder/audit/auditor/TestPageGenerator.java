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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.audit.auditor.PageGenerator;
import org.infrastructurebuilder.util.config.TestingPathSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPageGenerator {
  private TestingPathSupplier wps = new TestingPathSupplier();
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
  public void testPageGeneration() throws IOException, XmlPullParserException {
    final List<String> results = Arrays.asList("resultA", "resultB", "resultC", "resultD", "resultE", "resultF");

    NoopSink testSink = new NoopSink();
    PageGenerator.generate(path, " ", testSink);

    testSink.getElements().forEach(s -> System.out.println(s));
    assertTrue("Audit failure statement is in text elements", testSink.getElements().contains("Audit Failed"));
    results.stream().forEach(r -> {
      assertTrue(String.format("Results statement %s is in text elements", r), testSink.getElements().contains(r));
    });

    new PageGenerator();

  }
}
