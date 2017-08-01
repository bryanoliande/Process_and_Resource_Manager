package project1.PCB;

import java.util.LinkedList;

import project1.RCB.RCB;

//represents a single process
public class Process {

	//process id
	private String pid;
	
	//linked list of RCB pointers, each is a result of a request
	private LinkedList<RCB> otherResources;
	
	//contains the status of the process and a back pointer to its corresponding
	//ready or blocked list
	private Status status;
	
	//contains a PCB* to the process's parent and a linked list of PCB pointers 
	//which represent its children
	private CreationTree creationTree;
	
	//the priority level of the process - either 1 or 2
	private Integer priority;

	public Process(String pid, Integer priority, LinkedList<Process> listOnRL)
	{
		System.out.println("PCB parameterized ctor init");

		this.pid = pid;
		this.priority = priority;
		this.otherResources = new LinkedList<RCB>();
		this.status = new Status("running", listOnRL);
		this.creationTree = new CreationTree(null);
	}
	public Process(String pid, Integer priority, LinkedList<Process> listOnRL, Process parent)
	{
		System.out.println("PCB parameterized ctor");

		this.pid = pid;
		this.priority = priority;
		this.otherResources = new LinkedList<RCB>();
		this.status = new Status("ready", listOnRL);
		this.creationTree = new CreationTree(parent);
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public LinkedList<RCB> getOtherResources() {
		return otherResources;
	}

	public void setOtherResources(LinkedList<RCB> otherResources) {
		this.otherResources = otherResources;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CreationTree getCreationTree() {
		return creationTree;
	}

	public void setCreationTree(CreationTree creationTree) {
		this.creationTree = creationTree;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
	
}
