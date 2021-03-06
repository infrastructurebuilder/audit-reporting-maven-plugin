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

import java.io.File;
import java.nio.file.Paths;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true, threadSafe = true)
public class AuditorReportPackageMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project.build.directory}/", readonly = true)
  private File workDirectory;

  @Parameter(defaultValue = "${project.artifactId}-${project.version}-audits.xml", readonly = true)
  private String outputFile;

  @Parameter(property = "project", readonly = true, required = true)
  private MavenProject project;

  @Parameter(property = "classifier")
  private String classifier;

  @Component
  private MavenProjectHelper mavenProjectHelper;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!getPluginContext().containsKey(AuditorReportTestMojo.AUDITOR_RESULT_XML)) {
      throw new MojoExecutionException(
          String.format("Context was missing required key '%s'", AuditorReportTestMojo.AUDITOR_RESULT_XML));
    }
    String resultXmlFile = getPluginContext().get(AuditorReportTestMojo.AUDITOR_RESULT_XML).toString();
    File file = Paths.get(resultXmlFile).toFile();

    if (!file.exists()) {
      throw new MojoExecutionException(String.format("XML result file '%s' did not exist!", file.toString()));
    }

    if (classifier != null) {
      getLog().info(String.format("Classifier was set: %s", classifier));
      mavenProjectHelper.attachArtifact(project, "xml", classifier, file);
    } else {
      if (project.getArtifact().getFile() != null && project.getArtifact().getFile().isFile())
        throw new MojoExecutionException(
            "You have to use a classifier to attach supplemental artifacts to the project instead of replacing them.");
      project.getArtifact().setFile(file);
    }
  }
}
