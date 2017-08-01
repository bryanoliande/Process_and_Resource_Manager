package project1.RCB;

import java.util.HashMap;
import java.util.LinkedList;

import project1.PCB.Process;

public class BlockedList {

	//the blocked list itself - each resource has its own blockedlist
	private LinkedList<Process> bL;
	
	//Maps the name of the proccesses on the blockedList 
	//to how many units of the resource they requested
	private HashMap<String,Integer> requests;

	public BlockedList()
	{
		this.bL = new LinkedList<Process>();
		requests = new HashMap<String,Integer>();
	}
	
	public void addProcessToBlockedList(Process pcbToAdd, Integer numUnitsWanted)
	{
		this.bL.add(pcbToAdd);
		requests.put(pcbToAdd.getPid(), numUnitsWanted);
		
	}
	
	public void removeProcessFromBlockedList(Process pcbToRemove)
	{
		bL.remove(pcbToRemove);
		requests.remove(pcbToRemove.getPid());
	}
	
	public Process getProcessOffBlockedList(String name)
	{
		for(int i = 0; i < bL.size(); ++i)
		{
			if(bL.get(i).getPid().equals(name))
			{
				return bL.get(i);
			}
		}
		return null;
	}
	
	public void printBlockedList()
	{
		
	
		for (String name: requests.keySet())
		{

            String key = name.toString();
            String value = requests.get(key).toString();  
            System.out.println(key + " is requesting " + value + " units");  
		}
		
	}
	
	public LinkedList<Process> getbL() {
		return bL;
	}

	public void setbL(LinkedList<Process> bL) {
		this.bL = bL;
	}

	public HashMap<String, Integer> getRequests() {
		return requests;
	}

	public void setRequests(HashMap<String, Integer> requests) {
		this.requests = requests;
	}
	
	
}
