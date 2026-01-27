package School;


/* 
Algorithms 
Joshua B Wilson
java v21
 ITEC 3150
 January 26 2026

 this class is for storing the data from both the dog and cat classes so the main class can use it i called it Vet if thats okay
*/


public class Pet {



    
/*

soo cats and dogs have names
owners have names 
both have ages
thye have colors
thy have a type aka dog or cat
dogs have a breed
cats have long hair or short 
cats have claws or nun

*/

    private String name;
    private String owners;
    private int age;
    private String color;
    private String type;

 public Pet(String name, String type, int age, String owners, String color) {
 this.name = name;
this.owners = owners;
this.age = age;
this.color = color;
this.type = type;

}
public String getName(){
    return name;
}

public String getOwners(){
    return owners;
}

public int getAge(){
    return age;
}

public String getColor(){
    return color;
}

public String getType(){
    return type;
}



}