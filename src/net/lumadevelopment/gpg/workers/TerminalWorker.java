package net.lumadevelopment.gpg.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lumadevelopment.gpg.Parcel;
import net.lumadevelopment.gpg.objects.Course;
import net.lumadevelopment.gpg.objects.CourseType;
import net.lumadevelopment.gpg.objects.LetterGrade;

public class TerminalWorker {

	/**
	 * Lists all of the loaded courses, ordered 
	 * chronologically by semester. Courses within 
	 * the semester are sorted alphabetically.
	 * 
	 * @param p - Parcel
	 */
	public static void listCommand(Parcel p) {
		
		HashMap<String, List<Course>> list = new HashMap<String, List<Course>>();
		
		list = p.listCommandData();
		
		/*
		 * Here, unfortunately, we have to sort the keyset. 
		 * While I'd prefer to have listCommandData() provide 
		 * a sorted List<string> or something like that, the 
		 * implementation is already crowded as is so went with this.
		 */
		
		List<String> keySet = new ArrayList<>(list.keySet());
		
		/*
		 * Duplicate code from Parcel.java. Maybe I should 
		 * designate it to a function, but I don't know 
		 * where I'd put it. Maybe I should make a 
		 * UtilityWorker.java?
		 */
		
		int max_semester = Integer.MIN_VALUE;
		int min_semester = Integer.MAX_VALUE;
		
		for(String semester : keySet) {
			
			int semesterNumber = CourseWorker.getSemesterNumber(semester);
			
			if(semesterNumber < min_semester) {
				min_semester = semesterNumber;
			}
			
			if(semesterNumber > max_semester) {
				max_semester = semesterNumber;
			}
			
		}
		
		for(int i = min_semester; i <= max_semester; i++) {
			
			String semester = CourseWorker.getSemesterFromNumber(i);
			
			if(!keySet.contains(semester)) {
				continue;
			}
			
			System.out.println(semester);
			System.out.println("Course                                     Grade   Credits");
			System.out.println("----------------------------------------------------------");
			
			for(Course course : list.get(semester)) {
				
				System.out.printf("%-40s     %1s       %3.1f\n", course.courseName(), course.grade().toString(), CourseWorker.getCredits(course));
				
			}
			
			System.out.println();
			
		}
		
	}
	
	/**
	 * Prints the GPA for all courses or a subset of 
	 * courses in a Parcel, optionally weighted.
	 * 
	 * @param p - Parcel object
	 * @param cmd - Command text
	 */
	public static void gpaCommand(Parcel p, String cmd) {
		
		boolean weighted = false;
		String target = "n/a";
		
		/*
		 * *sigh*
		 * This is a monster and a lesson learned.
		 * In this program I don't often verify input.
		 * You can mess up the program by putting in a
		 * semester wrong, a year wrong, and a 1000 other 
		 * things. However, for some reason, I decided
		 * to verify super hard on command arguments, to 
		 * guide a user if they're using the command wrong.
		 * 
		 * However, I went halfway with verification, so now, 
		 * I have this giant mess of a text block which 
		 * perfectly identifies if the arguments are being 
		 * used correctly, but doesn't bother to verify if 
		 * those arguments themselves are usable data!
		 * 
		 * Might remove.
		 */
		
		if(cmd.contains(" ")) {
			
			// Longest command would be ex. gpa weighted Spring 2020
			if(cmd.split(" ").length > 4) {
				System.out.println("Invalid command use! Please reference the 'help' command!");
				return;
			}
			
			if(cmd.split(" ").length == 2) {
				
				// Either the GPA is weighted or restricted to a school year
				if(cmd.split(" ")[1].equalsIgnoreCase("weighted")) {
					weighted = true;
				} else if (cmd.contains("-")) {
					target = cmd.split(" ")[1];
				} else {
					System.out.println("Invalid command use! Please reference the 'help' command!");
					return;
				}
				
			} else if(cmd.split(" ").length == 3) {
				
				// Either the GPA is weighted and restricted to a school year
				// or
				// The GPA is restricted to a semester
				if(cmd.split(" ")[1].equalsIgnoreCase("weighted")) {
					weighted = true;
					target = cmd.split(" ")[2];
				} else {
					target = cmd.split(" ")[1] + " " + cmd.split(" ")[2];
				}
				
			} else if(cmd.split(" ").length == 4) {
				
				// The GPA must be weighted and restricted to a semester
				if(!cmd.split(" ")[1].equalsIgnoreCase("weighted")) {
					System.out.println("Invalid command use! Please reference the 'help' command!");
					return;
				} else {
					weighted = true;
				}
				
				target = cmd.split(" ")[2] + " " + cmd.split(" ")[3]; 
				
			}
			
		}
		
		List<Course> courses = new ArrayList<Course>();
		
		// '-' means school year
		if(target.contains("-")) {
			courses = p.getBySchoolYear(target);
		// ' ' means semester
		} else if(target.contains(" ")) {
			courses = p.getBySemester(target);
		// means 'n/a', so all courses
		} else {
			courses = p.getCourses();
		}
		
		/*
		 * Ironically, the calculation is designated to a 
		 * different function and is by far the shortest 
		 * portion of this command.
		 */
		double gpa = CourseWorker.getGPA(courses, weighted);
		
		// Pretty string!
		String message = "The ";
		
		if(weighted) {
			message += "weighted";
		} else {
			message += "unweighted";
		}
		
		message += " GPA for ";
		
		if(target.equals("n/a")) {
			message += "all semesters";
		} else {
			message += target;
		}
		
		message += " is " + gpa;
		
		System.out.println(message);
		
	}
	
	/**
	 * Lists all courses or courses in a school  
	 * year/semester without any categorization. 
	 * The courses are simply printed in 
	 * alphabetic order.
	 * 
	 * @param p - Parcel object
	 * @param cmd - Command text
	 */
	public static void freeListCommand(Parcel p, String cmd) {
		
		String target = "n/a";
		
		// The function operates just fine without arguments, but are there any?
		if(cmd.contains(" ")) {
			
			// Target is a school year
			if(cmd.contains("-")) {
				
				if(cmd.split(" ").length != 2) {
					System.out.println("Invalid command use! Please reference the 'help' command!");
					return;
				}
				
				target = cmd.split(" ")[1];
				
			// Target is a semester
			} else {
				
				if(cmd.split(" ").length != 3) {
					System.out.println("Invalid command use! Please reference the 'help' command!");
					return;
				}
				
				target = cmd.split(" ")[1] + " " + cmd.split(" ")[2];
				
			}
			
		}
		
		List<Course> courses;
		
		// see gpaCommand()
		if(target.contains(" ")) {
			courses = p.getBySemester(target);
		} else if(target.contains("-")) {
			courses = p.getBySchoolYear(target);
		} else {
			courses = p.getCourses();
		}
		
		courses = CourseWorker.sortByName(courses);
		
		System.out.println("Course                                     Grade   Credits");
		System.out.println("----------------------------------------------------------");
		
		for(Course course : courses) {
			System.out.printf("%-40s     %1s       %3.1f\n", course.courseName(), course.grade().toString(), CourseWorker.getCredits(course));
		}
		
		return;
		
	}
	
	/**
	 * Adds course to Parcel through single command 
	 * instead of long Q/A process.
	 * 
	 * @param p - Parcel object
	 * @param cmd - Command text
	 * @return Parcel with course added
	 */
	public static Parcel addCommand(Parcel p, String cmd) {
		
		if(!cmd.contains(" ")) {
			System.out.println("Invalid usage! Reference the 'help' command.");
			return p;
		}
		
		String name = "";
		String semester = "";
		LetterGrade grade = null;
		CourseType type = null;
		
		/**
		 * So I'm actually really happy with this 
		 * implementation. The problem at hand was:
		 * 
		 * "How am I going to know were the name ends 
		 * and everything else starts?"
		 *  
		 * However, semester is always two space-separated 
		 * strings, and grade and type are the same, just 
		 * one space-separated string. 
		 * 
		 * So, if we count backwards, depending on how many 
		 * spaces have been counted backwards, we can know 
		 * what argument we're looking at.
		 */
		
		for(int i = (cmd.split(" ").length - 1); i > 0; i--) {
			
			int forwardIndex = cmd.split(" ").length - 1 - i;
			
			if(forwardIndex == 0) {
				
				switch(cmd.split(" ")[i].toUpperCase()) {
				case "A":
					type = CourseType.AP;
					break;
				case "C":
					type = CourseType.COLLEGE;
					break;
				case "R":
					type = CourseType.REGULAR;
					break;
				}
				
			} else if(forwardIndex == 1) {
				//TODO try-catch. woops!
				grade = LetterGrade.valueOf(cmd.split(" ")[i]);
			} else if(forwardIndex == 2) {
				semester = " " + cmd.split(" ")[i];
			} else if(forwardIndex == 3) {
				semester = cmd.split(" ")[i] + semester;
			} else {
				name = cmd.split(" ")[i] + " " + name;
			}
			
		}
		
		name = name.trim();
		
		Course course = new Course(name, semester, grade, type);
		List<Course> courses = p.getCourses();
		courses.add(course);
		p.setCourses(courses);
		
		System.out.println("Course added!");
		
		return p;
		
	}
	
	/**
	 * Removes command from Parcel. Based on the 
	 * implementation that full year courses are 
	 * split into two one-semester courses, semester 
	 * has to be specified.
	 * 
	 * @param p - Parcel object
	 * @param cmd - Command text
	 * @return Parcel with course removed.
	 */
	public static Parcel removeCommand(Parcel p, String cmd) {
		
		if(!cmd.contains(" ")) {
			System.out.println("Invalid usage! Reference the 'help' command.");
			return p;
		}
		
		String name = "";
		String semester = "";
		
		/**
		 * Similar system to that used in addCommand(), 
		 * just substantially less cool because there's 
		 * less fun looking fancy code.
		 */
		
		for(int i = (cmd.split(" ").length - 1); i > 0; i--) {
			
			int forwardIndex = cmd.split(" ").length - 1 - i;
			
			if(forwardIndex == 0) {
				semester = " " + cmd.split(" ")[i];
			} else if(forwardIndex == 1) {
				semester = cmd.split(" ")[i] + semester;
			} else {
				name = cmd.split(" ")[i] + " " + name;
			}
			
		}
		
		name = name.trim();
		Course returnCourse = null;
		
		for(Course course : p.getCourses()) {
			
			if(course.courseName().equals(name) && course.semester().equals(semester)) {
				
				returnCourse = course;
				
				/*
				 * Stops at one course. If there's multiple 
				 * courses in one semester with the same 
				 * name, the issue is beyond the scope of 
				 * the remove command, presumably.
				 * 
				 * Assuming this is an actual issue, I'd 
				 * advise saving the Parcel to a file and 
				 * editing it directly.
				 */
				
				break;
				
			}
			
		}
		
		if(returnCourse == null) {
			
			System.out.println("No course was found, so none was deleted");
			return p;
			
		} else {
			
			List<Course> courses = p.getCourses();
			courses.remove(returnCourse);
			p.setCourses(courses);
			
			System.out.println("The course has been removed.");
			return p;
			
		}
	}
	
}
