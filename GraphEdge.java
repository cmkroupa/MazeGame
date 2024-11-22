
public class GraphEdge {

    //	I need an origin and a destination
    final private GraphNode origin;
    final private GraphNode destination;
    private int type;
    private String label;

    public GraphEdge(GraphNode u, GraphNode v, int type, String label) {
        origin = u;
        destination = v;
        this.type = type;
        this.label = label;
    }

//	I should probably fill in the bodies of those setters and getters
    public GraphNode firstEndpoint() {
        return origin;
    }

    public GraphNode secondEndpoint() {
        return destination;
    }

    public int getType() {
        return type;
    }

    public void setype(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
