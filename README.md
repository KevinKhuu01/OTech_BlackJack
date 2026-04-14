# Blackjack By Kevin Khuu and Awrron Kavian

## Overview
This project is for an Ontario Tech themed Blackjack app, featuring a betting function, cards, and tables with an Ontario Tech inspired design and multiplayer capabilities (up to 3 players at a table, unlimited number of tables). Players can create their own account complete with a starting balance of $10,000 and can add funds whenever they like. After logging in, players can create their own public tables to start playing or join other tables with their friends to play against each other and the dealer. We hope you will enjoy Ontario Tech Blackjack! 

## How to run
Below are the steps on how to get Ontario Tech Blackjack running on your computer. Please note that in order to run the app, an Integrated Development Environment is needed to clone the repository (Intellij IDEA IDE is recommended). Java version <ADD JAVA VERSION HERE> or more recent is also needed. 
- 1: On the main menu of the github repo of the project, click the green button that says `<> Code` and copy the `HTTPS` link to your clipboard.
- 2: Open your preferred terminal and navigate to the directory you want to clone the program into. You can also create a new folder to store it in.
- 3: Once you are in the desired directory, type `git clone <PASTE URL HERE>` into the command line and press enter. Once finished cloning, you can open the project in your preferred IDE
- 5: Locate the Server java file (`src\main\java\server`) and double click to open. you can run this file by pressing the green play button at the top of the page.
- 6: Next, locate the Client java file (`src\main\java\frontend`) and double click it to open. run this file with the green play button and you're now ready to start Ontario Tech BlackJack!
  ### Important Notes
  - When starting up the application, **Always** run the Server file before running the Client file. Running Client first will cause an error.
  - In order to use multiplayer features, you must enable multiple client instances to be run at the same time. You can enable this by navigating to **Run -> Edit Configurations -> Client -> Modify Options** and checkmark the 'Allow multiple instances' option to have multiple players on one device at a single time.
 
## Video Demo URL
https://youtu.be/tWeRdPixFRs 
