package lassignment3;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// -------------------- CUSTOM EXCEPTION --------------------
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

// -------------------- LOADER THREAD --------------------
class Loader implements Runnable {
    @Override
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted.");
        }
    }
}

// -------------------- STUDENT CLASS --------------------
class Student {

    private Integer rollNo;   // Wrapper Class
    private String name;
    private String email;
    private String course;
    private Double marks;     // Wrapper Class
    private char grade;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void display() {
        System.out.println("\nStudent Info:");
        System.out.println("Roll No : " + rollNo);
        System.out.println("Name    : " + name);
        System.out.println("Email   : " + email);
        System.out.println("Course  : " + course);
        System.out.println("Marks   : " + marks);
        System.out.println("Grade   : " + grade);
        System.out.println("----------------------------------");
    }
}

// -------------------- INTERFACE --------------------
interface RecordActions {
    void addStudent(Student s);
    void viewAllStudents();
    Student searchStudent(Integer rollNo) throws StudentNotFoundException;
}

// -------------------- STUDENT MANAGER --------------------
class StudentManager implements RecordActions {

    private Map<Integer, Student> records = new HashMap<>();

    @Override
    public void addStudent(Student s) {
        if (records.containsKey(s.getRollNo())) {
            System.out.println("Duplicate Roll Number! Student not added.");
            return;
        }
        records.put(s.getRollNo(), s);
        System.out.println("Student added successfully!");
    }

    @Override
    public void viewAllStudents() {
        if (records.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        for (Student s : records.values()) {
            s.display();
        }
    }

    @Override
    public Student searchStudent(Integer rollNo) throws StudentNotFoundException {
        if (!records.containsKey(rollNo)) {
            throw new StudentNotFoundException("Student with Roll No " + rollNo + " not found!");
        }
        return records.get(rollNo);
    }
}

// -------------------- MAIN APPLICATION --------------------
public class StudentManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer rollNo = Integer.parseInt(sc.nextLine().trim()); // Wrapper Class

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            System.out.print("Enter Course: ");
            String course = sc.nextLine().trim();

            System.out.print("Enter Marks (Double): ");
            Double marks = Double.parseDouble(sc.nextLine().trim()); // Wrapper Class

            // Validation
            if (name.isEmpty() || email.isEmpty() || course.isEmpty()) {
                throw new Exception("Name or course cannot be empty!");
            }
            if (marks < 0 || marks > 100) {
                throw new Exception("Invalid marks! Marks must be 0–100.");
            }

            // Multithreading (Loader)
            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            // Create student object
            Student s = new Student(rollNo, name, email, course, marks);

            // Save student
            manager.addStudent(s);

            // Display data
            s.display();
            System.out.println("Program execution completed.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid numeric input.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}