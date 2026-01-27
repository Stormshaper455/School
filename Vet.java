package School;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

/* 
Algorithms 
Joshua B Wilson
java v21
 ITEC 3150
 January 26 2026

 this class is for running the whole of all the files and makingbthe main loop of the project.
 I used this for using all the data i got and input from the other classes
*/
public class Vet {
    private final Scanner ans = new Scanner(System.in);
    private final Stack<Pet> pets = new Stack<>();

    public void addPet(Pet p) {
        pets.push(p);
    }

    public void DisplayAnimals() {
        Iterator<Pet> it = pets.iterator();

        while (it.hasNext()) {
            Pet p = it.next();

            if (p instanceof Cat) {
                Cat c = (Cat) p;
                System.out.println("Cat: " + c.getName());
                System.out.println("Age: " + c.getAge());
                System.out.println("Owner: " + c.getOwners());
                System.out.println("Color: " + c.getColor());
                System.out.println("Long hair: " + c.getLongHair());
                System.out.println("Claws: " + c.getClaws());
                System.out.println();
            } else if (p instanceof Dog) {
                Dog d = (Dog) p;
                System.out.println("Dog: " + d.getName());
                System.out.println("Age: " + d.getAge());
                System.out.println("Owner: " + d.getOwners());
                System.out.println("Color: " + d.getColor());
                System.out.println("Breed: " + d.getBreed());
                System.out.println();
            }
        }
    }

    public void read() throws FileNotFoundException {
        File fl = new File("/Users/stormdhsper45/Desktop/Pets.txt");

        try (Scanner sc = new Scanner(fl)) {
            while (sc.hasNextLine()) {
                String ty = sc.nextLine().trim();
                if (ty.isEmpty()) {
                    continue;
                }

                if (ty.equalsIgnoreCase("Cat")) {
                    String name = sc.nextLine().trim();
                    int age = Integer.parseInt(sc.nextLine().trim());
                    String owner = sc.nextLine().trim();
                    String color = sc.nextLine().trim();
                    boolean longHair = Boolean.parseBoolean(sc.nextLine().trim());
                    boolean claws = Boolean.parseBoolean(sc.nextLine().trim());
                    addPet(new Cat(name, age, owner, color, longHair, claws));
                } else if (ty.equalsIgnoreCase("Dog")) {
                    String name = sc.nextLine().trim();
                    int age = Integer.parseInt(sc.nextLine().trim());
                    String owner = sc.nextLine().trim();
                    String color = sc.nextLine().trim();
                    String breed = sc.nextLine().trim();
                    addPet(new Dog(name, age, owner, color, breed));
                }
            }
        }
    }

    public void write() throws FileNotFoundException {
        File fl = new File("text.txt");

        try (PrintWriter out = new PrintWriter(fl)) {
            Iterator<Pet> it = pets.iterator();

            while (it.hasNext()) {
                Pet p = it.next();

                if (p instanceof Cat) {
                    Cat c = (Cat) p;
                    out.println("Cat");
                    out.println(c.getName());
                    out.println(c.getAge());
                    out.println(c.getOwners());
                    out.println(c.getColor());
                    out.println(c.getLongHair());
                    out.println(c.getClaws());
                } else if (p instanceof Dog) {
                    Dog d = (Dog) p;
                    out.println("Dog");
                    out.println(d.getName());
                    out.println(d.getAge());
                    out.println(d.getOwners());
                    out.println(d.getColor());
                    out.println(d.getBreed());
                }
            }
        }
    }

    public void findByName() throws PetNotFoundException {
        System.out.print("Name to find: ");
        String target = ans.nextLine().trim();

        Iterator<Pet> it = pets.iterator();
        while (it.hasNext()) {
            Pet p = it.next();
            if (p.getName().equalsIgnoreCase(target)) {
                if (p instanceof Cat) {
                    Cat c = (Cat) p;
                    System.out.println("Cat: " + c.getName());
                    System.out.println("Age: " + c.getAge());
                    System.out.println("Owner: " + c.getOwners());
                    System.out.println("Color: " + c.getColor());
                    System.out.println("Long hair: " + c.getLongHair());
                    System.out.println("Claws: " + c.getClaws());
                } else if (p instanceof Dog) {
                    Dog d = (Dog) p;
                    System.out.println("Dog: " + d.getName());
                    System.out.println("Age: " + d.getAge());
                    System.out.println("Owner: " + d.getOwners());
                    System.out.println("Color: " + d.getColor());
                    System.out.println("Breed: " + d.getBreed());
                }
                return;
            }
        }

        throw new PetNotFoundException("Pet not found: " + target);
    }

    public void delAns() throws PetNotFoundException {
        System.out.print("Name to delete: ");
        String target = ans.nextLine().trim();

        boolean deleted = false;

        Iterator<Pet> it = pets.iterator();
        while (it.hasNext()) {
            Pet p = it.next();
            if (p.getName().equalsIgnoreCase(target)) {
                it.remove();
                deleted = true;
                break;
            }
        }

        if (!deleted) {
            throw new PetNotFoundException("Pet not found: " + target);
        }
    }

    public static void main(String[] args) {
        Vet building = new Vet();

        try {
            building.read();
            building.DisplayAnimals();

            System.out.print("Find or delete a pet by name? //find/delete/none//: ");
            String choice = building.ans.nextLine().trim();

            if (choice.equalsIgnoreCase("find")) {
                building.findByName();
            } else if (choice.equalsIgnoreCase("delete")) {
                building.delAns();
            }

            building.write();
        } catch (PetNotFoundException e) {
            System.out.println(e.getMessage());
            try {
                building.write();
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
