import java.util.*;

public class City{


    //Determine the City Grid based on the size of the Plotter
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;

    //Different names, same result
    public static final int MAX_COL = WIDTH;
    public static final int MAX_ROW = HEIGHT;

    
    // The Grid World for your reference
    //
    //        (x)
    //        columns
    //        0 1 2 3 4 5 ... WIDTH (MAX_COL)
    //       .----------------...
    // (y)r 0|           ,--column (x)
    //    o 1|      * (1,3) 
    //    w 2|         ^    
    //      3|         '-row (y)
    //      .|
    //      .|
    //      .|       
    //HEIGHT :
    //MAX_ROW:
    //


    //IMPORTANT! The grid world is a torus. Whenn a a point goes off
    //an edge, it wrapps around to the other side. So with a width of
    //of 80, a point at (79,5) would move to (0,5) next if it moved
    //one space down. Similarly, with a height of 80, a point
    //at (5,0) would move to (5,79) if it moved one space left.


    //-------------------------------------
    //The simulation's Data Structures
    //
    private List<Creature> creatures; //list of all creatues
    //Map of GridPoint to a list of cratures whose location is that grid point 
    private HashMap<GridPoint,LinkedList<Creature>> creatureGrid; 
    
    private Queue<Creature> rmQueue; //creatures that are staged for removal
    private Queue<Creature> addQueue; //creatures taht are staged to be added    

    //... YES! you must use all of these collections.
    //... YES! you may add others if you need, but you MUST use these too!
    

    //Random instance
    private Random rand;

    //Note, for Level 4, you may need to change this constructors arguments.
    public City(Random rand, int numMice, int numCats, int numZombieCats, int numEggs) {
        this.rand = rand;

        creatures = new LinkedList<Creature>();
        creatureGrid = new HashMap<GridPoint,LinkedList<Creature>>();
        rmQueue = new LinkedList<Creature>();
        addQueue = new LinkedList<Creature>();

        for(int i = 0; i < MAX_ROW; i++){
            for(int j = 0; j < MAX_COL; j++){
                creatureGrid.put(new GridPoint(i,j), new LinkedList<Creature>());
            }
        }

        for(int i = 0; i < numMice; i++){
            Mouse m = new Mouse(this, rand);
            addQueue.add(m);
        }
        for(int i = 0; i < numCats; i++){
            Cat m = new Cat(this, rand);
            addQueue.add(m);
        }

        for(int i = 0; i < numZombieCats; i++){
            ZombieCat m = new ZombieCat(this, rand);
            addQueue.add(m);
        }

        for(int i = 0; i < numEggs; i++){
            Egg m = new Egg(this, rand);
            addQueue.add(m);
        }
        
    }


    //Return the current number of creatures in the simulation
    public int numCreatures(){
        return creatures.size();
    }

    
    // Because we'll be iterating of the Creature List we can't remove
    // items from the list until after that iteration is
    // complete. Instead, we will queue (or stage) removals and
    // additions.
    //
    // I gave yout the two methods for adding, but you'll need to
    // implementing the clearing.

    //stage a create to be removed
    public void queueRmCreature(Creature c){
        //DO NOT EDIT
        rmQueue.add(c);
    }

    //Stage a creature to be added
    public void queueAddCreature(Creature c){
        //DO NOT EDIT
        addQueue.add(c);
    }
    
    //Clear the queue of creatures staged for removal and addition
    public void clearQueue(){


        for(Creature r : rmQueue){
            creatures.remove(r);
            creatureGrid.get(r.getGridPoint()).remove(r);
        }

        for(Creature a: addQueue){
            if(!creatures.contains(a)){
                creatures.add(a);
                creatureGrid.get(a.getGridPoint()).add(a);
            }
        }

        rmQueue.clear();
        addQueue.clear();
    }

    //updates creaturegrid
    public void updateCG(Creature c){
        creatureGrid.get(c.getLastGridPoint()).remove(c);
        creatureGrid.get(c.getGridPoint()).add(c);
    }


    // returns a deep copy of creatures
    // encapsulation
    public LinkedList<Creature> getAllCreatures(){
        LinkedList<Creature> copy = new LinkedList<Creature>();
        for(Creature c: creatures){
            copy.add(c.getDeepCopy());
        }
        return copy;
    }

    
    public LinkedList<Creature> getCreaturesAtGP(GridPoint p){
        LinkedList<Creature> ll = creatureGrid.get(p);
        LinkedList<Creature> copy = new LinkedList<Creature>();

        for(Creature c: ll){
            copy.add(c.getDeepCopy());
        }

        return copy;
    }


    //TODO -- there are a number of other member methods you'll want
    //to write here to interact with creatures. This is a good thing
    //to think about when putting together your UML diagram


    

    // This is the simulate method that is called in Simulator.java
    // 
    //You need to realize in your Creature class (and decendents) this
    //functionality so that they work properly. Read through these
    //comments so it's clear you understand.
    public void simulate() {
        //DO NOT EDIT!
        
        //You get this one for free, but you need to review this to
        //understand how to implement your various creatures

        //First, for all creatures ...
        for(Creature c : creatures){
            //check to see if any creature should die
            if(c.die()){
                queueRmCreature(c); //stage that creature for removal
                continue;
            }
            
            //for all remaining creatures take a step
            //this could involve chasing another creature
            //or running away from a creature
            c.step();
        }

        //Clear queue of any removes or adds of creatures due to creature death
        clearQueue(); 

        
        //For every creature determine if an action should be taken
        // such as, procreating (mice), eating (cats, zombiecats), or
        // some new action that you'll add to the system.
        for(Creature c : creatures){
            c.takeAction(); 
        }

        //Clear queue of any removes or adds following actions, such
        //as a mouse that was eaten or a cat that was was removed due
        //to being turned into a zombie cat.
        clearQueue();

        //Output all the locations of the creatures.
        for(Creature c : creatures){
            System.out.println(c);
        }
    }
}