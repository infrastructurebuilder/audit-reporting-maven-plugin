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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.maven.doxia.sink.Sink;
import org.infrastructurebuilder.auditor.model.AuditResult;
import org.infrastructurebuilder.auditor.model.AuditorResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public final class PageGenerator {
  private final Map<String, AuditReporter> reporters;
  private final static Logger log = LoggerFactory.getLogger(PageGenerator.class);

  @Inject
  public PageGenerator(Map<String, AuditReporter> reporters) {
    this.reporters = reporters;
    log.info(String.format("audit-reporting-maven-plugin found %d reporters", reporters.size()));
  }

  public void generate(String title, Sink sink) {
    Objects.requireNonNull(sink);
    Objects.requireNonNull(title);

    log.info("Executing reports");
    List<AuditorResults> results = reporters.values().parallelStream().map(reporter -> {
      return reporter.get();
    }).flatMap(List::stream).collect(Collectors.toList());

    log.info("Generating page head and table of contents");
    sink.head();
    sink.title();
    sink.text(title);
    sink.title_();
    sink.head_();

    sink.body();
    sink.section1();
    sink.text("Table of Contents");
    sink.section1_();

    results.parallelStream().forEach(result -> {
      /*
       * prepend "id-" to result.getId() because UUIDs can start with numbers, but
       * HTML anchors can't
       */
      sink.link(String.format("#id-%s", result.getId()));
      sink.text(result.getName());
      sink.link_();
    });

    log.info(String.format("Beginning report sections generation (%d reporters found)", reporters.size()));
    results.parallelStream().forEach(result -> {
      log.info(String.format("Found audit result named %s", result.getName()));
      createSection(result, sink);
    });
    sink.body_();
    log.info("Generation complete");
  }

  private void createSection(AuditorResults results, Sink sink) {
    sink.anchor(String.format("id-%s", results.getId()));
    sink.anchor_();
    sink.section1();

    sink.sectionTitle1();
    sink.text(results.getName());
    sink.sectionTitle1_();

    sink.paragraph();
    sink.text(results.getConfidentialityStatement());
    sink.paragraph_();

    sink.paragraph();
    sink.text(results.getIntroduction());
    sink.paragraph_();

    resultMetrics(results.getDescriptionHeaders(), results.getResults(), sink);

    sink.section1_();
    sink.body_();
  }

  private void resultMetrics(List<String> tableHeaders, List<AuditResult> results, Sink sink) {
    results = results.parallelStream().filter(r -> r.isReported()).collect(Collectors.toList());
    long totalFailures = results.parallelStream().filter(r -> r.isAuditFailure() && !r.isErrored()).count();
    long totalErrors = results.parallelStream().filter(r -> r.isErrored()).count();
    boolean passedAudit = totalFailures == 0 && totalErrors == 0;

    sink.paragraph();
    if (passedAudit) {
      sink.text("Audit Passed");
    } else {
      sink.text("Audit Failed");
    }
    sink.paragraph_();

    sink.table();

    sink.tableRow();
    tableHeaders.stream().forEach(header -> {
      sink.tableHeaderCell();
      sink.text(header);
      sink.tableHeaderCell_();
    });
    sink.tableRow_();

    results.stream().forEach(r -> {
      sink.tableRow();
      r.getDescriptions().stream().forEach(description -> {
        sink.tableCell();
        sink.text(description);
        sink.tableCell_();
      });
      sink.tableRow_();
    });
    sink.table_();
  }
}
