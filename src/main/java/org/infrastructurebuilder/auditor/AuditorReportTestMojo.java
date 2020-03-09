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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "audit", defaultPhase = LifecyclePhase.TEST, requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true, threadSafe = true)
public class AuditorReportTestMojo extends AbstractMojo {
  // context key
  public static final String AUDITOR_RESULT_XML = "AUDITOR_RESULT_XML";

  @Parameter(defaultValue = "${project.build.directory}/", readonly = true)
  private File workDirectory;

  @Parameter(defaultValue = "${project.artifactId}-${project.version}-audits.xml", readonly = true)
  private String outputFile;

  @Component
  private AuditExecutor executor;

  @SuppressWarnings("unchecked")
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    Path path = Paths.get(workDirectory.getPath(), outputFile);
    try {
      executor.execute(path);
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to write the audit results file!");
    }
    getPluginContext().put(AUDITOR_RESULT_XML, path.toString());
  }
}
