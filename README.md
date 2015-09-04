# FTLAV
*FTL: Faster Than Light Adventure Visualiser*

With this tool you can record and visualise your unique adventures in the videogame [FTL:Faster Than Light](http://www.ftlgame.com/). This could use as a way to archive a particular spectacular FTL session to save it for posterity. Alternatively, you use this tool for optimising your play style.

<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/screenshot1.png" /></a>

Here’s an example of of a full FTL game from beginning to end:
<a href="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png"><img src="https://raw.github.com/Niels-NTG/FTLAV/master/img/fullgame1.png" /></a>

## Roadmap
- Version 1 (6 September 2015)
	- Data types:
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
	- Graph Features:
		- Labeled sectors (name, type, number)
		- Beacon labels (as N explored beacons)
		- Labeled graph lines
		- Coloured graph lines (purple for crew, blue for the rest)
		- Graph title (ship name, current score, difficulty, Advanced Edition on/off)
	- Graph Art:
		- FTL colour scheme
		- FTL blue glowing lines
		- FTL typography
	- UI:
		- Export to PNG
		- Export to PNG with transparant background
		- Help menu
- Version 2 (TBA)
	- Data types:
		- Systems and system upgrades
		- Player distance to enemy fleet
		- Crew skills (pilot, engine, shields, weapons, repairing, combat) as difference per sector
		- Crew stats (total repairs, combat kills, evasions, survived jumps, skills points) as difference per sector
		- Ship log (total ships defeated, total beacons explored, total scrap collected, total crew hired) as difference per sector
	- Graph Features
		- Bar graphs
		- Second axis
	- Graph Art
		- New graph title style
		- Graph title with sprite of current player ship exterior
		- Crew member key next to graph title with a fitting sprite and line graph colour for each
	- UI:
		- Quick refresh button
		- Export gather data as csv
		- An Export As… button with options for PNG, PNG with transparancy, TIF, TGA and CSV (data only)
		- More compact UI (less scrolling, more iconography, less typography)


## Contributing
If you have an idea, suggestion or you have found a bug (definitely not as rare as you might think), please let me know, preferably by filing an *issue* at this GitHub repository.

If you want to delve into the code you will probably need some knowledge of Processing (Java-like code that is responsible for the graph graphics) or/and Java (interface, data-parsing, everything else). For the Java part you can look at [Vhati](https://github.com/Vhati)’s version of [FTL Profile Editor](https://github.com/Vhati/ftl-profile-editor), of which a large part of the code of this project is based on. Use [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) to compile the source code.