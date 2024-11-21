
public class GraphNode {

	//	I need 2 variables, name and mark
	final private int name;
	private boolean mark;
	
	public GraphNode(int name) {
		//		It's probably worth it to do something with that name argument
		this.name = name;
		mark = false;
	}

	
//	setters and getters, should be fun
	public void mark(boolean mark) {
		this.mark = mark;
	}
	
	public boolean isMarked() {
		return mark;
	}
	
	public int getName() {
		return name;
	}
	
}
