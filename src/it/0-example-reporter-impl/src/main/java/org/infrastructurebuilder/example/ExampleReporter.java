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
package org.infrastructurebuilder.example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Named;

import org.infrastructurebuilder.audit.AuditReporter;
import org.infrastructurebuilder.audit.auditor.model.AuditResult;
import org.infrastructurebuilder.audit.auditor.model.AuditorResults;

@Named("ExampleReporter")
public class ExampleReporter implements AuditReporter {

  @Override
  public List<AuditorResults> get() {
    AuditResult k = new AuditResult();
    k.setTimestampStart(new Date());
    k.setTimestampEnd(new Date());
    k.setAuditFailure(false);
    k.setDescriptions(Arrays.asList("Row A data", "Row B data"));
    k.setErrored(false);
    k.setReported(true);
    AuditorResults r = new AuditorResults();
    r.setId(UUID.randomUUID().toString());
    r.setConfidentialityStatement("A statement of confidentiality.");
    r.setDescriptionHeaders(Arrays.asList("Header A", "Header B"));
    r.setIntroduction("Introductory paragraph.");
    r.setName("Example Audit Reporter");
    r.setResults(Arrays.asList(k));
    r.setTimestampStart(new Date());
    r.setTimestampEnd(new Date());

    return Arrays.asList(r);
  }

}
