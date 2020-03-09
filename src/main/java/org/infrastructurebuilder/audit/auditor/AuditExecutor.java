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

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.infrastructurebuilder.audit.AuditReporter;
import org.infrastructurebuilder.audit.auditor.model.AuditorResults;
import org.infrastructurebuilder.audit.auditor.model.AuditorResultsShell;
import org.infrastructurebuilder.audit.auditor.model.io.xpp3.AuditorResultsModelXpp3Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public final class AuditExecutor {
  private final Map<String, AuditReporter> reporters;
  private final static Logger log = LoggerFactory.getLogger(AuditExecutor.class);

  @Inject
  public AuditExecutor(Map<String, AuditReporter> reporters) {
    requireNonNull(reporters);
    this.reporters = reporters;
    log.info(String.format("audit-reporting-maven-plugin found %d reporters", reporters.size()));
  }

  public void execute(Path file) throws IOException {
    log.info("Executing reports");
    List<AuditorResults> results = reporters.values().parallelStream().map(reporter -> {
      return reporter.get();
    }).flatMap(List::stream).collect(Collectors.toList());

    AuditorResultsShell shell = new AuditorResultsShell();
    shell.setAudits(results);
    log.info(String.format("Writing audit results to %s", file.toString()));

    /*
     * If you are making a single-pom-file with just this execution, the target
     * directory won't exist during this execution.
     */
    if (file.toFile().getParent() != null) {
      new File(file.toFile().getParent()).mkdirs();
    }

    try (final OutputStream out = Files.newOutputStream(file)) {
      new AuditorResultsModelXpp3Writer().write(out, shell);
    }
  }
}
