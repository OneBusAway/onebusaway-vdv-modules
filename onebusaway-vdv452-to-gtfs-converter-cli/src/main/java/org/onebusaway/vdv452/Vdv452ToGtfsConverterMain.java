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
package org.onebusaway.vdv452;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.UnrecognizedOptionException;

public class Vdv452ToGtfsConverterMain {
  
  private static final String ARG_TIME_ZONE = "timeZone";

  private static CommandLineParser _parser = new PosixParser();

  private Options _options = new Options();

  public static void main(String[] args) {
    Vdv452ToGtfsConverterMain m = new Vdv452ToGtfsConverterMain();
    m.run(args);
  }

  public void run(String[] args) {
    if (needsHelp(args)) {
      printHelp();
      System.exit(0);
    }

    try {
      buildOptions();
      CommandLine cli = _parser.parse(_options, args, true);
      runApplication(cli);
      System.exit(0);
    } catch (MissingOptionException ex) {
      System.err.println("Missing option: " + ex.getMessage());
      printHelp();
    } catch (MissingArgumentException ex) {
      System.err.println("Missing option argument: " + ex.getMessage());
      printHelp();
    } catch (UnrecognizedOptionException ex) {
      System.err.println("Unknown option: " + ex.getMessage());
      printHelp();
    } catch (AlreadySelectedException ex) {
      System.err.println("Option already selected: " + ex.getMessage());
      printHelp();
    } catch (ParseException ex) {
      System.err.println(ex.getMessage());
      printHelp();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    System.exit(-1);
  }

  private void buildOptions() {
    _options.addOption(ARG_TIME_ZONE, true, ARG_TIME_ZONE);
  }

  private void runApplication(CommandLine cli) throws IOException {
    String[] args = cli.getArgs();
    if (args.length != 2) {
      printHelp();
      System.exit(-1);
    }

    Vdv452ToGtfsConverter converter = new Vdv452ToGtfsConverter();
    converter.setInputPath(new File(args[0]));
    converter.setOutputPath(new File(args[1]));
    
    if (cli.hasOption(ARG_TIME_ZONE)) {
      converter.setTimeZone(TimeZone.getTimeZone(cli.getOptionValue(ARG_TIME_ZONE)));
    }
    converter.run();
  }

  private void printHelp() {
    System.err.println("usage: [program] path/to/vdv-input path/to/gtfs-output");
  }

  private boolean needsHelp(String[] args) {
    for (String arg : args) {
      if (arg.equals("-h") || arg.equals("--help") || arg.equals("-help"))
        return true;
    }
    return false;
  }

}
