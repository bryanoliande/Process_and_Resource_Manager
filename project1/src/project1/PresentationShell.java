package project1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import project1.manager.Manager;

public class PresentationShell {
	
	


	/**
	 * Reads commands from terminal or test file,
	 * invokes kernel functions (the manager),
	 * displays reply (on terminal or text file):
	 * which process is running and any errors.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Scanner userInputScanner = new Scanner(System.in);
		System.out.print("Enter input file path: ");
		
		String inFilePath = userInputScanner.nextLine();
		inFilePath.trim();
		System.out.println("inFilePath: " + inFilePath);
		File inFile = new File(inFilePath);
		
		
		String outFilePath = inFilePath.substring(0, inFilePath.length() - 10); //chop off "/input.txt"
		outFilePath = outFilePath.trim();
		outFilePath = outFilePath.concat("/13179240.txt");
		System.out.println("outFilePath: " + outFilePath);
		System.out.println("Test");
		final File outFile = new File(outFilePath);
		final FileWriter fileWriter = new FileWriter(outFile.getAbsoluteFile(), true);

		final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		Manager manager = new Manager(outFile, fileWriter, bufferedWriter);
		
		
		Scanner inScanner = null;
		try {
			inScanner = new Scanner(inFile);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't open file");
		}
		
		String userInput = "";
		String[] splitInput = {"", "", ""};
		String name;
		Integer priority;
		String resourceName;
		int numUnits;
		
		
	
	
		// /Volumes/NO NAME/input.txt
		while(!userInput.equals("quit"))
		{
			manager.listAllProcesses();
			manager.listAllResources();
			//manager.scheduler();
			System.out.print("Shell> ");
			if(inScanner.hasNextLine())
			{
				userInput = inScanner.nextLine();
				splitInput = userInput.split(" ");
			}
			//System.out.println(splitInput[0]);
			/*
			init
			cr <name> <priority>   
			de <name>
			req <resource name> <# of units>  
			rel <resource name> <# of units>  
			to */  
			if(userInput.equals(""))
			{
				manager.writeNewLineToFile();

			}
			
			if(splitInput[0].equals("init"))
			{
				manager = new Manager(outFile, fileWriter, bufferedWriter);

			}
			
			if(splitInput[0].equals("cr"))
			{
				name = splitInput[1];
				priority = Integer.parseInt(splitInput[2]);
				manager.create(name, priority);
			}
			
			if(splitInput[0].equals("de"))
			{
				name = splitInput[1];
				manager.destroy(name);

			}
			
			if(splitInput[0].equals("req"))
			{
				resourceName = splitInput[1].toUpperCase();
				numUnits = Integer.parseInt(splitInput[2]);
				manager.request(resourceName, numUnits);

			}
			
			if(splitInput[0].equals("rel"))
			{
				resourceName = splitInput[1].toUpperCase();
				numUnits = Integer.parseInt(splitInput[2]);
				manager.release(resourceName, numUnits);
			}
			
			if(splitInput[0].equals("to"))
			{
				manager.timeout();
			}
			
			//if there is no "quit" at the EOF
			if(!inScanner.hasNextLine())
			{
				userInput = "quit";
			}
			
			
			
		}
		manager.quit();
		System.out.println("OUTPUT TO GRADE WRITTEN TO: " + outFilePath);
	}

}
