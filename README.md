# GradePointGofer
*The restrictive but amusingly-named solution to all of your command line based GPA calculation problems!*

As a dual-enrolled student, calculating my GPA was always a nightmare to do with online calculators. I would have to add years of high school AND college classes, and do it all over again the moment I got done with a semester. Now, while a normal person would use an Excel spreadsheet, a Computer Science student that has been itching to write some good (and or servicable) code will make a program for it!

**This is GradePointGofer, a program that allows you to add, remove list, list different, and calculat the GPA of classes, all while allowing what you've done to be saved to a nice text file.**

Commands:
- Add courses ('add') - Adds a course with the specified name, semester, grade, and type (AP, College, or Regular)
- Remove courses ('remove') - Removes the first found course with the designated name and semester.
- List courses ('list') - Prints every semester (every semester that has a class) and its classes chronologically, with the classes sorted alphabetically.
- List courses without semester headings ('freelist') - Prints either all courses, courses from a semester, or courses from a school year alphabetically and without semester organization.
- Calculate GPA ('gpa') - Calculates the GPA for either all courses, courses from a semester, or courses from a school year either weighted or unweighted.

Now, this is a personal project, so it likely won't fit virtually anyone's needs in practice, but the code is nice!

If you *must* know the details of what the program assumes of the grading system:
- Assumes a semester based system (Spring, Summer, and Fall) in which multi-semester courses are split into semesters and assigned a grade for each semester.
- Assumes a 4-point scale with no +/- grade letters.
- Assumes +.5 grade points for AP classes and +1 grade point for College courses.
- Assumes all classes are worth .5 credits unless they're college classes, in which case they're worth 1 credit.

Additionally, if you *must* know the details of what the program assumes of the user
- Assumes the user does not input invalid Semester names (ex. Winter 2022, Winter2022, winter 2022, etc.)
- Assumes the user does not input invalid school years (ex. 2021 - 2022, Fall 2021-Spring 2022, etc.)
- Assumes the user has no need for multiple Parcel files.
- Assumes the user has the knowhow to launch .jar files.

In all seriousness, this project was super fun to work on. I'm constantly trying to improve my formatting, conventions, commenting, and technical capabilities, so a project was just what I need right now. Maybe the code will be helpful for any other students looking to learn more about code. Or just trying to get a solid number on the GPA and not waste a little under an hour of their life on hunting down all the course details and re-entering them every semester.

~ Luma