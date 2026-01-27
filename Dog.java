package School;

public class Dog extends Pet {


    /* 
Algorithms 
Joshua B Wilson
java v21
 ITEC 3150
 January 26 2026

 this class is for just creating the different variables for the dog class and setting the info
*/
    private String breed;

    public Dog(String name, int age, String owners, String color,String breed){

        super(name, "Dog", age, owners, color);
        this.breed = breed;

    }

    public String getBreed(){
        return breed;
    }

}
