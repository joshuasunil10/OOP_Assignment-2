# MySearchEngine | OOP Java Assignment
#### This is a Java Application that utilizes OOP Principles to create a searching system for a set of .txt files within a selected diretory

## Key Features

### Select a Directory
##### The User can select a specific directory of .txt file to search for a search term

### Ranking
##### The search terms are returned to the user and is ranked based on the amount of matches of that search term in the text files.

### Preview Panel
##### Allows for preview of the file within the Application it self and the search term is highlighted in it for the user's reference

### Advanced Searching
##### The user can use the * operator and ? operator to search for wildcards within the search term, for example, Walk* = walk, walks, walking and the most relevant search file is presented within the results at the top

### Search History 
##### Keeps track of the user's searches for each instance of the application being run. The user can also delete the history if they wish.

### Status Indication
##### shows system status information to the user at the status bar in the bottom left of the application

### Additional Features

##### - Logging of Search Process within the IDE Console using System.out.println.


# Structure of the Application
## There are 3 Classes which are vital to the operatbility of the system.

### SearchEngineGUI
##### This java class encapsulates all GUI components and elements within the class, and provides a clear and minimal interface for the user to interact with, 
#### It also incorporates abstraction by taking away the complexities of the GUI for the user, and only focus on the searching functionality which is handled by FileSearcher

### FileSearcher
##### Also using encapsulation, the logic for searching through the files is located here, this keeps the searching function separate to the GUI, The 

### FileMatch 
##### provides a constuctor for the file match class, and getters for getFile, and getMatchCount

### AdvancedSearching
##### This class implements the WildCardSearch interface for wildcard searching

OOP Principles, Abstraction, Encapsulation, Inheritance

# How to use? 
 ## 1. Make sure you have a directory of text files (.txt)
 ## 2. Run the SearchEngineGUI class, this contains the runnable file
 ## 3. Select your chosen directory
 ## 4. Start Searching


 # Created Using 
 ### Java Development Kit, JavaSwing, and EclipseIDE


### Author 
#### Joshua Sunil Mathew, C22419706
 



