package itech3150.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provided utility to build links.dat from links.txt.
 * (Per assignment: do not modify other than package name.)
 */
public class WebsiteBuilder {
    ArrayList<Website> links = new ArrayList<Website>();

    public static void main(String[] args) {
        
        WebsiteBuilder wb = new WebsiteBuilder();
        wb.readFile();
        wb.writeBinaryFile();

    }


    private void readFile() {
        File linkFile = new File("links.txt");
        try {
            Scanner inFile = new Scanner(linkFile);
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                String[] parts = line.split(" - ");
                Website ws = new Website(parts[0], parts[1]);
                links.add(ws);
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeBinaryFile() {
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream("links.dat"));
            for (int index = 0; index < links.size(); index++) {
                output.writeObject(links.get(index));
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
