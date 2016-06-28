package codepath.shah.db;

/**
 * Created by pshah on 6/21/16.
 */
public class Task {

    //private variables
    int id;
    String name;

    // Empty constructor
    public Task(){

    }

    // constructor
    public Task(int id, String name){
        this.id = id;
        this.name = name;
    }

    // constructor
    public Task(String name){
        this.name = name;
    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.name;
    }

    // setting name
    public void setName(String name){
        this.name = name;
    }

}
