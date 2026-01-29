/**
 * ITEC 3150 - 01 Spring 2026
 * IC 1
 */

package School.al;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StudentTracker {
    private ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        StudentTracker tracker = new StudentTracker();
        if (tracker.readFile()) {
            tracker.printStudents();
        }
    }

    // TODO: MODIFY THIS METHOD ONLY - THIS IS THE ONE YOU WILL SUBMIT
    private boolean readFile() {
        try (DataInputStream read = new  DataInputStream(new FileInputStream("Students.dat"))){

            while(true){
                int id = read.readInt();
                String name = read.readUTF();
                double gpa = read.readDouble();
                addStudent(new Student(id, name, gpa));
                
            }
        } catch (EOFException e){


            return true;
        } catch (IOException e){
        return false;

        }

        // Replace this with binary reading from Students.dat
    }

    private void addStudent(Student s) {
        students.add(s);
    }

    private void printStudents() {
        for (Student s : students) {
            System.out.println(s);
        }
    }
}
