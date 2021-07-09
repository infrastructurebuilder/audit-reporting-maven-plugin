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
package org.infrastructurebuilder.audit.excel;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.infrastructurebuilder.audit.auditor.AuditorReportTestMojo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Mojo(name = "excel", defaultPhase = LifecyclePhase.PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true, threadSafe = true)

public class ExcelGeneratorTestMojo extends AbstractMojo {
  // context key
  public static final String EXCEL_RESULT_XLSX = "EXCEL_RESULT_XLSX";

  @Parameter(defaultValue = "${project.build.directory}/", readonly = true)
  private File workDirectory;

  @Parameter(defaultValue = "${project.artifactId}-${project.version}-excel.xlsx", readonly = true)
  private String outputFile;

  @SuppressWarnings("unchecked")
  @Override
  public void execute() throws MojoExecutionException {
    if (!getPluginContext().containsKey(AuditorReportTestMojo.AUDITOR_RESULT_XML)) {
      throw new MojoExecutionException(
          String.format("Context was missing required key '%s'", AuditorReportTestMojo.AUDITOR_RESULT_XML));
    }
    String resultXmlFile = getPluginContext().get(AuditorReportTestMojo.AUDITOR_RESULT_XML).toString();
    File input = Paths.get(resultXmlFile).toFile();
    Path inputFile = Paths.get(resultXmlFile);

    if (!input.exists()) {
      throw new MojoExecutionException(String.format("XML result file '%s' did not exist!", input.toString()));
    }
    Path output = Paths.get(workDirectory.getPath(), outputFile);
    try {
      ExcelGenerator.generate(inputFile, output);
    } catch (IOException | XmlPullParserException e) {
      throw new MojoExecutionException("Failed to write the excel file!");
    }
    getPluginContext().put(EXCEL_RESULT_XLSX, output.toString());
  }
}