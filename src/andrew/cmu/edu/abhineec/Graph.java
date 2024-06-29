/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 * */

package andrew.cmu.edu.abhineec;

import java.io.*;
import java.util.*;

/********************************************************************************************************
 * RedBlackNode class represents a node in a RedBlackTree with properties
 * vertices - an array of strings representing different courses
 * adjacencyMatrix - a 2D array representing edges in the course graph
 * dictionary - a RadBlackTree object representing the dictionary of courses
 ********************************************************************************************************/

public class Graph {
    private String[] vertices;
    private boolean[][] adjacencyMatrix;
    private RedBlackTree dictionary;
    private BufferedWriter bufferedWriter;

    private static final String RESULTS_FILE_PATH = System.getProperty("user.dir") + "/" + "result.txt";

    /**
     * Initialize a Graph object
     * @param inputFilePath
     *      the file path for the stydent data that needs to be loaded
     * @precondition
     *  The String inputFilePath contains the path to
     *  a file formatted in the exact same way as Project 3's description
     * @postcondition
     *    The Graph is constructed and its
     *    corresponding adjacent matrix is added to
     *    result.txt and printed to console
     **/
    public Graph(String inputFilePath) throws IOException {
        //initialize the BufferedWriter Object
        this.bufferedWriter = new BufferedWriter(new FileWriter(RESULTS_FILE_PATH, true));

        //initialize a new dictionary (i.e. RedBlackTree)
        this.dictionary = new RedBlackTree();
        //Since, There is a maximum of 20 different courses offered each term.
        //vertices or adjacencyMatrix  can have a max of 20 or 20x20 entries
        this.vertices = new String[20];
        this.adjacencyMatrix = new boolean[20][20];

        File fileObj = new File(inputFilePath);
        Scanner myReader = new Scanner(fileObj);
        //for each student's data
        while(myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] userDetails = line.split(" ");

            //create a list of courses taken by current student
            ArrayList<String> courseList = new ArrayList<String>();
            for(int i = 2; i<userDetails.length; i++) {
                courseList.add(userDetails[i]);
            }

            //populate vertices and adjacencyMatrix for current student's courseList
            for(int i = 0; i<courseList.size(); i++){
                String currentCourse = courseList.get(i);
                //if course is new, adds to dictionary and returns its index in vertices array
                //else only returns its index in vertices array
                int currentCourseIndex = dictionary.getOrInsert(currentCourse);
                //add currentCourse to veritces array at its index
                vertices[currentCourseIndex] = currentCourse;
                //populate adjacency matrix for currentCourse
                for(int j=i+1; j<courseList.size(); j++){
                    String adjacentCourse = courseList.get(j);
                    int adjacentCourseIndex = dictionary.getOrInsert(adjacentCourse);
                    adjacencyMatrix[currentCourseIndex][adjacentCourseIndex] = true;
                    adjacencyMatrix[adjacentCourseIndex][currentCourseIndex] = true;
                }
            }
        }
        myReader.close();

        this.bufferedWriter.append("\n");
        //print adjacency matrix and append to result.txt
        for(int i=0; i< dictionary.getCountNodes(); i++){
            String lineToWrite = "";
            for(int j=0; j< dictionary.getCountNodes(); j++){
                int connection = adjacencyMatrix[i][j]?1:0;
                System.out.print(connection);
                lineToWrite += connection;
            }
            System.out.println();
            this.bufferedWriter.append(lineToWrite);
            this.bufferedWriter.append("\n");
        }
        this.bufferedWriter.append("\n");
    }

    /**
     * Generate exam schedule using Graph Coloring algorithm
     * @precondition
     *  Graph has been initialized
     * @postcondition
     *   An optimal schedule for exam has been posted to result.txt
     *   and printed on console
     **/
    public void scheduleExams() throws IOException {
        //maintain a boolean array representing nodes that have not been coloured yet
        boolean[] availableToColour = new boolean[dictionary.getCountNodes()];
        Arrays.fill(availableToColour, true);

        Integer v = 0;
        int index=1;
        //unless all nodes have been colored
        while(v!=null){
            Set<Integer> newclr = new HashSet<Integer>();
            //find the next set of vertices that can be colored together
            greedyClr(v, newclr, availableToColour);
            //these vertices represent courses that can be placed in the same exam slot
            //print and add to results.txt
            System.out.print("Final Exam Period " + index + " => ");
            String lineToWrite = "Final Exam Period " + index++ + " => ";
            for(Integer x:newclr) {
                System.out.print(vertices[x] + " ");
                lineToWrite += vertices[x] + " ";
            }
            System.out.println();
            this.bufferedWriter.append(lineToWrite);
            this.bufferedWriter.append("\n");
            //find the next unexamined node in entire array
            v = getIndexOfNextGraphNodeAvailableToColour(-1, availableToColour);
        }
        this.bufferedWriter.close();
    }

    /**
     * Color the graph with a greedy approach
     * @precondition
     *  Graph has been initialized
     * @postcondition
     *   A list of vertices that can be colored with the same
     *   color has been added to newclr set
     * @reference
     *   The function remains true to the pseudocode provided in
     *   Project 3's description
     **/
    private void greedyClr(Integer v, Set<Integer> newclr, boolean[] availableToColour){
        boolean found = false;
        Integer w=null;
        //until all uncolored vertex have been examined
        while(v != null){
            String courseName = this.vertices[v];
            found = false;
            //check if there is an edge between v and any vertex in newclr
            for(Integer currentColoredVertex : newclr) {
                w = currentColoredVertex;
                if(this.adjacencyMatrix[v][w])
                    found = true;
            }
            //if no edge found -> v can be colored same with all vertex in new clr
            //add v to newclr and mark it as colored
            if(!found){
                availableToColour[v] = false;
                newclr.add(v);
            }
            //search for the next unexamined + uncoloured node in graph
            v = getIndexOfNextGraphNodeAvailableToColour(v, availableToColour);
        }
    }

    /**
        Helper function to find the next vertex in graph that is
        available for colouring (v)
     */
    private Integer getIndexOfNextGraphNodeAvailableToColour(int v, boolean[] availableToColour) {
        for(int i=v+1; i<availableToColour.length; i++)
            if(availableToColour[i])
                return i;
        return null;
    }


    /**
     * Main function demonstrating program execution
     **/
    public static void main(String[] a) throws IOException {
        System.out.println("Hello and welcome!\n");
        System.out.println("This assignment was submitted by:\nName: Abhineet Chaudhary\nAndrewId: abhineec\nCourse: 95-771 A Fall 2023\n\n");

        Scanner userScanner = new Scanner(System.in);

        System.out.println("Would you like to append the output to results.txt or overwrite the results.txt file completely?\n0 -> APPEND\n1 -> OVERWRITE");
        int overwriteChoice =  Integer.parseInt(userScanner.nextLine());
        if(overwriteChoice==1){
            FileWriter fw = new FileWriter(RESULTS_FILE_PATH, false);
            fw.write("abhineec");
            fw.close();
        }
        System.out.println();

        System.out.println("Enter absolute path for input file:");

        String inputFilePath = userScanner.nextLine();
        System.out.println();

        System.out.println("abhineec");
        Graph graph = new Graph(inputFilePath);
        System.out.println();

        graph.scheduleExams();

    }

}
