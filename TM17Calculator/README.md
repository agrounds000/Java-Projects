## About this project:

This project is an attempt to simplify the TM17 Glitch that can be found in the English versions of Pokemon Gold and Silver that can be found here https://www.youtube.com/watch?v=PsIb3OZaYAs
Originally a user would have to constantly swap between two different tabs: one in Google Sheets, the other to convert hex code into text that can be written in the game to perform **Arbitrary Code Execution** (or **ACE** for short). This projects combines both into a single program that is easier for users that are not comfortable working in hexadecimal and writing values into different addresses in the game.

This project was written using Apache Derby as the database, and Window Builder for the GUI.

## Known Bugs
Some Pokemon that have special characters in their name (such as Nidoran♂ and Nidoran♀) do not show up properly. Also Pokemon that have special characters that must be written into some addresses do not show up properly in the GUI and are either emitted entirely or shows a random symbol. For example, Heracross should return 'Pk♀' but instead shows something else. This is likely an encoding/decoding issue when reading information from the database.
