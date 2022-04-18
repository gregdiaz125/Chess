By Gregory Diaz
# Game of Chess #
I created a two player Chess game in Java. This was an assigment aimed at practicing Object-Oriented Code. To focus on proper OOP implementation, the game is played on the terminal.
### Running ###
To build the project run the line below. This will compile all the classes into a new directory called bin

``` 
javac -d bin -classpath bin src/**/*.java 
```
Now enter this directory
``` 
cd bin
```
To start the game enter the command:
``` 
java chess/chess
```
### Playing the game ###
####Making a Move ####
The game starts with the picture of the board and waiting for whites input. The format of the input is: "Start Positon" "End Postion"
``` 
White's move: e2 e4
```
In this example white is moving pawn at e2 to e4

In the case that a player is promoting a piece, if a specific piece is desired it can be requested with the input formated as
``` 
Black's move: e2 e1 N
```
The third input is the type of piece to promote the pawn to. Where the character and its piece type are defined by  
``` 
N -> Knight
B -> Bishop
R -> Rook 
```
If a queen promote is desired then omit the third input, as queen is the default promoting piece.



####Ending the game####
The game is concluded when a player is in check mate , they resign, or call a draw. To resign in the game of chess the player must input 
``` 
Black's move: resign
```
To call a draw a player must input 
``` 
Black's move: draw
```