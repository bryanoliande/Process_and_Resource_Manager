package project1.PCB;

import java.util.LinkedList;

//contains the status of the process and a back pointer to its corresponding
//ready or blocked list
public class Status {

	private String type;
	private LinkedList<Process> list;
	
	public Status(String type, LinkedList<Process> list)
	{
		//"ready" or "blocked" or "running"
		this.type = type;
		this.list = list;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LinkedList<Process> getList() {
		return list;
	}
	public void setList(LinkedList<Process> list) {
		this.list = list;
	}



	
}
