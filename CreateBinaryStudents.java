/**
 * ITEC 3150 - 01 Spring 2026
 * IC 1
 */

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateBinaryStudents {
    public static void main(String[] args) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("Students.dat"))) {

            // Record format (repeat):
            // int id, UTF name, double gpa
            out.writeInt(1001);
            out.writeUTF("Ava Kim");
            out.writeDouble(3.82);

            out.writeInt(1002);
            out.writeUTF("Noah Patel");
            out.writeDouble(3.45);

            out.writeInt(1003);
            out.writeUTF("Mia Johnson");
            out.writeDouble(3.95);

            out.writeInt(1004);
            out.writeUTF("Ethan Garcia");
            out.writeDouble(2.90);

            System.out.println("Students.dat created successfully.");

        } catch (IOException e) {
            System.out.println("Error creating Students.dat");
            e.printStackTrace();
        }
    }
}
