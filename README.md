# FTLAV - FTL: Faster Than Light Adventure Visualiser

*FTL: Faster Than Light Adventure Visualiser* (FTLAV) is a tool that records and visualises the player's progress in the rogue-like video game *[FTL: Faster Than Light](https://subsetgames.com/ftl.html)*. The idea of this program was born out of a dissatisfaction with how FTL only shows the player a high score and not much more on its "game over" screen. This does not properly reflect the journey of the player that brought them to this point. Nor does it help players improve their strategy for future play sessions. FTLAV records the player's adventure every step of the way, offering data visualisation and data export features.

![FTLAV v3 screenshot](https://github.com/Niels-NTG/FTLAV/raw/master/img/FTLAV%20v3%20demo%20screenshot.png)

Jump to: [Features](#Features) | [Installation](#Installation) | [Usage](#Usage) | [Acknowledgements](#Acknowledgements)

## Features

FTLAV is meant to be run alongside FTL, watching FTL's save game file for changes while you play. FTLAV parses the game state into human-readable data, which it can export to a `.tsv` (tab-separated values) spreadsheet file. Each row contains detailed data reflecting the game state at that point in this, which includes but is not limited to:

- Number of beacons explored, sector information, rebel fleet advancement.
- Environmental hazards, encounter type, contents of store (if present).
- Cumulative resources and other statistics: total scrap collected, total scrap collected, total ships defeated and total score.
- Current available resources: scrap, fuel, missiles, drone parts, crew members, hull strength, oxygen level and cargo.
- Current system status: total power capacity, power consumption and amount of damage for every system installed on the ship.
- Nearby ship resources and systems, if present.
- Real-world date and time.

Some of this data is presented in a customisable dynamic visualisation which can be exported as an image file.

See the [examples folder](https://github.com/Niels-NTG/FTLAV/tree/master/examples) for an example of FTLAV's output.

## Installation

FTLAV requires the following:

- *[FTL: Faster Than Light](https://subsetgames.com/ftl.html)*
  - Version 1.6.4 or newer for Mac/Linux/Windows
  - Older versions of the game *might* also work (untested)
  - Modded versions of the game *might* work (untested)
- Any version of macOS, Linux or Windows that [supports Java 8](https://www.java.com/en/download/help/sysreq.html) up to [Java 17](https://www.oracle.com/java/technologies/javase/products-doc-jdk17certconfig.html). Never versions of Java may not work with FTLAV.
  - Mac OS X 10.8.3 or newer
  - Ubuntu 14.04 or newer
  - Windows Vista SP2 or newer
- The [latest release of FTLAV itself](https://github.com/Niels-NTG/FTLAV/releases/latest)

## Usage

### Startup

When starting FTLAV for the first time it will try to auto-detect the location of FTL resources scanning for common installation locations of the standalone, Steam and GoG versions of the game on Mac, Linux and Windows. When confirmed this location will be remembered for the next time you use FTLAV.

FTLAV will try to automatically load the current FTL save file (`continue.sav`) on startup. If it cannot find this file you likely do not have an ongoing session, meaning you should start a new game in FTL. Use the "![load game icon](https://github.com/Niels-NTG/FTLAV/raw/master/src/main/resources/UI/loadgame.gif) Load save game" button to manually load a save game file.

### Recording your FTL game

FTL's `continue.sav` file only stores the most current state of the game. For this reason FTLAV watches this file for changes and will attempt to parse the file and append a new row to its recording when you arrive at a beacon different from the previous one. Or when there's new information for the current beacon, it will update the last row with this new information.

Please note that due to the mysterious nature of the FTL save game file format, FTLAV won't always be able to parse the current game state. In such a case FTLAV skips recording this game state and waits for the next one.

### Export recording

To export the recording to a `.tsv` (tab-separated values) spreadsheet file, click the "![new game icon](https://github.com/Niels-NTG/FTLAV/raw/master/src/main/resources/UI/new.gif) New recording" button and then choose a file location and file name.

After creating this file, FTLAV will append new recorded information  to this file as long as the current FTL save game exists.

### Import recording

To resume recording a preexisting recording or merge an existing recording with the current one, click the "![open recording icon](https://github.com/Niels-NTG/FTLAV/raw/master/src/main/resources/UI/open.gif) Open recording" button and choose a TSV file created by FTLAV.

### Visualisation

Some of this data visualised in a separate window that automatically gets redrawn each time new data comes in. This window can be toggled using the "![graph icon](https://github.com/Niels-NTG/FTLAV/raw/master/src/main/resources/UI/graph.gif) Graph" button. The presentation of this data can be changed in the settings panel in FTLAV's main window. Here you also find the "![export graph icon](https://github.com/Niels-NTG/FTLAV/raw/master/src/main/resources/UI/savegraph.gif) Export graph" button to export the current visualisation as an image file.

## Acknowledgements

For the parsing and reading of the binary-formatted FTL save data, FTLAV makes heavy use of the source code of the [FTL Profile Editor](https://github.com/reseto/ftl-profile-editor/) project by GitHub user [reseto](https://github.com/reseto), which is a fork of [FTL Profile Editor](https://github.com/Vhati/ftl-profile-editor) by [Vhati](https://github.com/Vhati), which in turn is a fork from the [original repository](https://github.com/ComaToes/ftl-profile-editor) by [ComaToes](https://github.com/ComaToes). FTLAV would have never gotten off the ground without the efforts of these  people, as well as other contributors to the FTL Profile Editor project.
