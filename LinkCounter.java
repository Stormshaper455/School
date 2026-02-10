package itech3150.hw2;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * 
 * Joshua Wilson
 * ITEC 3150 (Algorithms)
 * Written Febuary 8th, 2026
 * 
 * Student File (YOU complete this file)
 *
 * Do not modify Website or WebsiteBuilder (except the package).
 *
 * Task Summary:
 * 1) Run WebsiteBuilder to create links.dat from links.txt
 * 2) Read Website objects from links.dat using ObjectInputStream until EOFException
 * 3) Store objects in ArrayList<Website>
 * 4) Print all Website objects using toString()
 * 5) Build a HashSet<String> of unique website names and print it
 * 6) Build HashMap<String, HashSet<String>> name -> unique URLs
 * 7) Build HashMap<String, Integer> name -> count of unique URLs
 * 8) Print the final counts
 */
public class LinkCounter {

    // Store the Website objects from the file for later processing
    private ArrayList<Website> sites = new ArrayList<>();

    public static void main(String[] args) {
        LinkCounter app = new LinkCounter();

        // TODO 1: read links.dat into app.sites
        app.readBinaryFile("/Users/stormdhsper45/Desktop/links.dat");

        // TODO 2: print all Website objects
        app.printWebsites();

        // TODO 3: build a set of unique names and print it
        HashSet<String> names = app.buildNameSet();
        app.printNames(names);

        // TODO 4: build a HashMap<String, HashSet<String>> name -> unique URLs
        HashMap<String, HashSet<String>> nameToUrls = app.buildHashMap(names);

        // TODO 5: build a HashMap<String, Integer> name -> count
        HashMap<String, Integer> counts = app.buildCountMap(nameToUrls);

        // TODO 6: print final counts
        app.printCounts(counts);
    }

    /**
     * Read Website objects from a binary file.
     * Use EOFException to stop reading.
     */
    private void readBinaryFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                Website web = (Website) in.readObject();
                sites.add(web);
            }
        } catch (EOFException e) {
            // End of file reached - stop reading
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print all Website objects from the ArrayList.
     */
    private void printWebsites() {
        // just setting the prints for the names
        for (Website web : sites) {
            System.out.println(web);
        }
    }

    /**
     * Build a HashSet of unique website names.
     */
    private HashSet<String> buildNameSet() {
        //adds the names to the Map
        HashSet<String> names = new HashSet<>();
        for (Website web : sites) {
            names.add(web.getName());
        }
        return names;
    }

    /**
     * Print the unique names set.
     */
    private void printNames(HashSet<String> names) {
        // i used this to make a custom format but still similar to the one you said
        System.out.println("\tNames");
        for (String name : names) {
            System.out.println("Name: " + name);
        }
    }

    /**
     * Build HashMap<String, HashSet<String>> mapping name -> set of unique URLs.
     * (Suggested method in assignment.)
     */
    private HashMap<String, HashSet<String>> buildHashMap(HashSet<String> names) {
        HashMap<String, HashSet<String>> map = new HashMap<>();

        for (String name : names) {
            HashSet<String> urls = new HashSet<>();

            for (Website site : sites) {
                if (site.getName().equals(name)) {
                    urls.add(site.getURL());
                }
            }

            map.put(name, urls);
        }

        return map;
    }

    /**
     * Build HashMap<String, Integer> mapping name -> count of unique URLs.
     */
    private HashMap<String, Integer> buildCountMap(HashMap<String, HashSet<String>> hashed) {
        HashMap<String, Integer> counts = new HashMap<>();

        for (String name : hashed.keySet()) {
            HashSet<String> urls = hashed.get(name);
            counts.put(name, urls.size());
        }

        return counts;
    }

    /**
     * Print the final counts.
     */
    private void printCounts(HashMap<String, Integer> counts) {
        System.out.println("\tWebsite counts");
        System.out.println("Site\t\tCount");

        for (String name : counts.keySet()) {
            System.out.println(name + "\t\t" + counts.get(name));
        }
    }
}
