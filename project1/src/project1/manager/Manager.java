package project1.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import project1.PCB.Process;
import project1.PresentationShell;
import project1.RCB.*;
public class Manager {

	private ReadyList readyList;
	private Process currentProcess;
	private RCB R1;
	private RCB R2;
	private RCB R3;
	private RCB R4;
	File outFile;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter;
	
	public Manager(File outFile, FileWriter fileWriter, BufferedWriter bufferedWriter)
	{
		this.outFile = outFile;
		this.fileWriter = fileWriter;
		this.bufferedWriter = bufferedWriter;
		System.out.println("Initializing a new manager");
		this.readyList = new ReadyList();
		System.out.println("Initialized ReadyList");

		currentProcess = new Process("init",0, readyList.getListOnLevel(0));
		readyList.addToReadyList(currentProcess);
		R1 = new RCB("R1",1);
		R2 = new RCB("R2",2);
		R3 = new RCB("R3",3);
		R4 = new RCB("R2",4);		
		scheduler();
	
		//outFile = new File("/Volumes/NO NAME/13179240.txt");

				
		
		
		listAllProcesses(); //test del
		listAllResources(); //test del
	}
	
	public void create(String name, Integer priority)
	{
		System.out.println("In Manager.create(), name = " + name + " priority = " + priority.toString());
		if(priority < 1 || priority > 2)
		{
			System.out.println("PRIORITY HAS TO BE EITHER ONE OR TWO ERROR!!! ERROR!!!!");
			writeErrorToFile();
		}
		else if(doesProcessExist(name))
		{
			System.out.println("THE PROCESS ALREADY EXISTS ERROR");
			writeErrorToFile();
		}
		else
		{
			Process createdPCB = new Process(name, priority, readyList.getListOnLevel(priority), currentProcess);
			currentProcess.getCreationTree().addChildToTree(createdPCB);
			readyList.addToReadyList(createdPCB);
			readyList.printReadyList();
			scheduler();
		}
	}
	
	public void destroy(String name)
	{
		System.out.println("Manager.destroy function name = " + name);
		
		
		if(name.equals("init"))
		{
			System.out.println("YOU CANT DESTROY INIT!");
			writeErrorToFile();
		}
		else if(checkIfProcessAboveCurrentProcess(name))
		{
			System.out.println("a process cant destroy any process ABOVE it in the creation tree ERROR");
			writeErrorToFile();
		}
		else if(this.currentProcess.getPid().equals(name))
		{
			killTree(this.currentProcess);
			scheduler();
		}
		else
		{
			//it could be on the ready list or the blocked list
			
			Process processToKill = readyList.getProcess(name);
			if(processToKill == null)
			{
				//BLOCKED LIST 
				
				if(R1.getBlockedList().getRequests().get(name) != null)
				{
					processToKill = R1.getBlockedList().getProcessOffBlockedList(name);
				}
				else if(R2.getBlockedList().getRequests().get(name) != null)
				{
					processToKill = R2.getBlockedList().getProcessOffBlockedList(name);
				}
				else if(R3.getBlockedList().getRequests().get(name) != null)
				{
					processToKill = R3.getBlockedList().getProcessOffBlockedList(name);
				}
				else if(R4.getBlockedList().getRequests().get(name) != null)
				{
					processToKill = R4.getBlockedList().getProcessOffBlockedList(name);
				}
				else
				{
					System.out.println("PROCESS " + name + "DOESNT EXIST ERRORR");
					writeErrorToFile();
					return;
				}
				killTree(processToKill);
				scheduler();
			}
			
			
		}
		
	}
	
	public void killTree(Process processToKill)
	{
		
		//free all resources
		for(int i = 0; i < processToKill.getOtherResources().size(); ++i)
		{
			RCB resourceToRelease = processToKill.getOtherResources().get(i);
			Integer numUnitsToRelease = resourceToRelease.getWhoHasMe().get(processToKill);
			if(numUnitsToRelease == null)
			{
				System.out.println("Something went wrong IN NUMUNTIS TO RELEASE");
			}
			resourceToRelease.getUnitsBack(processToKill, this.readyList);
			
		}
		
		for(int i = 0; i < processToKill.getCreationTree().getChildren().size(); ++i)
		{
			Process childProcess = processToKill.getCreationTree().getChildren().get(i);
			killTree(childProcess);
		}
		
		if(this.currentProcess.getPid().equals(processToKill.getPid()))
		{
			System.out.println("IM THE CURRENTLY RUNNING PROCESS AND IM KILLING MYSELF!!");
			this.currentProcess.getStatus().setType("ready");
			currentProcess = readyList.findHighestPriorityProcess();
			readyList.removeFromReadyList(currentProcess.getPid());
			currentProcess.getStatus().setType("running");
		}
		readyList.removeFromReadyList(processToKill.getPid());
		processToKill = null;
		
			
	}
	
	public void request(String rid, Integer numUnitsWanted)
	{
		System.out.println("Manager.request function rid = " + rid + " numResourcesWanted = "
							+ numUnitsWanted.toString());
		RCB resourceWanted = returnResourceWanted(rid);
		
		if(resourceWanted == null)
		{
			System.out.println("RESOURCE DOESNT EXIST ERROR");
			writeErrorToFile();
		}
		else if(currentProcess.getPid().equals("init"))
		{
			System.out.println("INIT CANNOT REQUEST RESOURCES ERROR!!!");
			writeErrorToFile();
		}
		else if(numUnitsWanted > resourceWanted.getTotalUnits())
		{
		
			System.out.println("YOU ARE REQUESTING MORE UNITS THAN THAT RESOURCE HAS ERROR");
			writeErrorToFile();
		}
		else if( (resourceWanted.getNumUnitsProcessIsHolding(this.currentProcess) + numUnitsWanted) > resourceWanted.getTotalUnits())
		{
			//a request "req Ri j" for j units of of resource Ri cannot simply compare j against i
			//but must first add to j the number of units of Ri that the process is already holding.
			System.out.println("YOU ARE ALREADY HOLDING TOO MANY UNITS, CAN'T MAKE THAT REQUEST ERROR");
			writeErrorToFile();
		}
		else
		{
			//blocked
			if(numUnitsWanted > resourceWanted.getNumFreeUnits())
			{
				resourceWanted.getBlockedList().addProcessToBlockedList(this.currentProcess, numUnitsWanted);
				this.currentProcess.getStatus().setType("blocked");
				this.currentProcess.getStatus().setList(resourceWanted.getBlockedList().getbL());
				readyList.removeFromReadyList(this.currentProcess.getPid());
				scheduler();
			
			}	
			else
			{
				System.out.println("ALLOCATING RESOURCE!");
				//allocate the resource
				this.currentProcess.getOtherResources().add(resourceWanted);
				resourceWanted.allocateUnits(this.currentProcess, numUnitsWanted);
				scheduler();
			}

		}
	}
	
	
	public void release(String rid, Integer numUnitsToRelease)
	{
		
		System.out.println("Manager.release function rid = " + rid + " numResourcesToRelease = "
				+ numUnitsToRelease.toString());
		
		RCB resourceWanted = returnResourceWanted(rid);
		
		if(resourceWanted != null)
		{	
			if(resourceWanted.getTotalUnits() < numUnitsToRelease)
			{
				System.out.println("YOU ARE TRYING TO RELEASE MORE UNITS THAN THAT UNIT HAS ERROR!!!");
				writeErrorToFile();
			}
			else if(resourceWanted.getUnitsBack(this.currentProcess, readyList))
			{
				currentProcess.getOtherResources().remove(resourceWanted);
				scheduler();
			}
			else
			{
				System.out.println("TRYING TO RELEASE UNITS THAT HAVENT BEEN ALLOCATED ERROR");
				writeErrorToFile();
			}
		}
		else
		{
			System.out.println("ENTERED A RESOURCE THAT DOESNT EXIST ERROR");
			writeErrorToFile();
		}
	}
	
	RCB returnResourceWanted(String ridWanted)
	{
		if(ridWanted.equals("R1"))
			return R1;
		if(ridWanted.equals("R2"))
			return R2;
		if(ridWanted.equals("R3"))
			return R3;
		if(ridWanted.equals("R4"))
			return R4;
					
		return null;
	
		
	}
	public void preempt(Process processToRun)
	{
		System.out.println("In preempt function");
		currentProcess = processToRun;
		currentProcess.getStatus().setType("running");
		readyList.removeFromReadyList(currentProcess.getPid());
	}
	
	//Does round-robin on 1 level of the ReadyList
	public void timeout()
	{
		
		System.out.println("In timeout function");
		readyList.removeFromReadyList(currentProcess.getPid());
		currentProcess.getStatus().setType("ready");
		readyList.addToReadyList(currentProcess);
		scheduler();

	}

	public void scheduler()
	{
		System.out.println("In scheduler function");
		Process highestPriorityProcess = readyList.findHighestPriorityProcess();
		
		if(       currentProcess.getPriority() < highestPriorityProcess.getPriority()
				|| !("running".equals(currentProcess.getStatus().getType()))
				|| (currentProcess == null)      )
		{
			preempt(highestPriorityProcess);
		}
		
		System.out.println("Process " + currentProcess.getPid() + 
				" is the currently running process"); 
		
		try {
			bufferedWriter.write(currentProcess.getPid() + " ");
			System.out.println("WROTE TO FILE");
		} catch (IOException e) {
			System.out.println("Caught exception when trying to write to file");
		}
		
	}
	
	public ReadyList getReadyList()
	{
		return this.readyList;
	}
	
	
	public void listAllProcesses()
	{
		
		System.out.println("Listing all processes on ReadyList: ");
		readyList.printReadyList();
		
		System.out.println("Listing all processes on BlockedListt: ");
		System.out.println("R1 BlockedList: ");
		R1.getBlockedList().printBlockedList();
		
		System.out.println("R2 BlockedList: ");
		R2.getBlockedList().printBlockedList();
		
		System.out.println("R3 BlockedList: ");
		R3.getBlockedList().printBlockedList();
		
		System.out.println("R4 BlockedList: ");
		R4.getBlockedList().printBlockedList();
	}
	
	
	
	public void listAllResources()
	{
		System.out.println("Listing all Resources: ");
		System.out.println("R1: ");
		System.out.println("NumFreeUnits: " + R1.getNumFreeUnits() + " numUnitsUsed: " 
							+ R1.getNumUnitsUsed().toString());
		R1.printAllAllocatedProcesses();
		
		System.out.println("R2: ");
		System.out.println("NumFreeUnits: " + R2.getNumFreeUnits() + " numUnitsUsed: " 
				+ R2.getNumUnitsUsed().toString());
		R2.printAllAllocatedProcesses();
		
		System.out.println("R3: ");
		System.out.println("NumFreeUnits: " + R3.getNumFreeUnits() + " numUnitsUsed: " 
				+ R3.getNumUnitsUsed().toString());
		R3.printAllAllocatedProcesses();
		
		System.out.println("R4: ");
		System.out.println("NumFreeUnits: " + R4.getNumFreeUnits() + " numUnitsUsed: " 
				+ R4.getNumUnitsUsed().toString());
		R4.printAllAllocatedProcesses();
	}

	public void quit() {
		
		try{
		if (bufferedWriter != null)
		{
			bufferedWriter.close();
		}
		
		if (fileWriter != null)
		{
			fileWriter.close();
		}
		}catch(Exception E)
		{
			System.out.println("Caught Exception in quit()");
		}
	
	}
	
	public void writeErrorToFile()
	{
		try {
			bufferedWriter.write("error ");
			System.out.println("WROTE AN ERROR TO FILE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeNewLineToFile()
	{
		try {
			bufferedWriter.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	boolean checkIfProcessAboveCurrentProcess(String nameOfProcess)
	{
		Process parent = this.currentProcess.getCreationTree().getParent();
		while(parent != null)
		{
			if(parent.getPid().equals(nameOfProcess))
			{
				return true;
			}
			
			parent = parent.getCreationTree().getParent();
		}
		
		return false;
	}
	
	public boolean doesProcessExist(String name)
	{
		//check current process
		if(this.currentProcess.getPid().equals(name))
		{
			return true;
		}
		
		//check ready list
		if(readyList.getProcess(name) != null)
		{
			return true;
		}
		
		//check all blocked lists
		
		if(R1.getBlockedList().getRequests().get(name) != null)
		{
			return true;
		}
		else if(R2.getBlockedList().getRequests().get(name) != null)
		{
			return true;
		}
		else if(R3.getBlockedList().getRequests().get(name) != null)
		{
			return true;
		}
		else if(R4.getBlockedList().getRequests().get(name) != null)
		{
			return true;
		}
		
		return false;
	}
}
