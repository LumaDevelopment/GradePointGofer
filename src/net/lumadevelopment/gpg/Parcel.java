package net.lumadevelopment.gpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lumadevelopment.gpg.objects.Course;
import net.lumadevelopment.gpg.workers.CourseWorker;

public class Parcel {

	/*
	 * Technically, Parcel could just be seen
	 * as a List wrapper, but the functions
	 * are what make it unique.
	 */
	private List<Course> courses;
	
	public Parcel() {
		this.courses = new ArrayList<Course>();
	}
	
	public Parcel(List<Course> courses) {
		this.courses = courses;
	}
	
	public List<Course> getCourses() { return courses; }
	
	public void setCourses(List<Course> courses) { this.courses = courses; }
	
	/*
	 * No add or remove course functions except through Terminal.
	 * Probably could've just done a function
	 * that returns a Parcel object in ParcelWorker
	 * for that, but it didn't come up often
	 * so I just... forgot.
	 */
	
	// Format: <season> <year>
	public List<Course> getBySemester(String semester) {
		
		List<Course> semCourses = new ArrayList<Course>();
		
		for(Course course : courses) {
			
			if(course.semester().equalsIgnoreCase(semester)) {
				semCourses.add(course);
			}
			
		}
		
		return semCourses;
		
	}
	
	// Format: <year>-<year>
	public List<Course> getBySchoolYear(String year) {
		
		String startYear = year.split("-")[0];
		String endYear = year.split("-")[1];
		
		List<Course> yrCourses = new ArrayList<Course>();
		
		String[] semesters = new String[] {"Summer " + startYear, "Fall " + startYear, "Spring " + endYear};
		
		for(int i = 0; i < semesters.length; i++) {
			for(Course course : getBySemester(semesters[i])) {
				yrCourses.add(course);
			}
		}
		
		return yrCourses;
		
	}
	
	/**
	 * To find all the semesters with courses in them,
	 * sort them chronologically, then associate all of their
	 * courses with the semesters, then sort the courses 
	 * alphabetically.
	 * 
	 * @return List of semesters and their courses, sorted alphabetically
	 */
	public HashMap<String, List<Course>> listCommandData() {
		
		int max_semester = Integer.MIN_VALUE;
		int min_semester = Integer.MAX_VALUE;
		
		for(Course c : getCourses()) {
			
			int semesterNumber = CourseWorker.getSemesterNumber(c.semester());
			
			if(semesterNumber < min_semester) {
				min_semester = semesterNumber;
			}
			
			if(semesterNumber > max_semester) {
				max_semester = semesterNumber;
			}
			
		}
		
		HashMap<String, List<Course>> list = new HashMap<String, List<Course>>();
		
		for(int i = min_semester; i <= max_semester; i++) {
		
			String semester = CourseWorker.getSemesterFromNumber(i);
			
			List<Course> semesterCourses = new ArrayList<Course>();
			
			for(Course c : getCourses()) {
				
				if(c.semester().equals(semester)) {
					
					semesterCourses.add(c);
					
				}
				
			}
			
			semesterCourses = CourseWorker.sortByName(semesterCourses);
			
			if(semesterCourses.size() != 0) {
				list.put(semester, semesterCourses);
			}
			
		}
		
		return list;
		
	}
	
}
