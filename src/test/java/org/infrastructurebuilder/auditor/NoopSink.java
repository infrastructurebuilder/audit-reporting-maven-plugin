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

import org.apache.maven.doxia.logging.Log;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributes;

import java.util.ArrayList;
import java.util.List;

public class NoopSink implements Sink {
  private List<String> elements = new ArrayList<>();

  public List<String> getElements() {
    return elements;
  }

  @Override
  public void enableLogging(Log log) {

  }

  @Override
  public void head() {

  }

  @Override
  public void head(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void head_() {

  }

  @Override
  public void title() {

  }

  @Override
  public void title(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void title_() {

  }

  @Override
  public void author() {

  }

  @Override
  public void author(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void author_() {

  }

  @Override
  public void date() {

  }

  @Override
  public void date(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void date_() {

  }

  @Override
  public void body() {

  }

  @Override
  public void body(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void body_() {

  }

  @Override
  public void sectionTitle() {

  }

  @Override
  public void sectionTitle_() {

  }

  @Override
  public void section1() {

  }

  @Override
  public void section1_() {

  }

  @Override
  public void sectionTitle1() {

  }

  @Override
  public void sectionTitle1_() {

  }

  @Override
  public void section2() {

  }

  @Override
  public void section2_() {

  }

  @Override
  public void sectionTitle2() {

  }

  @Override
  public void sectionTitle2_() {

  }

  @Override
  public void section3() {

  }

  @Override
  public void section3_() {

  }

  @Override
  public void sectionTitle3() {

  }

  @Override
  public void sectionTitle3_() {

  }

  @Override
  public void section4() {

  }

  @Override
  public void section4_() {

  }

  @Override
  public void sectionTitle4() {

  }

  @Override
  public void sectionTitle4_() {

  }

  @Override
  public void section5() {

  }

  @Override
  public void section5_() {

  }

  @Override
  public void sectionTitle5() {

  }

  @Override
  public void sectionTitle5_() {

  }

  @Override
  public void section(int i, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void section_(int i) {

  }

  @Override
  public void sectionTitle(int i, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void sectionTitle_(int i) {

  }

  @Override
  public void list() {

  }

  @Override
  public void list(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void list_() {

  }

  @Override
  public void listItem() {

  }

  @Override
  public void listItem(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void listItem_() {

  }

  @Override
  public void numberedList(int i) {

  }

  @Override
  public void numberedList(int i, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void numberedList_() {

  }

  @Override
  public void numberedListItem() {

  }

  @Override
  public void numberedListItem(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void numberedListItem_() {

  }

  @Override
  public void definitionList() {

  }

  @Override
  public void definitionList(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void definitionList_() {

  }

  @Override
  public void definitionListItem() {

  }

  @Override
  public void definitionListItem(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void definitionListItem_() {

  }

  @Override
  public void definition() {

  }

  @Override
  public void definition(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void definition_() {

  }

  @Override
  public void definedTerm() {

  }

  @Override
  public void definedTerm(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void definedTerm_() {

  }

  @Override
  public void figure() {

  }

  @Override
  public void figure(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void figure_() {

  }

  @Override
  public void figureCaption() {

  }

  @Override
  public void figureCaption(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void figureCaption_() {

  }

  @Override
  public void figureGraphics(String s) {

  }

  @Override
  public void figureGraphics(String s, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void table() {

  }

  @Override
  public void table(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void table_() {

  }

  @Override
  public void tableRows(int[] ints, boolean b) {

  }

  @Override
  public void tableRows_() {

  }

  @Override
  public void tableRow() {

  }

  @Override
  public void tableRow(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void tableRow_() {

  }

  @Override
  public void tableCell() {

  }

  @Override
  public void tableCell(String s) {

  }

  @Override
  public void tableCell(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void tableCell_() {

  }

  @Override
  public void tableHeaderCell() {

  }

  @Override
  public void tableHeaderCell(String s) {

  }

  @Override
  public void tableHeaderCell(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void tableHeaderCell_() {

  }

  @Override
  public void tableCaption() {

  }

  @Override
  public void tableCaption(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void tableCaption_() {

  }

  @Override
  public void paragraph() {

  }

  @Override
  public void paragraph(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void paragraph_() {

  }

  @Override
  public void verbatim(boolean b) {

  }

  @Override
  public void verbatim(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void verbatim_() {

  }

  @Override
  public void horizontalRule() {

  }

  @Override
  public void horizontalRule(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void pageBreak() {

  }

  @Override
  public void anchor(String s) {

  }

  @Override
  public void anchor(String s, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void anchor_() {

  }

  @Override
  public void link(String s) {

  }

  @Override
  public void link(String s, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void link_() {

  }

  @Override
  public void italic() {

  }

  @Override
  public void italic_() {

  }

  @Override
  public void bold() {

  }

  @Override
  public void bold_() {

  }

  @Override
  public void monospaced() {

  }

  @Override
  public void monospaced_() {

  }

  @Override
  public void lineBreak() {

  }

  @Override
  public void lineBreak(SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void nonBreakingSpace() {

  }

  @Override
  public void text(String s) {
    this.elements.add(s);
  }

  @Override
  public void text(String s, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void rawText(String s) {

  }

  @Override
  public void comment(String s) {

  }

  @Override
  public void unknown(String s, Object[] objects, SinkEventAttributes sinkEventAttributes) {

  }

  @Override
  public void flush() {

  }

  @Override
  public void close() {

  }
}
