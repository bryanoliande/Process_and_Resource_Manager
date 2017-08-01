package project1.manager;
import java.util.ArrayList;
import java.util.LinkedList;

import project1.PCB.Process;

public class ReadyList {
		
	private ArrayList<LinkedList<Process>> rL;
	private LinkedList<Process> priorityZeroList;
	private LinkedList<Process> priorityOneList;
	private LinkedList<Process> priorityTwoList;
	
	//Highest to lowest priorities: 2, 1, 0
	public ReadyList()
	{
		System.out.println("ReadyList default ctor");
		rL = new ArrayList<LinkedList<Process>>();
		priorityZeroList = new LinkedList<Process>();
		priorityOneList = new LinkedList<Process>();
		priorityTwoList = new LinkedList<Process>();
		
		rL.add(priorityTwoList);
		rL.add(priorityOneList);
		rL.add(priorityZeroList);

	}


	//add the PCB to the linked list on the ready list corresponding to its priority
	public void addToReadyList(Process pcbToAdd)
	{
		Integer priority = pcbToAdd.getPriority();
		rL.get(priority).add(pcbToAdd);
	}
	
	public void removeFromReadyList(String nameOfPCBToRemove)
	{
		for(int i = 2; i >= 0; i--)
		{
			for(int j = 0; j < rL.get(i).size(); j++)
			{
				if(nameOfPCBToRemove.equals(rL.get(i).get(j).getPid()))
				{
					Process pcbToRemove = rL.get(i).get(j);
					if(nameOfPCBToRemove.equals("init"))
					{
						System.out.println("NOT REMOVING INIT OFF READY LIST!!!");
					}
					else
					{
						rL.get(i).remove(pcbToRemove);
					}
				}
			}
		}
					
	}
	
	public LinkedList<Process> getListOnLevel(Integer level)
	{
		if(level.equals(2)){return priorityTwoList;}
		if(level.equals(1)){return priorityOneList;}
		return priorityZeroList;
	}
	
	public Process findHighestPriorityProcess()
	{
		Process pcb = null;
		
		System.out.print("In findHighestPriority Process(), ");
		for(int i = 2; i >= 0; i--)
		{
			for(int j = 0; j < rL.get(i).size(); j++)
			{
				System.out.println("Encountered " + rL.get(i).get(j).getPid());
				pcb = rL.get(i).get(j);
				break;
			}
			if(pcb != null)
			{
				break;
			}
		}
		
		System.out.println("Returning: " + pcb.getPid());
		return pcb; 
	}
	
	public Process getProcess(String name)
	{
		Process pcb = null;
		
		System.out.print("In getProcess(), ");
		for(int i = 2; i >= 0; i--)
		{
			for(int j = 0; j < rL.get(i).size(); j++)
			{
				if(rL.get(i).get(j).getPid().equals(name))
				{
				pcb = rL.get(i).get(j);
				break;
				}
			}
		
		}
		if(pcb != null)
		{
			System.out.println("Returning: " + pcb.getPid());
		}
		else
		{
			System.out.println("Process " + name + " is not on the readylist");
		}
		return pcb;
	}
	public void printReadyList()
	{
		for(int i = 2; i >= 0; i--)
		{
			System.out.print("Level: " + i + ": ");
			for(int j = 0; j < rL.get(i).size(); j++)
			{
				System.out.print(rL.get(i).get(j).getPid() + " ");
			}
			System.out.println("");
		}
	}

	
}

