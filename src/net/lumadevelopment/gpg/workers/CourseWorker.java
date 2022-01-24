package net.lumadevelopment.gpg.workers;

import java.util.List;

import net.lumadevelopment.gpg.objects.Course;
import net.lumadevelopment.gpg.objects.CourseType;
import net.lumadevelopment.gpg.objects.LetterGrade;

public class CourseWorker {

	public static double getCredits(Course course) {
		
		double credits = 0.5;
		
		if(course.type().equals(CourseType.COLLEGE)) {
			credits *= 2;
		}
		
		/*
		 * Hacky workaround. I need to add
		 * a system for custom credit weight
		 * for classes. Unfortunately, such an
		 * effort will likely be monumental.
		 */
		//TODO add actual credit implementation
		if(course.courseName().contains("Calculus")) {
			credits = 1.5;
		}
		
		return credits;
		
	}
	
	public static double getGradeNum(Course course, boolean weighted) {
		
		LetterGrade grade = course.grade();
		double num = 0;
		
		switch (grade) {
		case A:
			num = 4;
			break;
		case B:
			num = 3;
			break;
		case C:
			num = 2;
			break;
		case D:
			num = 1;
			break;
		case F:
			num = 0;
			break;
		default:
			break;
		}
		
		if(weighted) {
			if(course.type().equals(CourseType.COLLEGE)) {
				num += 1;
			} else if(course.type().equals(CourseType.AP)) {
				num += .5;
			}
		}
		
		return num;
		
	}
	
	/**
	 * The semester number system neatly allows chronological
	 * comparison of semesters without complex String processing
	 * or a function dedicated to figuring out that mess.
	 * 
	 * It simply adds 3 for every year except the current one, then
	 * adds a number, depending on the semester. This generates
	 * a unique number for the semester.
	 * 
	 * @param semesterInput - The semester, ex. Spring 2020
	 * @return The semester's number
	 */
	public static int getSemesterNumber(String semesterInput) {
		
		String semester = semesterInput.split(" ")[0];
		int year = Integer.valueOf(semesterInput.split(" ")[1]);
		
		int num = (year - 1) * 3;
		
		switch(semester) {
		case "Spring":
			num += 1;
			break;
		case "Summer":
			num += 2;
			break;
		case "Fall":
			num += 3;
			break;
		default:
			break;
		}
		
		return num;
		
	}
	
	/**
	 * This function undoes the process of getSemesterNumber() 
	 * by adding numbers until the number is divisible by 3.
	 * Of course, this final number divided by 3 is the 
	 * semester's year.
	 * 
	 * Based on the amount of addition required to get to a
	 * number divisble by 3, we can conclude the appropriate
	 * semester.
	 * 
	 * @param semesterNumber - The semester's number
	 * @return The semester's name (ex. Spring 2020)
	 */
	public static String getSemesterFromNumber(int semesterNumber) {
		
		int addition = 0;
		
		while((semesterNumber % 3) != 0) {
			semesterNumber += 1;
			addition++;
		}
		
		if(addition == 0) {
			return "Fall " + (semesterNumber / 3);
		} else if(addition == 2) {
			return "Spring " + (semesterNumber / 3);
		} else if(addition == 1){
			return "Summer " + (semesterNumber / 3);
		}else {
			/*
			 * Based on the while loop, this shouldn't be possible,
			 * but better safe than sorry. Plus makes for an 
			 * amazing bug report.
			 * 
			 * Issue #1: "Whenever I try to list my courses the
			 * semesters all say 'AHHHHHHHHHHH', what do?"
			 */
			return "AHHHHHHHHHHH";
		}
		
	}
	
	/**
	 * Uses a simple selection sort to arrange
	 * courses by name.
	 * 
	 * @param courses - Unsorted list
	 * @return Sorted list
	 */
	public static List<Course> sortByName(List<Course> courses) {
		
		int i, k, posmax;
		
		for(i = courses.size() - 1; i > 0; i--) {
			
			posmax = 0;
			
			for(k = 1; k <= i; k++) {
				
				if(courses.get(k).courseName().compareTo(courses.get(posmax).courseName()) > 0) {
					posmax = k;
				}
				
			}
			
			Course temp = courses.get(i);
			courses.set(i, courses.get(posmax));
			courses.set(posmax, temp);
			
		}
		
		return courses;
		
	}
	
	/**
	 * Calculates the GPA of the provided
	 * courses, rounded to 2 decimal places.
	 * 
	 * @param courses - Courses to calculate GPA of
	 * @param weighted - If weighted, adds +.5 to grade for AP and +1 for College
	 * @return GPA value of courses with weight option.
	 */
	public static double getGPA(List<Course> courses, boolean weighted) {
		
		double total = 0.0;
		double credits = 0.0;
		
		for(Course course : courses) {
			total += getGradeNum(course, weighted) * getCredits(course);
			credits += getCredits(course);
		}
		
		double baseGPA = total / credits;
		double gpa = (int)(baseGPA * 100.0) / (100.0);
		
		return gpa;
		
	}
	
}
