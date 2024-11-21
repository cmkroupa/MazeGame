
import java.util.ArrayList;
import java.util.Iterator;

public class Graph implements GraphADT {

    //	Create an adjacency list or an adjacency matrix, a list is probably easier
    final private GraphNode[] list;
    final private ArrayList<GraphEdge>[] edge;

    public Graph(int n) {
        //		initialize your representation with empty adjacency lists
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

        GraphEdge newEdge = new GraphEdge(nodeu, nodev, type, label);
        edge[nodeu.getName()].add(newEdge);
        edge[nodev.getName()].add(newEdge);
    }

    @Override
    public GraphNode getNode(int u) throws GraphException {
        //		Return the node with the appropriate name
        return list[u];
    }

    @Override
    public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
//		Select from your adjacency list the appropriate Node and return an iterator over the collection.
//		Usually a call to .iterator() should work, unless you do something really exotic
        try {
            return edge[u.getName()].isEmpty() == false ? edge[u.getName()].iterator() : null;
        } catch (Exception e) {
            throw new GraphException("Node not in Node List");
        }

    }

    @Override
    public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
//		check if those nodes exist, then check if they have edges, then who has the least number of edges.
//		find the appropriate edge and return it, if no such edge exists remember to return null 
//		there are faster ways too ;)

        Iterator<GraphEdge> i;
        try {
            if (edge[u.getName()].size() >= edge[v.getName()].size()) {
                i = edge[u.getName()].iterator();
            } else {
                i = edge[v.getName()].iterator();
            }
        } catch (Exception e) {
            throw new GraphException("Node not in Edge List");
        }

        while (i.hasNext()) {
            GraphEdge e = i.next();
            if (e.firstEndpoint().getName() == u.getName() && e.secondEndpoint().getName() == v.getName()) {
                return e;
            }
            if (e.firstEndpoint().getName() == v.getName() && e.secondEndpoint().getName() == u.getName()) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
        return getEdge(u, v) != null;
    }

}
