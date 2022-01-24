package net.lumadevelopment.gpg;

import java.util.Scanner;

import net.lumadevelopment.gpg.workers.ParcelWorker;
import net.lumadevelopment.gpg.workers.TerminalWorker;

public class Gofer {
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		// gofer the deliverer, not the animal
		System.out.println("Welcome to the GradePointGofer!");
		
		String startPoint = "n";
		
		while(startPoint.equals("n")) {
			
			// My main intention for making this was making grades savable
			System.out.print("Would you like to (c)ontinue from your save or start (f)resh (c/f)? ");
			
			startPoint = scan.next();
			
			if(!(startPoint.equalsIgnoreCase("c") || startPoint.equalsIgnoreCase("f"))) {
				startPoint = "n";
				System.out.println("Invalid input! Use 'c' for continue or 'f' for fresh!");
			}
			
		}
		
		// parcel, package of courses
		// I was trying to fit with the gofer theme
		Parcel p;
		
		if(startPoint.equals("c")) {
			
			p = ParcelWorker.parcelFromSave();
			
			/*
			 * parcelFromSave() returns null if there's any
			 * issue with the file's formatting or reading
			 * the file. In that case, we don't want a null
			 * Parcel, we want a new one
			 */
			if(p == null) {
				System.out.println("Invalid save/file not found! Creating new Parcel...");
				p = new Parcel();
			}
			
		} else {
			
			// Starting fresh, initialize new Parcel
			p = new Parcel();
			
		}
		
		/*
		 * The Terminal is the core of the program.
		 * It allows the adding, removal, listing,
		 * and grade calculation of classes.
		 */
		System.out.println("Welcome to the Terminal!");
		
		String cmd = "x";
		
		// To clear out the issues caused by .next()
		scan.nextLine();
		
		while(cmd.equals("x")) {
			
			System.out.print("> ");
			cmd = scan.nextLine();
			
			if(cmd.equals("help")) {
				
				System.out.println("\nGradePointGofer");
				System.out.println("add <name> <semester> <grade> <type> - Add course. Grades are letter grades. Course Types: (A)P, (C)ollege, (R)egular.");
				System.out.println("remove <name> <semester> - Remove course.");
				System.out.println("list - List courses alphabetically and categorized by semester.");
				System.out.println("freelist [semester/years] - List courses alphabetically, optionally listing only courses from a certain semester or school year.");
				System.out.println("gpa [weighted] [semester/years] - Calculate GPA, optionally weighted, optionally only for one semester or school year.");
				System.out.println("exit - Exits the program.\n");
				
				cmd = "x";
				
			} else if(cmd.length() > 3 && cmd.substring(0, 4).equalsIgnoreCase("list")) {
				
				System.out.println();
				TerminalWorker.listCommand(p);
				cmd = "x";
				
			} else if(cmd.length() > 3 && cmd.substring(0, 4).equalsIgnoreCase("exit")) {
				
				System.out.print("Do you want to save your Parcel to a file? If so, respond 'yes': ");
				String response = scan.next();
				
				if(response.equalsIgnoreCase("yes")) {
					ParcelWorker.saveParcel(p);
					System.out.println("Parcel saved!");
				}
				
				System.out.println("Goodbye!");
				cmd = "y";
				scan.close();
				
			} else if(cmd.length() > 2 && cmd.substring(0, 3).equalsIgnoreCase("gpa")) {
				
				System.out.println();
				TerminalWorker.gpaCommand(p, cmd);
				System.out.println();
				cmd = "x";
				
			} else if(cmd.length() > 7 && cmd.substring(0, 8).equalsIgnoreCase("freelist")) {
				
				System.out.println();
				TerminalWorker.freeListCommand(p, cmd);
				System.out.println();
				cmd = "x";
				
			} else if(cmd.length() > 2 && cmd.substring(0, 3).equalsIgnoreCase("add")) {
				
				System.out.println();
				p = TerminalWorker.addCommand(p, cmd);
				System.out.println();
				cmd = "x";
				
			} else if(cmd.length() > 5 && cmd.substring(0, 6).equalsIgnoreCase("remove")) {
				
				System.out.println();
				p = TerminalWorker.removeCommand(p, cmd);
				System.out.println();
				cmd = "x";
				
			} else {
				
				System.out.println("Unknown command! Use 'help' and try again...");
				cmd = "x";
				
			}
			
		}
		
	}

}
