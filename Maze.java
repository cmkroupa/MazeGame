
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;

public class Maze {

//	instance variables you may need
//	a variable storing the graph, a variable storing the id of the starting node, a variable storing the id of the end node
//	a variable storing the read number of coins, maybe even a variable storing the path so far so that you don't perform accidental 
//	(and unnecessary cycles).
//	if you maintain nodes on a path in a list, be careful to make a list of GraphNodes, 
//	otherwise removal from the list is going to behave in a weird way. 
//	REMEMBER your nodes have a field mark.. maybe that field could be useful to avoid cycles...
    private Graph graph;
    private int coins;
    private GraphNode start;
    private GraphNode exit;
    private Stack<GraphNode> stack;

    public Maze(String inputFile) throws MazeException {
//		initialize your graph variable by reading the input file!
//		to maintain your code as clean and easy to debug as possible use the provided private helper method
        try {
            StringTokenizer terminal_comand =  new StringTokenizer(inputFile, " ");
            String line = "";
            while(terminal_comand.hasMoreTokens()){
                line = terminal_comand.nextToken();
                if(terminal_comand.hasMoreTokens() == false){
                    break;
                }
            }
            readInput(new BufferedReader(new FileReader(line)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public Graph getGraph() {
//		return your graph
        return graph;
    }

    public Iterator<GraphNode> solve() {
//		simply call your private DFS. If you come up with a different approach that's ok too.
//		remember to always return an Iterator or null
        try {
            Iterator<GraphNode> reverse_path = DFS(start, coins);
            Stack<GraphNode> path = new Stack<GraphNode>();
            if(reverse_path != null){
                while (reverse_path.hasNext()) { 
                    path.push(reverse_path.next());
                }
                return path.iterator();
            }    
        } catch (Exception e) {
            System.out.println("SOLVING ERROR");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Iterator<GraphNode> DFS(GraphNode go, int k) throws GraphException {
//		perform a DFS of your graph. Reduce your k which represents the remaining coins
//		start with the base case
//		remember to return null if you didn't find a path
        if(go == exit){
            return stack.iterator();
        }
        if (k <= 0) {
            return null;
        }
        go.mark(true);
        stack.push(go);

        try {
            Iterator<GraphEdge> i = graph.incidentEdges(go);
            Iterator<GraphNode> maybe_path = null;
            boolean option_marked;

            if (i.hasNext()) {
                GraphEdge option = i.next();
                option_marked = false;

                if (option.firstEndpoint() == go) {
                    if (option.secondEndpoint().isMarked()) {
                        option_marked = true;
                    }
                }

                if (option_marked == false) {
                    String label = option.getLabel();
                    if ((label.equals("corridor")) || (label.equals("door") && k - option.getType() >= 0)) {
                        maybe_path = DFS(option.secondEndpoint(), k - option.getType());
                        if (maybe_path != null) {
                            Iterator<GraphNode> path = stack.iterator();
                            clear_stack();
                            return path;
                        }
                    }
                }
            }
            go.mark(false);
            stack.pop();
        } catch (GraphException e) {
            throw e;
        }
        return null;
    }

    private void clear_stack(){
        while(stack.isEmpty() == false){
            stack.pop().mark(false);
        }
    }

    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        //		Read the values S, A, L, and k
        //		pay attention when iterating over the input.. All testing input will be correctly formatted
        //		remember to identify the starting and ending rooms
        //		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
        //		To maintain this method cleaner, you may use the private helper method insertEdge
        try {
            int S = Integer.parseInt(inputReader.readLine()); //Read unused var S
            int A = Integer.parseInt(inputReader.readLine());
            int L = Integer.parseInt(inputReader.readLine());
            int K = Integer.parseInt(inputReader.readLine());
            
            this.graph = new Graph(A * L);
            this.coins = K;
            for (int i = 0; i < L; i++) {
                String line = inputReader.readLine();

                if (i % 2 == 0) {
                    for (int j = 0; j < A; j++) {
                        if(j % 2 == 0){
                            switch (line.charAt(j)) {
                                case 's':
                                    start = graph.getNode(i * A + j);
                                    break;
                                case 'x':
                                    exit = graph.getNode(i * A + j);
                                    break;
                                default:
                                    break;
                            }
                        }else{
                            switch (line.charAt(j)) {
                                case 'w':
                                    insertEdge(i * A + j -1, i * A + j + 1, -1, "wall");
                                    break;
                                case 'c':
                                    insertEdge(i * A + j - 1, i * A + j + 1, -1, "corridor");
                                    break;
                                default:
                                    insertEdge(i * A + j - 1, i * A + j + 1, line.charAt(j)-'0', "door");
                                    break;
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < A; j++) {
                        if (j % 2 == 0) {
                            switch (line.charAt(j)) {
                                case 'w':
                                    insertEdge(i * A + j - 1, i * A + j + 1, -1, "wall");
                                    break;
                                case 'c':
                                    insertEdge(i * A + j - 1, i * A + j + 1, -1, "corridor");
                                    break;
                                default:
                                    insertEdge(i * A + j - 1, i * A + j + 1, line.charAt(j) - '0', "door");
                                    break;
                            }
                        }
                        else{
                            insertEdge(i * A + j -1, i * A + j + 1, -1, "wall");
                        }
                    }
                }

            }
            inputReader.close();
        } catch (Exception e) {
            throw e;
        }
        
    }

    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
        try {
            graph.insertEdge(graph.getNode(node1),graph.getNode(node2),linkType,label);
        } catch (GraphException e) {
            throw e;
        }
    }

}
