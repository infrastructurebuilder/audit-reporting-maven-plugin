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

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.infrastructurebuilder.audit.auditor.AuditorReportPackageMojo;
import org.infrastructurebuilder.audit.auditor.AuditorReportTestMojo;
import org.junit.Test;

public class TestAuditorReportPackageMojo {
  @Test
  public void testConstructor() {
    new AuditorReportPackageMojo();
  }

  @Test(expected = MojoExecutionException.class)
  public void testNoSuchFileException() throws MojoExecutionException, MojoFailureException {
    AuditorReportPackageMojo mojo = new AuditorReportPackageMojo();
    Map<String, String> map = new HashMap<String, String>();
    map.put(AuditorReportTestMojo.AUDITOR_RESULT_XML, "non/existent/file");
    mojo.setPluginContext(map);
    mojo.execute();
  }

  @Test(expected = MojoExecutionException.class)
  public void testKeyNotSetException() throws MojoExecutionException, MojoFailureException {
    AuditorReportPackageMojo mojo = new AuditorReportPackageMojo();
    mojo.setPluginContext(new HashMap<String, String>());
    mojo.execute();
  }
}