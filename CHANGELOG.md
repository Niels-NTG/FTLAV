# FTLAV 3.1.0

- NEW: Added markers in visualisation that show hazards and other information. These include: Store (S), Red Giant (RG), Pulsar (P), Asteroid Field (AF), Anti-Ship Battery (ASB), Rebel Fleet (RF) and Hostile Ship (HS). Addresses issue [#6](https://github.com/Niels-NTG/FTLAV/issues/6).
- NEW: Create a recording when the player arrives at the beacon and whenever the event type changes, allowing the recording of both the start and end of an encounter at the same beacon.
- NEW: Enable hull strength in the visualiser by default.
- NEW: Record the ID number of each beacon.
- FIX: Set minimum size for visualiser window.
- FIX: Save game parsing is now much more reliable.
- FIX: Create only 1 release, not one for Mac/Linux and one for Windows, since there was no difference between these.

# FTLAV 3.0.0

It has been 9 years, but a new version of FTLAV is finally here!

- NEW: Now compatible with FTL v1.6.4, the latest version of the game!
- NEW: Spreadsheet export and import feature has been completely rewritten to be much more reliable.
- NEW: Feature resume earlier recordings.
- NEW: Added new data types for recording:
    - Crew list of player and enemy ship.
    - Store contents, if a store is present.
    - More expansive listing of weapon layout, drone layout and ship augments of both the player and enemy ship.
    - …and more!
- NEW: Redesigned visualiser.
- NEW: Redesigned UI.
- NEW: Massive under-the-hood-changes
    - Saved game parser code is now based on code from the [FTL Profile Editor](https://github.com/reseto/ftl-profile-editor/) by [Reseto](https://github.com/reseto).
    - Upgraded from Java 7 to Java 8.
    - Lots of cleanup.