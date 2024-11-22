
import java.util.ArrayList;
import java.util.Iterator;

public class Graph implements GraphADT {

    //	Create an adjacency list or an adjacency matrix, a list is probably easier
    final private GraphNode[] list;
    final private ArrayList<GraphEdge>[] edge;

    public Graph(int n) {
        //		initialize your representation with empty adjacency lists
        n++;
        list = new GraphNode[n];
        edge = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            list[i] = new GraphNode(i);
            edge[i] = new ArrayList<>();
        }
    }

    @Override
    public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {

        //		create and insert the edge
        //		REMEMBER, an edge is accessible from both endpoints, so make sure you add it as an edge for both end nodes		
        boolean state = true;

        try {
            getEdge(nodeu, nodev);
            state = false;
        } catch (GraphException e) {
        }
        if (state) {
            if (getNode(nodeu.getName()) != nodeu && getNode(nodev.getName()) != nodev) {
                throw new GraphException("Node not in list");
            }
            edge[nodeu.getName()].add(new GraphEdge(nodeu, nodev, type, label));
            edge[nodev.getName()].add(new GraphEdge(nodev, nodeu, type, label));
        } else {
            throw new GraphException("Edge already in list");
        }

    }

    @Override
    public GraphNode getNode(int u) throws GraphException {
        //		Return the node with the appropriate name
        try {
            return list[u];
        } catch (IndexOutOfBoundsException e) {
            throw new GraphException("getNode Error");
        }
    }

    @Override
    public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
//		Select from your adjacency list the appropriate Node and return an iterator over the collection.
//		Usually a call to .iterator() should work, unless you do something really exotic
        try {
            return edge[u.getName()].isEmpty() == false ? edge[u.getName()].iterator() : null;
        } catch (IndexOutOfBoundsException e) {
            throw new GraphException("incidentEdges Error");
        }

    }

    @Override
    public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
//		check if those nodes exist, then check if they have edges, then who has the least number of edges.
//		find the appropriate edge and return it, if no such edge exists remember to return null 
//		there are faster ways too ;)

        Iterator<GraphEdge> i;

        if (edge[u.getName()].size() >= edge[v.getName()].size()) {
            i = edge[u.getName()].iterator();
        } else {
            i = edge[v.getName()].iterator();
        }

        while (i.hasNext()) {
            GraphEdge e = i.next();
            if (e.firstEndpoint() == u && e.secondEndpoint() == v) {
                return e;
            }
            if (e.firstEndpoint() == v && e.secondEndpoint() == u) {
                return e;
            }
        }
        throw new GraphException("Edge DNE");
    }

    @Override
    public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
        return getEdge(u, v) != null;
    }

}
