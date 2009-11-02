package am.app.feedback.measures;

import java.util.ArrayList;

public class Pattern  {
	private int length;
	private ArrayList<Edge> edgeSequence;
	private static int lastVisit;

	public Pattern(int len, ArrayList<Edge> edgeSeq, Pattern p){
		
		edgeSequence = new ArrayList<Edge>();
		if(p != null){
			for(Edge e: p.edgeSequence){
				edgeSequence.add(e);
			}
		}
		length = len;
		edgeSequence.add( edgeSeq.get(0) );
	}
	
	public Pattern(Pattern p){
		length = p.length;
		edgeSequence = new ArrayList<Edge>();
		for(Edge e: p.edgeSequence){
			edgeSequence.add(e);
		}
	}
	
	public String toString(){
		String out = "Length: " + length + ", EdgeSequence: ";
		for(Edge e : edgeSequence){
			out += e.getSourceNode().getLocalName();
			out += ", ";
			out += e.getTargetNode().getLocalName();
			out += " -- ";
		}
		return out;
	}
	
	public boolean equals(Pattern p){
		Edge anEdge, nextEdge;
		if(this.length != p.length){
			return false;
		}
		else{
			for(int i = 0; i < edgeSequence.size(); i++){
				anEdge = edgeSequence.get(i);
				nextEdge = p.getEdgeAtIndex(i);
				if(anEdge.getSourceVisit() == nextEdge.getSourceVisit()
						&& anEdge.getTargetVisit() == nextEdge.getTargetVisit()
						&& anEdge.getSourceNode().getIndex() == nextEdge.getSourceNode().getIndex()
						&& anEdge.getTargetNode().getIndex() == nextEdge.getTargetNode().getIndex()
						)
						{
							return true;
						}
			}
		}
		return false;
	}
	
	public int getLength()
	{
		return length;
	}
	
	public void setLength(int len)
	{
		length = len;
	}
	
	public void setEdgeSequence(ArrayList<Edge> edgeSeq)
	{
		edgeSequence = edgeSeq;
	}
	
	public ArrayList<Edge> getEdgeSequence()
	{
		return edgeSequence;
	}
	
	public Edge getEdgeAtIndex(int i)
	{
		return edgeSequence.get(i);
	}


	public void setLastVisit(int lastVisit) {
		this.lastVisit = lastVisit;
	}


	public int getLastVisit() {
		return lastVisit;
	}
}
