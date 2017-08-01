package project1.PCB;

import java.util.LinkedList;

//contains a PCB* to the process's parent and a linked list of PCB pointers 
//which represent its children
public class CreationTree {

	private Process parent;
	private LinkedList<Process> children;
	
	public CreationTree(Process parent)
	{
		this.parent = parent;
		this.children = new LinkedList<Process>();
	}
	public Process getParent() {
		return parent;
	}
	public void setParent(Process parent) {
		this.parent = parent;
	}
	public LinkedList<Process> getChildren() {
		return children;
	}
	public void setChildren(LinkedList<Process> children) {
		this.children = children;
	}
	
	public void addChildToTree(Process child)
	{
		children.add(child);
	}
	
	
	
	
	
}
