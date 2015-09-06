# FTLAV
*FTL: Faster Than Light Adventure Visualiser*

With this tool you can record and visualise your unique adventures in the videogame [FTL:Faster Than Light](http://www.ftlgame.com/). This could use as a way to archive a particular spectacular FTL session to save it for posterity. Alternatively, this tool could be used for optimising your play style.

<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png" /></a>

Here’s an example of a full FTL game from beginning to end:
<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png" /></a>

## Usage
Start FTL: Faster Than Light; start a new game or continue an existing one. Now start FTLAV. If it's your first time it will ask where your FTL data is located. After this, click *Open* and select the file `continue.sav` (this file contains all information regarding your current run). 

If the selected file is valid you will be presented with a second window showing an empty graph. In the main window you can select the types of data you want the graph to keep track of. You can toggle these on and off at any time without loosing any data. Please note that when toggling changes won’t show up instantly, but only when the current gamestate is updated.

Play FTL as you would do normally with FTLAV running in the background. The graph will update automatically each time you reach a safe point in the game, usually after an encounter or exiting a store.

If you get a game-over by either loosing or winning the game, FTL will delete `continue.sav`. This means that FTLAV can’t track any new data and will respond with a “File Not Found” error. But you can still save your graph by pressing the *Export* button. This will save the graph as two PNG images, one with a transparant background and one without, both at the resolution of the graph window.

It’s possible to track data from games that are already ongoing. However, it’s not possible to look into the past from the moment you’ve started tracking. This also means that if you close FTLAV, all data from before the current beacon will be lost. You can however open and close FTL at any moment without using any graph data.

## Roadmap
### Version 1 (7 September 2015)
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

### Version 2 (TBA)
**Data types**

- Systems and system upgrades
- Player distance to enemy fleet
- Crew skills (pilot, engine, shields, weapons, repairing, combat) as difference per sector
- Crew stats (total repairs, combat kills, evasions, survived jumps, skills points) as difference per sector
- Ship log (total ships defeated, total beacons explored, total scrap collected, total crew hired) as difference per sector

**Graph Features**

- Bar graphs
- Second axis

**Graph Graphics**

- New graph title style
- Graph title with sprite of current player ship exterior
- Crew member key next to graph title with a fitting sprite and line graph colour for each

**UI**

- Quick refresh button
- Export gather data as CSV
- An Export As… button with options for PNG, PNG with transparancy, TIF, TGA and CSV (data only)
- More compact UI (less scrolling, more iconography, less typography)


## Contribute
If you have ideas, suggestions, problems or encountered a bug (definitely not as rare as you might think), please let me know, preferably by filing an *[issue](https://github.com/Niels-NTG/FTLAV/issues)* at this GitHub repository.

If you want to delve into the code you will probably need some knowledge of Processing (Java-like code that is responsible for the graph graphics) or/and Java (UI, data-parsing and basicly everything else). For the Java part you can look at [Vhati](https://github.com/Vhati)’s version of [FTL Profile Editor](https://github.com/Vhati/ftl-profile-editor), of which a large part of the code of this project is based on. Use [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) to compile the source code.
