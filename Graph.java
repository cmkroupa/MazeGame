
import java.util.Iterator;
import java.util.LinkedList;

public class Graph implements GraphADT {

    //	Create an adjacency list or an adjacency matrix, a list is probably easier
    final private GraphNode[] list;
    final private LinkedList<GraphEdge>[] edge;

    public Graph(int n) {
        //		initialize your representation with empty adjacency lists
        list = new GraphNode[n+1];
        edge = new LinkedList[n+1];

        for (int i = 0; i < n; i++) {
            list[i] = new GraphNode(i);
            edge[i] = new LinkedList<>();
        }
    }

    @Override
    public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
//		create and insert the edge
//		REMEMBER, an edge is accessible from both endpoints, so make sure you add it as an edge for both end nodes		
        if (!nodes_in_graph(nodeu, nodev)) {
            throw new GraphException("insertEdge: Nodes not in list");
        }
        try {
            getEdge(nodeu, nodev);
        } catch (GraphException e) {
            GraphEdge newEdge = new GraphEdge(nodeu, nodev, type, label);
            edge[nodeu.getName()].add(newEdge);
            edge[nodev.getName()].add(newEdge);
            return;
        }
        throw new GraphException("insertEdge: Edge already exists");
        
    }

    @Override
    public GraphNode getNode(int u) throws GraphException {
        //		Return the node with the appropriate name
        if (u < 0 || u > list.length) {
            throw new GraphException("U out of Bounds");
        }
        return list[u];
    }

    @Override
    public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
//		check if nodes in graph, then return iterator or null
        if (!nodes_in_graph(u, u)) {
            throw new GraphException("incidentEdges: NODES NOT IN GRAPH");
        }

        if (edge[u.getName()].isEmpty()) {
            return null;
        }
        return edge[u.getName()].iterator();
    }

    @Override
    public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
//		check if those nodes exist, then check if they have edges, then who has the least number of edges.
//		find the appropriate edge and return it, if no such edge exists remember to return null 
//		there are faster ways too ;)

        //check if u and v are the proper nodes in the list
        if (!nodes_in_graph(u, v)) {
            throw new GraphException("getEdge: NODES NOT IN GRAPH");
        }

        //find which is smaller and get its iterator for edges
        Iterator<GraphEdge> i;
        i = incidentEdges(u);
        if (edge[u.getName()].size() > edge[v.getName()].size()) {
            i = incidentEdges(v);
        }
        if (i != null) {
            while (i.hasNext()) { //while iterator has next pop and see if its edge is the target edge
                GraphEdge possible_edge = i.next();
                if (possible_edge.firstEndpoint() == u && possible_edge.secondEndpoint() == v) {
                    return possible_edge;
                }
                if (possible_edge.firstEndpoint() == v && possible_edge.secondEndpoint() == u) {
                    return possible_edge;
                }
            }
        }
        throw new GraphException("getEdge: NO EDGE EXISTS");

    }

    @Override
    public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
        //get edge, if it doesnt throw an exception return true
        try {
            getEdge(u, v);
            return true;
        } catch (GraphException e) {
        }
        return false;
    }

    //make sure nodes in graph
    private boolean nodes_in_graph(GraphNode u, GraphNode v) throws GraphException {
        return ((getNode(u.getName()) == u) || (getNode(v.getName()) == v));
    }

}
