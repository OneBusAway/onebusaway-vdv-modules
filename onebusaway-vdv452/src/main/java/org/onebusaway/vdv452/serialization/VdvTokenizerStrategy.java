/**
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.vdv452.serialization;

import java.util.List;

import org.onebusaway.csv_entities.DelimitedTextParser;
import org.onebusaway.csv_entities.TokenizerStrategy;

public class VdvTokenizerStrategy implements TokenizerStrategy {

  private final DelimitedTextParser _delimiterLibrary = createParser();

  @Override
  public List<String> parse(String line) {
    return _delimiterLibrary.parse(line);
  }

  @Override
  public String format(Iterable<String> tokens) {
    throw new UnsupportedOperationException();
  }

  private static DelimitedTextParser createParser() {
    DelimitedTextParser parser = new DelimitedTextParser(';');
    parser.setTrimInitialWhitespace(true);
    return parser;
  }  
}
