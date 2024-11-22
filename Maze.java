
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.temporal.TemporalAdjuster;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import org.w3c.dom.Node;

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
        stack = new Stack<GraphNode>();
        try {
            StringTokenizer terminal_comand = new StringTokenizer(inputFile, " ");
            String line = "";
            while (terminal_comand.hasMoreTokens()) {
                line = terminal_comand.nextToken();
                if (terminal_comand.hasMoreTokens() == false) {
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
            if (reverse_path != null) {
                while (reverse_path.hasNext()) {
                    path.push(reverse_path.next());
                }
                clear_stack();
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
        if (go == exit) {
            return stack.iterator();
        }
        if (k <= 0) {
            return null;
        }
        go.mark(true);
        stack.push(go);

        try {
            Iterator<GraphEdge> i = graph.incidentEdges(go);
            boolean option_marked;

            while (i.hasNext()) {
                GraphEdge option = i.next();
                option_marked = false;

                if (option.secondEndpoint().isMarked()) {
                    option_marked = true;
                }

                if (option_marked == false) {
                    String label = option.getLabel();
                    if ((label.equals("corridor")) || (label.equals("door") && k - option.getType() >= 0)) {
                        Iterator<GraphNode> maybe_path = DFS(option.secondEndpoint(), k - option.getType());
                        if (maybe_path != null) {
                            return stack.iterator();
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

    private void clear_stack() {
        while (stack.isEmpty() == false) {
            stack.pop().mark(false);
        }
    }

    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        //		Read the values S, A, L, and k
        //		pay attention when iterating over the input.. All testing input will be correctly formatted
        //		remember to identify the starting and ending rooms
        //		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
        //		To maintain this method cleaner, you may use the private helper method insertEdge

        int S = Integer.parseInt(inputReader.readLine()); //Read unused var S
        int A = Integer.parseInt(inputReader.readLine());
        int L = Integer.parseInt(inputReader.readLine());
        int K = Integer.parseInt(inputReader.readLine());

        A = A * 2 - 1; //Readjust Width
        L = L * 2 - 1;
        this.coins = K;
        graph = new Graph(A * L);

        String[][] maze_grid = new String[L][A];
        for (int i = 0; i < L; i++) {
            String line = inputReader.readLine();
            for (int j = 0; j < A; j++) {
                maze_grid[i][j] = line.charAt(j) + "";
            }
        }

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < A; j++) {
                System.out.print(maze_grid[i][j] + ' ');
            }
            System.out.println();
        }
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < A; j++) {
                System.out.print("(" + (A * i + j) + ") ");
            }
            System.out.println();
        }
        inputReader.close();

        for (int i = 0; i < maze_grid.length; i += 2) {
            for (int j = 0; j < maze_grid[i].length - 2; j += 2) {
                int horizontal_type = 0;
                String edge_label = "";
                switch (maze_grid[i][j + 1].charAt(0)) {
                    case 'w':
                        edge_label = "wall";
                        horizontal_type = -1;
                        break;
                    case 'c':
                        edge_label = "corridor";
                        horizontal_type = 0;
                        break;
                    default:
                        edge_label = "door";
                        horizontal_type = maze_grid[i][j + 1].charAt(0) - '0';
                        break;
                }

                //check for s or x 
                switch (maze_grid[i][j].charAt(0)) {
                    case 's':
                        start = graph.getNode(A * i + j);
                        break;
                    case 'x':
                        exit = graph.getNode(A * i + j);
                        break;
                }
                switch (maze_grid[i][j + 2].charAt(0)) {
                    case 's':
                        start = graph.getNode(A * i + j + 2);
                        break;
                    case 'x':
                        exit = graph.getNode(A * i + j + 2);
                        break;
                }
                insertEdge(A * i + j, A * i + j + 2, horizontal_type, edge_label);
            }
        }

        for (int i = 0; i < maze_grid.length - 2; i += 2) {
            for (int j = 0; j < maze_grid[i].length; j += 2) {
                int vertical_type = 0;
                String edge_label = "";
                switch (maze_grid[i + 1][j].charAt(0)) {
                    case 'w':
                        edge_label = "wall";
                        vertical_type = -1;
                        break;
                    case 'c':
                        edge_label = "corridor";
                        vertical_type = 0;
                        break;
                    default:
                        edge_label = "door";
                        vertical_type = maze_grid[i + 1][j].charAt(0) - '0';
                        break;
                }

                //check for s or x 
                switch (maze_grid[i][j].charAt(0)) {
                    case 's':
                        start = graph.getNode(A * i + j);
                        break;
                    case 'x':
                        exit = graph.getNode(A * i + j);
                        break;
                }
                switch (maze_grid[i + 2][j].charAt(0)) {
                    case 's':
                        start = graph.getNode(A * (i + 2) + j + 2);
                        break;
                    case 'x':
                        exit = graph.getNode(A * (i + 2) + j + 2);
                        break;
                }
                insertEdge(A * i + j, A * (i + 2) + j, vertical_type, edge_label);
            }
        }

    }

    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
        graph.insertEdge(graph.getNode(node1), graph.getNode(node2), linkType, label);
    }

    public static void main(String[] args) {
        try {
            Maze tester = new Maze("java Solve maze0.txt");
            Graph graph = tester.getGraph();
            for (int i = 1; i < 40; i++) {
                GraphNode node = graph.getNode(i);
                Iterator<GraphEdge> edges = graph.incidentEdges(node);
                if (edges.hasNext()) {
                    GraphEdge next = edges.next();
                    System.out.print(i + "-->" + next.secondEndpoint().getName());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
