import java.io.FileWriter;
import java.util.Scanner;

/**
 * Command-driven program for working with grid-based maps rendered as text.
 * 
 * @author Gazi MD Wasi-ul-hoque Eusha
 * @version 6 April 2022
 */
public class CharMap {

    /**
     * Converts the multi-line String into a 2D array of characters.
     * Each line in the String should be the same length for best results.
     */
    public static char[][] convertStringMap(String str) {
        char[][] result;
        String[] rows;
        
        rows = str.split("\n");
        result = new char[rows.length][];
        for (int r = 0; r < rows.length; r++) {
            result[r] = rows[r].toCharArray();
        }
        
        return result;
    }
    
    /** Reads the next character the user enters. */
    public static char nextChar(Scanner in) {
        return in.next().charAt(0);
    }
    
    /** Displays the given character-based map with a leading and trailing blank line. */
    public static void displayMap(char[][] map) {
        
        // output contents in the array
        System.out.println();
        for (int r = 0; r < map.length ; r++){
            for (int c = 0; c < map[r].length; c++ ){
                System.out.print(map[r][c]);
            }
            System.out.println();
        }
        System.out.println();
    }   
    
    /** Returns true if (row, col) is inside the map's bounds, false otherwise. */
    public static boolean inBounds(int row, int col, char[][] map) {        
        // variable to check if everything is in bounds
        boolean condition = false;

        // to check row length and column
        if (col >= 0 && row >= 0 && row < map.length && col < map[row].length ){
            condition = true;
        }                
        return condition;
    }
    
    /**
     * Displays the 9 grid squares in the map centred at (row, col). Displays a
     * space for any grid square that is outside the map's bounds.
     */
    public static void zoom(char[][] map, int row, int col) {
        for (int r = row - 1; r < row + 2 ; r++){
            for (int c = col - 1; c < col + 2; c++ ){
                if (inBounds(r, c, map)){
                    System.out.print(map[r][c]);
                } else {                
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Starts a flood fill operation by selecting the replacement grid square value
     * at the given row and column. Returns the number of cells modified.
     */
    public static int recolour(char[][] map, int row, int col, char fill) {
        //Must be within the bounds of the map and not already equal to fill
        if (inBounds(row, col, map) && map[row][col] != fill) {
            return floodFill(map, row, col, map[row][col], fill);
        }
        return 0;
    }

    /**
     * Performs flood fill, replacing replace with fill, starting from (row, col)
     * and returns the number of cells changed.
     */
    public static int floodFill(char[][] map, int row, int col, char replace, char fill) {
        //variable to find the number of cells changed
        int changed = 0;

        if (inBounds(row, col, map) && map[row][col] == replace){
            map[row][col] = fill; 
            changed += 1;
            changed = changed + floodFill(map, row - 1 , col, replace, fill);
            changed = changed + floodFill(map, row + 1, col, replace, fill);
            changed = changed + floodFill(map, row, col - 1, replace, fill);
            changed = changed + floodFill(map, row, col + 1, replace, fill);
        }

        return changed;
    }

    /**
     * Function to write the data inside a file in order to permanently store it
     * @param data the value which needs to be writen
     */
    public static void fileWriter(char[][] map) {
        
        // data writing in an exception handling code block in case any error occurs
        try {
            // file writer object instantiation
            FileWriter writeFile = new FileWriter("Character Array Store.txt",true);
            String messageOutput = ""; // to convert the values of the charater array to a string format
            // writing the content in the file
            for (int r = 0; r < map.length ; r++){ // to go through the rows
                for (int c = 0; c < map[r].length; c++ ){ // to go through the columns
                    messageOutput += map[r][c]; 
                    writeFile.write(messageOutput);                    
                }
                writeFile.write("\n"); // write in a new line
                
            }
            System.out.println();
            

            // to close the file after writing the value
            writeFile.close();
        } catch (Exception e) {
            System.out.println("Error Occoured while writing in the file " + e.getMessage()); // error message if an exception happens
        }

    }
  
    public static void main(String[] args) {
        //Commands
        final char CMD_DISPLAY = 'd', CMD_ZOOM = 'z', CMD_RECOLOUR = 'r', CMD_HELP = '?',
                   CMD_SAVE = 's', CMD_QUIT = 'q';
        Scanner in = new Scanner(System.in);
        char[][] map; // the character-based map
        char command; //user's entered command
        int row;   //|
        int col;   //|- command parameters
        char fill; //|
        //The initial map source. Edit this to create some holes (or change its size).
        String strMap = "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################\n" +
                        "####################";
 
        
        map = convertStringMap(strMap);
        System.out.println("Char Map");
        System.out.println("========");
        System.out.println();
        System.out.println("Enter commands (? for help). There are no further prompts after this point.");
        
        do {
            command = nextChar(in); //read the next single-character command
            
            switch (command) {
                case CMD_DISPLAY:
                    displayMap(map);                     
                    break;
                case CMD_ZOOM: 
                    System.out.println();
                    row = in.nextInt();
                    col = in.nextInt();
                    zoom(map, row, col);
                    System.out.println();                    
                    break;
                case CMD_RECOLOUR:
                    row = in.nextInt();
                    col = in.nextInt();
                    fill = in.next().charAt(0);
                    System.out.println(recolour(map, row, col, fill) + " grid squares changed");                     
                    break;
                case CMD_SAVE: in.nextLine(); //read the arguments given
                               fileWriter(map);
                               System.err.println("Character Map Stored!");
                               break;
                case CMD_QUIT: break;
                case CMD_HELP:
                default: in.nextLine(); //read and discard any arguments given to unknown command
                         System.out.println("\nCommands:");
                         System.out.println(CMD_DISPLAY  + "             \tDisplay the map");
                         System.out.println(CMD_ZOOM     + " row col     \tShow grid squares surrounding (row, col)");
                         System.out.println(CMD_RECOLOUR + " row col fill\tRecolour with fill (a single character) starting at (row, col)");
                         System.out.println(CMD_HELP    +  "             \tShow this list of commands");
                         System.out.println(CMD_SAVE    +  " filename    \tSave the current map to filename");
                         System.out.println(CMD_QUIT    +  "             \tLeave the program");
                         System.out.println("\nCommands may be chained together, separated by whitespace");
                         System.out.println();
            }
        } while (command != CMD_QUIT);
    }
    
}
