# FTLAV
*FTL: Faster Than Light Adventure Visualiser*

With this tool you can record and visualise your unique adventures in the videogame [FTL: Faster Than Light](http://www.ftlgame.com/). This could use as a way to archive a particular spectacular FTL session to save it for posterity. Alternatively, this tool could be used for optimising your play style.

<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png" /></a>

Here’s an example of a full FTL game from beginning to end:
<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png" /></a>

## Usage
Before you do anything make sure you have FTL: Faster Than Light installed and have downloaded the [latest version of FTLAV from the releases page](https://github.com/Niels-NTG/FTLAV/releases). FTLAV is powered by Java, meaning that it can run on any operation system (Mac OS X, Windows, Linux, etc.) that supports Java 7 or higher. It’s designed to work with any up-to-data version of FTL for Mac/Linux/Windows, no matter if you bought it from Steam, GOG, ftlgame.com or some other place. It’s possible for modded versions of FTL to work with FTLAV, but it might not work for you.

Start FTL: Faster Than Light; start a new game or continue an existing one. Now start FTLAV. If it's your first time it will ask where your FTL data is located. After this, click ***Open*** and select the file `continue.sav` (this file contains all information regarding your current run). 

If the selected file is valid you will be presented with a second window showing an empty graph. In the main window you can select the types of data you want the graph to keep track of. You can toggle these on or off at any time without loosing any data. Please note that when toggling changes won’t show up instantly, but only when the current gamestate is updated.

Play FTL as you would do normally with FTLAV running in the background. The graph will update automatically each time you reach a safe point in the game, usually after an encounter or when exiting a store.

If you get a game-over by either loosing or winning the game, FTL will delete `continue.sav`. This means that FTLAV can’t track any new data and will respond with a “File Not Found” error. But you can still save your graph as an image by pressing the ***Export image*** button. This will save the graph as two PNG images, one with a [transparant background](https://raw.github.com/Niels-NTG/FTLAV/master/img/game2alpha.png) and [one without](https://raw.github.com/Niels-NTG/FTLAV/master/img/game2.png), at the current resolution of the graph window. Alternatively you can export the raw data by clicking ***Export data***. This will save all data as a CSV-file. This can be read by pretty much all spreadsheet applications. You could use this for example to maintain an Excel document containing data from all runs with a particular type of ship. But with CSV being such a flexible format you might have very different ideas on what to do with your FTL data. If so, share your creations with the community, but please don’t forget to mention FTLAV. *If you’re using Apple Numbers you might want to convert the file using [this script](http://www.appletips.nl/files/CSVtoTabs-Clipboard.zip) so Numbers can parse it correctly.*

[Here’s an example of such a CSV-file](https://raw.github.com/Niels-NTG/FTLAV/master/CSVexamples/example(FederationCruiseA-EASY-AE).csv). The image below was captured during the same session.
<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/game2.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/game2.png" /></a>

It’s possible to track data from games that are already ongoing. However, it’s not possible to look into the past from the moment you’ve started tracking. This also means that if you close FTLAV, all data from before the current beacon will be lost. You can however open and close FTL at any moment without using any graph data.


## Roadmap
### Version 1 (13 September 2015)
**Data types**

- Scrap count
- Hull level
- Fuel count
- Drone Parts count
- Missiles count
- Crew size
- Ship Oxygen level
- Crew member health
- Crew skills (pilot, engine, shields, weapons, repairing, combat)
- Crew stats (total repairs, combat kills, evasions, survived jumps, skills points)
- Ship log (total ships defeated, total beacons explored, total scrap collected, total crew hired)

**Graph Features**

- Labeled sectors (name, type, number)
- Beacon labels (as N explored beacons)
- Labeled graph lines
- Coloured graph lines (purple for crew, blue for the rest)
- Graph title (ship name, current score, difficulty, Advanced Edition on/off)

**Graph Graphics**

- FTL colour scheme
- FTL blue glowing lines
- FTL typography

**UI**

- Export to PNG
- Export to PNG with transparant background
- Help menu

### Version 2 (27 september 2015)
**Data types**

- Enemy fleet advancement
- Info on any system (capacity, current energy, damage) on the players ship and nearby ships (CSV exports only)

**UI**

- Export the full dataset as a CSV-file

### Version 3 (TBA)
**Data types**

- Ship log (total ships defeated, total beacons explored, total scrap collected, total crew hired) as difference per beacon
- System upgrades
- Reactor capacity

**Graph Features**

- Bugfix for displaying large numbers ([issue #5](https://github.com/Niels-NTG/FTLAV/issues/5))
- Graph canvas gets horizontal scrollbar when information gets too dense to display it properly
- Second axis using a different scale

**UI**

- Quick-refresh button

### Future versions
**Data types**

- Crew skills (pilot, engine, shields, weapons, repairing, combat) as difference per sector
- Fixed bugs involving shrinking and growing crews
- Generate sequentially numbered backups of `continue.sav` in order to preserve current game history for tracking the same game over multiple sessions.

**Graph Features**

- Bar graphs to indicate totals or averages for each sector
- Systems and system upgrades in graph as icons/sprites
- Add more interactivity by adding a mouse-over feature that reveals extra information at any beacon
- Add more interactivity by enabling users to add annotations at any chosen beacon
- Scatter plots?
- Separate graphs that show overall player performance
- New graph title style
- Crew member key next to graph title with a fitting sprite and line graph colour for each
- PDF export

**UI**

- More compact UI (less scrolling, more iconography, less typography)


## Contribute
If you have ideas, suggestions, problems or encountered a bug, please let me know, preferably by filing an [issue](https://github.com/Niels-NTG/FTLAV/issues) at this GitHub repository.

If you want to delve into the code you will probably need some knowledge of Processing (Java-like code responsible for the graph graphics) or/and Java (UI, data-parsing and basicly everything else). For the Java part you can look at [Vhati](https://github.com/Vhati)’s version of [FTL Profile Editor](https://github.com/Vhati/ftl-profile-editor), of which a large part of the code of this project is based on. Use [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) to compile the source code.