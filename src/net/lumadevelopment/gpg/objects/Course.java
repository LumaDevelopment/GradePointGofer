package net.lumadevelopment.gpg.objects;

public record Course(String courseName, String semester, LetterGrade grade, CourseType type) { }

// I love records.