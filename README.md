# audit-reporting-maven-plugin
Plugin to execute a series of one or more audits, generate a result artifact, and report the results via the Maven site.

## Examples

Always up-to-date examples can be found underneath the integration test directory `src/it`.

## Walkthrough

To create a fresh set of audits, it is recommended to use more than one project. You can write any number of audit implementations; we recommend storing each implementation in separate projects but you may keep them together if desired. Finally, you will write a project to execute the audits and generate the result artifact and site.

We'll start by writing our audit implementation. First, we'll need to make sure we have the following dependencies, at minimum:

Auditor pom.xml
```
<dependency>
  <groupId>org.infrastructurebuilder.audit</groupId>
  <artifactId>auditor-api</artifactId>
  <version>0.3.0</version>
</dependency>
<dependency>
  <groupId>org.eclipse.sisu</groupId>
  <artifactId>org.eclipse.sisu.inject</artifactId>
  <version>0.3.4</version>
</dependency>
<dependency>
  <groupId>javax.inject</groupId>
  <artifactId>javax.inject</artifactId>
  <version>1</version>
</dependency>
```

Now, we need to write a class that satisfies the following requirements:

- the class must be annotated with @Named for javax and sisu injection
- the class must implement the AuditReporter interface defined in the auditor-api

This is easier than it sounds. The AuditReporter interface requires only one method with signature `public List<AuditorResults> get()`.

A full example of implementing this class and creating the auditor result, with comments to indicate the 'why' behind each step:

```
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
```

The example shown is the shortest-path implementation of the minimum requirements to satisfy the API. In a more complex
example, you might write a series of tests that each returned an `AuditResult` and then merely use the `get()` method
to execute that series of test and add them to the AuditorResults object.

Now, let's go to the execution.

The recommended means of creating an execution package is to perform the following steps:

- Create a separate project
- Set version and dependencies for audit-reporting-maven-plugin in the pluginManagement section
- Add the audit reporting plugin to the build plugins
- Add the maven site plugin to the build plugins
- Add the plugin to the reporting section

Here's a full example pom (copied over from the integration tests, so hence some oddities in its presentation like @project.version@:

Execution pom.xml
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.infrastructurebuilder.example</groupId>
  <artifactId>example-audit-execution</artifactId>
  <version>@project.version@</version>

  <properties>
    <test.coverage.percentage.required>00</test.coverage.percentage.required>
  </properties>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.infrastructurebuilder</groupId>
          <artifactId>audit-reporting-maven-plugin</artifactId>
          <version>@project.version@</version>
          <dependencies>
            <dependency>
              <groupId>org.infrastructurebuilder.example</groupId>
              <artifactId>example-reporter-impl</artifactId>
              <version>@project.version@</version>
            </dependency>
          </dependencies>
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.infrastructurebuilder</groupId>
        <artifactId>audit-reporting-maven-plugin</artifactId>
        <configuration>
          <classifier>audit</classifier>
        </configuration>
        <executions>
          <execution>
            <id>execute-audits</id>
            <goals>
              <goal>audit</goal>
              <goal>package</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-site-plugin</artifactId>
        <version>3.8.2</version>
        <executions>
          <execution>
            <id>report</id>
            <phase>package</phase>
            <goals>
              <goal>site</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.infrastructurebuilder</groupId>
        <artifactId>audit-reporting-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </reporting>
</project>
```

And, that's it! Installing (or deploying) your audits, and then running `mvn clean verify` on your execution project
will execute your audits. The final result in your target/ directory will include the site page audits.html, and 
the raw XML data from the results.
