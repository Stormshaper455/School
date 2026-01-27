package School;



 /* 
Algorithms 
Joshua B Wilson
java v21
 ITEC 3150
 January 26 2026

 this class is for just creating the different variables for the Cat class and setting the info
*/
public class Cat extends Pet {
    private boolean longHair;
    private boolean claws;
    
    public Cat(String name, int age, String owners, String color, boolean longHair, boolean claws){

        super(name, "Cat", age, owners, color);
         this.longHair = longHair;
        this.claws = claws;

    }

    public boolean getLongHair(){
        return longHair;

    }

    public boolean getClaws(){
        return claws;
    }


   
}
