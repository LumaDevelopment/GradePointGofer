package net.lumadevelopment.gpg.workers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.lumadevelopment.gpg.Parcel;
import net.lumadevelopment.gpg.objects.Course;
import net.lumadevelopment.gpg.objects.CourseType;
import net.lumadevelopment.gpg.objects.LetterGrade;

public class ParcelWorker {

	/**
	 * Loads a Parcel object from a text file generated
	 * by the program. The function returns null if the 
	 * file is not there, if there's any issue reading 
	 * it, or if it's not a GradePointGofer text file.
	 * 
	 * @return Parcel created from file, or null
	 */
	public static Parcel parcelFromSave() {
		
		try {
			
			/*
			 * I'd say the next step for this system
			 * would be multiple files, like a save
			 * slot approach, but for my personal
			 * application it's not something I need or
			 * something I'm very motivated to add.
			 */
			
			File parcelFile = new File("parcel.txt");
			Scanner inFile = new Scanner(parcelFile);
			
			List<String> lines = new ArrayList<String>();
			
			/*
			 * Rather than keeping an index and
			 * reading data and creating Course
			 * objects on the spot, let's get 
			 * everything solidified into a list 
			 * and we'll figure it out from there.
			 */
			while(inFile.hasNextLine()) {
				lines.add(inFile.nextLine());
			}
			
			inFile.close();
			
			// Basic check, not a Parcel if doesn't have beginning line
			if(!lines.get(0).equals("-BOF-")) {
				return null;
			}
			
			// Also isn't a Parcel if it doesn't have the closing line
			if(!lines.get(lines.size() - 1).equals("-EOF-")) {
				return null;
			}
			
			List<Course> courses = new ArrayList<Course>();
			
			// Loop every 5 lines because that's how much a Course takes up
			for(int i = 1; i < (lines.size() - 1); i += 5) {
				
				String name = lines.get(i).substring(6);
				String semester = lines.get(i+1).substring(10);
				String grade = lines.get(i+2).substring(7);
				String type = lines.get(i+3).substring(6);
				
				Course course = new Course(name, semester, LetterGrade.valueOf(grade), CourseType.valueOf(type));
				
				courses.add(course);
				
			}
			
			Parcel p = new Parcel(courses);
			
			return p;
			
		// If the file isn't there, no need to scream about it, just return null
		} catch (FileNotFoundException e) { }
		
		return null;
	}
	
	/**
	 * Saves a Parcel object into a text file that 
	 * can be read by the program to recreate that
	 * Parcel object.
	 * 
	 * @param p - Parcel object from the program
	 */
	public static void saveParcel(Parcel p) {
		
		File parcelFile = new File("parcel.txt");
		
		// Plain overwrite. No multiple file/writing issue funny business here.
		if(parcelFile.exists()) { parcelFile.delete(); }
		
		// One of two identifying markers for GradePointGofer Parcel files
		String parcelText = "-BOF-\n";
		
		for(int i = 0; i < p.getCourses().size(); i++) {
			
			parcelText += "Name: " + p.getCourses().get(i).courseName() + "\n"
					+ "Semester: " + p.getCourses().get(i).semester() + "\n"
					+ "Grade: " + p.getCourses().get(i).grade().toString() + "\n"
					+ "Type: " + p.getCourses().get(i).type().toString() + "\n"
					+ "-----\n";
			
		}
		
		// Identifying marker two
		parcelText += "-EOF-";
		
		try {
			
			PrintWriter outFile = new PrintWriter(parcelFile);
			outFile.print(parcelText);
			outFile.close();
			
		} catch (FileNotFoundException e) {
			
			/*
			 * Honestly I'm not 100% sure how a 
			 * FileNotFoundException would be called 
			 * for a file write, but that's why I've 
			 * kept the stack trace: Just in case it 
			 * is called I'll find out why and resolve it.
			 */
			
			e.printStackTrace();
		}
		
	}
	
}
