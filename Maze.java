
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
                    GraphNode node = reverse_path.next();
                    path.push(node);
                }
                return path.iterator();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Iterator<GraphNode> DFS(GraphNode go, int k) throws GraphException {
//		perform a DFS of your graph. Reduce your k which represents the remaining coins
//		start with the base case
//		remember to return null if you didn't find a path
        go.mark(true);
        stack.push(go);
        if (go == exit) {
            return stack.iterator();
        }

        try {
            Iterator<GraphEdge> i = graph.incidentEdges(go);
            boolean option_marked;
            while (i.hasNext()) {
                GraphEdge option = i.next();
                GraphNode nextNode = option.firstEndpoint();
                if (option.firstEndpoint() == go) {
                    nextNode = option.secondEndpoint();
                }


                if (nextNode.isMarked() == false) {
                    String label = option.getLabel();
                    if ((label.equals("corridor") || label.equals("door")) && k - option.getType() >= 0) {
                        Iterator<GraphNode> maybe_path = DFS(nextNode, k - option.getType());
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



    private void readInput(BufferedReader inputReader) throws IOException, GraphException {
        //		Read the values S, A, L, and k
        //		pay attention when iterating over the input.. All testing input will be correctly formatted
        //		remember to identify the starting and ending rooms
        //		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
        //		To maintain this method cleaner, you may use the private helper method insertEdge

        int S = Integer.parseInt(inputReader.readLine()); //Read unused var S
        int l = Integer.parseInt(inputReader.readLine());
        int w = Integer.parseInt(inputReader.readLine());
        int K = Integer.parseInt(inputReader.readLine());
        graph = new Graph(w * l);
        this.coins = K;

        //read text = double array
        String[][] maze_grid = new String[w*2 -1][l*2 -1];
        for (int i = 0; i < w * 2 - 1; i++) {
            String line = inputReader.readLine();
            for (int j = 0; j < l * 2 - 1; j++) {
                maze_grid[i][j] = line.charAt(j) + "";
            }
        }
        inputReader.close();

        String edge_label = "";
        int type = 0;


        //horizontal edges
        int[] current_node;

        for (int i = 0; i < w; i++) {
            int j = 0;
            while(j < l-1) {
                current_node = maze_rep(i, j);
                int[] right_edge = right_node_maze(i, j);

                switch (maze_grid[current_node[0]][current_node[1]].charAt(0)){
                    case 's':
                        start = graph.getNode(i * l + j);
                        break;
                    case 'x':
                        exit = graph.getNode(i * l + j);
                        break;
                }

                switch (maze_grid[right_edge[0]][right_edge[1]].charAt(0)) {
                    case 'w':
                        edge_label = "wall";
                        type = -1;
                        break;
                    case 'c':
                        edge_label = "corridor";
                        type = 0;
                        break;
                    default:
                        edge_label = "door";
                        type = maze_grid[right_edge[0]][right_edge[1]].charAt(0) - '0';
                        break;
                }
                current_node = maze_rep(i, j);
                switch (maze_grid[current_node[0]][current_node[1]].charAt(0)) {
                    case 's':
                        start = graph.getNode(i * l + j);
                        break;
                    case 'x':
                        exit = graph.getNode(i * l + j);
                        break;
                }
                insertEdge(i * l + j, i * l + j+1, type, edge_label);
                j++;
            }
        }

        //vertical edges
        for (int i = 0; i < w-1; i++) {
            int j = 0;
            while (j < l) {
                int[] down_edge = under_node_maze(i, j);

                switch (maze_grid[down_edge[0]][down_edge[1]].charAt(0)) {
                    case 'w':
                        edge_label = "wall";
                        type = -1;
                        break;
                    case 'c':
                        edge_label = "corridor";
                        type = 0;
                        break;
                    default:
                        edge_label = "door";
                        type = maze_grid[down_edge[0]][down_edge[1]].charAt(0) - '0';
                        break;
                }
                insertEdge(i * l + j, (i+1) * l + j, type, edge_label);
                j++;
            }
        }
    }

    private int[] maze_rep(int x, int y) {
        return new int[]{x * 2, y * 2};
    }

    private int[] right_node_maze(int x, int y) {
        int[] cords = maze_rep(x, y);
        return new int[]{cords[0], cords[1]+1};
    }

    private int[] under_node_maze(int x, int y) {
        int[] cords = maze_rep(x, y);
        return new int[]{cords[0]+1, cords[1]};
    }

    private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
        graph.insertEdge(graph.getNode(node1), graph.getNode(node2), linkType, label);
    }
}
