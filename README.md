onebusaway-vdv-modules [![Build Status](https://travis-ci.org/OneBusAway/onebusaway-vdv-modules.svg?branch=master)](https://travis-ci.org/OneBusAway/onebusaway-vdv-modules)
======================

Libraries and tools for working with transit data conforming to the VDV specification.  Includes:

* onebusaway-vdv452: a Java library for parsing and processing [VDV-452](http://mitglieder.vdv.de/module/layout_upload/452_sesv14.pdf) transit schedule data.
* onebusaway-vdv452-converter-cli: a Java command-line utility for converting VDV-452 schedule data into the [GTFS](https://developers.google.com/transit/gtfs/) format

## Converting VDV-452 to GTFS

To convert transit schedule data in the VDV-452 format into GTFS, use the our handy utility.

[Download the latest version of onebusaway-vdv452-converter-cli](http://nexus.onebusaway.org/service/local/artifact/maven/redirect?r=public&g=org.onebusaway&a=onebusaway-vdv452-to-gtfs-converter-cli&v=LATEST)

The utility is a executable Java jar file, so you'll need Java installed to run the tool.  To run it:

    java -jar onebusaway-vdv452-converter-cli.jar [-args] input_vdv_path output_gtfs_path

**Note**: Converting large GTFS feeds is often processor and memory intensive.
You'll likely need to increase the max amount of memory allocated to Java with
an option like -Xmx1G (adjust the limit as needed). I also recommend adding the
-server argument if you are running the Oracle or OpenJDK, as it can really
increase performance.

### Arguments

* `input_vdv_path` - path to a zip file or directory containing VDV-452 .x10 files (note the lower-case x in .x10).  For zip files, all files must be in the root of the zip. 
* `output_gtfs_path` - path to a zip file or directory where the converted GTFS feed will be written.
