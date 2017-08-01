package project1.RCB;

import java.util.HashMap;
import java.util.Set;

import project1.PCB.Process;
import project1.manager.ReadyList;

public class RCB {

	private String rid;
	private Integer numFreeUnits;
	private Integer numUnitsUsed;
	private BlockedList blockedList;
	private HashMap<Process, Integer> whoHasMe;
	
	public RCB(String rid, int numFreeUnits)
	{
		this.rid = rid;
		this.numFreeUnits = numFreeUnits;
		this.numUnitsUsed = 0;
		this.blockedList = new BlockedList();
		this.whoHasMe = new HashMap<Process, Integer>();
	}
	
	public void allocateUnits(Process processWhoRequested, Integer numUnitsRequested)
	{
		Integer numUnitsHas = whoHasMe.get(processWhoRequested);
	   if(numUnitsHas != null)
	   {
		whoHasMe.put(processWhoRequested, numUnitsRequested + numUnitsHas);
	   }
	   else
	   {
			whoHasMe.put(processWhoRequested, numUnitsRequested);
	   }
		
		numFreeUnits -= numUnitsRequested;
		numUnitsUsed += numUnitsRequested;
	}
	
	public boolean getUnitsBack(Process processWhosGivingBack, ReadyList readyList)
	{
		Integer numUnitsReleased = whoHasMe.get(processWhosGivingBack);
		whoHasMe.remove(processWhosGivingBack);
		if(numUnitsReleased != null)
		{
			numFreeUnits += numUnitsReleased;
			numUnitsUsed -= numUnitsReleased;
			System.out.println("I GOT UNITS BACK!");
			
			
			//allocate the resource to a process on the blockedlist
			//MAY NEED TO CHANGE THIS TO A LOOP TO ALLOCATE UNITS
			//TO MULTIPLE PROCESSES i.e. if R3 was being requested by
			//2 processes only requesting 1 unit each
			if(!this.blockedList.getbL().isEmpty())
			{

				Process q = this.blockedList.getbL().get(0);
				Integer numUnitsWanted = this.blockedList.getRequests().get(q.getPid());
				this.blockedList.removeProcessFromBlockedList(q);
				q.getOtherResources().add(this);
				allocateUnits(q, numUnitsWanted);
				q.getStatus().setType("ready");
				
				readyList.addToReadyList(q);
				q.getStatus().setList(readyList.getListOnLevel(q.getPriority()));	
			}
			return true;
		}
		else
		{
			System.out.println("COULDNT GET UNITS BACK FROM PROCESS! "
					+ "PROCESS WHO MESSED IT UP " + processWhosGivingBack.getPid());
			return false;

		}
	}


	public Integer getNumUnitsProcessIsHolding(Process process)
	{
		
		Integer numUnitsProcessHas = whoHasMe.get(process);
		if(numUnitsProcessHas != null)
		{
			return numUnitsProcessHas;
		}
		return 0;
	}
	
	public Integer getTotalUnits()
	{
		return (numFreeUnits + numUnitsUsed);
	}
	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public Integer getNumFreeUnits() {
		return numFreeUnits;
	}

	public void setNumFreeUnits(Integer numFreeUnits) {
		this.numFreeUnits = numFreeUnits;
	}

	public Integer getNumUnitsUsed() {
		return numUnitsUsed;
	}

	public void setNumUnitsUsed(Integer numUnitsUsed, Process processWhoRequested) 
	{
		this.numUnitsUsed = numUnitsUsed;
		
	}

	public BlockedList getBlockedList() {
		return blockedList;
	}

	public void setBlockedList(BlockedList blockedList) {
		this.blockedList = blockedList;
	}
	
	public HashMap<Process, Integer> getWhoHasMe()
	{
		return whoHasMe;
	}
	public void printAllAllocatedProcesses()
	{
		for (Process key : whoHasMe.keySet()) 
		{
		    System.out.println("Process: " + key.getPid() + 
		    		 " holds " + whoHasMe.get(key).toString() + " units of me");
		}
	}
	
}
