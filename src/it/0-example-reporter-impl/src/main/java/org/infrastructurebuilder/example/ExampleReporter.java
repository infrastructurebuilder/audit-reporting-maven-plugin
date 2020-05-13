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
    /* Create the AuditorResults object we will return from the function, and set some basic metadata */
    AuditorResults r = new AuditorResults();
    r.setId(UUID.randomUUID().toString());
    // If your report is confidential, this is where its confidentiality statement can be defined in markdown.
    r.setConfidentialityStatement("## Confidentiality Statement\n A statement of confidentiality.");
    /* An introduction to your audit that will appear before the results in the site. This is a great place to describe
       your audit results and how a reader might interpret them. Format in markdown. */
    r.setIntroduction("Introductory paragraph.");
    // The name of your audit report, as it will appear in the table of contents in the site.
    r.setName("Example Audit Reporter");
    /* Perhaps the most important parts -- your description headers here become table headers in the site.
     * This needs to map exactly to the `AuditResult` you will generate from your tests -- meaning if you have
     * six data points in your AuditResult, you will need six DescriptionHeaders in your AuditorResults. They must
     * be ordered the same.
     *
     * In this example, we are pretending to check servers to see if they are open on a port range, so we will use
     * server name and open ports as our data headers.
     */
    r.setDescriptionHeaders(Arrays.asList("Server", "Open Ports"));
    // Track the start time of the audit
    r.setTimestampStart(new Date());

    /* A more realistic example might use a set of servers to check and ports; we're going to hand-craft our result
     * rather than perform a Real Test.
     *
     * First, create the AuditResult and its start timestamp.
     */
    AuditResult k = new AuditResult();
    k.setTimestampStart(new Date());
    /* here we would normally perform the real checks. Instead, we are crafting this result by hand.
     * Presume we checked a server with name "cool-server", validated connectivity, and confirmed the ports in our
     * 'worry range' were closed.
     *
     * Given those results:
     * - we set audit failure to false (no ports were open in the Bad Range)
     * - we set errored to false (we validated that we could connect to cool-server on its public port, so no false positives
     * - we set 'reported' to true -- this is useful in instances where a result might need to be skipped (e.g. what if you are checking a bunch of SQL servers, but only care about the ones that contain a specific table? You can use setReported(false) to skip reporting on the servers that don't contain the table)
     */
    k.setAuditFailure(false);
    k.setErrored(false);
    k.setReported(true);
    // set the descriptions -- remember these must be ordered in the same way as our AuditResults description headers
    k.setDescriptions(Arrays.asList("cool-server", "none"));
    // Now this check is complete, so we set the ending timestamp
    k.setTimestampEnd(new Date());

    // Now we're completed on all of our checks, so set the end timestamp for the audit...
    r.setTimestampEnd(new Date());
    // Gather all the `AuditResult`s (yes, all one of them in this example) into a list.
    r.setResults(Arrays.asList(k));
    // and return our result, wrapped in a list. If we had multiple AuditResults as part of this audit, we could include them as well.
    return Arrays.asList(r);
  }

}
