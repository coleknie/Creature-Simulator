import java.util.Random;

public abstract class Creature {

    
    // Note, that output should be in (x,y) or (column,row) format as
    // the plotter expects it in that format. But internally, we will
    // think of things as (row,column)


    // When considering the direction a creature is facing, we will use indexes like so.
    
    // dir: 0=North, 1=East, 2=South, 3=West.
    // 
    //
    //
    //               N (r-1,c+0)
    //               0
    //(r+0,c-1) W 3 [ ]  1 E (r+0,c+1)
    //               2
    //               S (r+1,c+0)
    //
    //
    // 
    // 
    // The following static constants could be useful for you :)    
    public final static int NORTH = 0;
    public final static int EAST = 1;
    public final static int SOUTH = 2;
    public final static int WEST = 3;
    public final static int NUM_DIRS = 4;
    public final static int[] DIRS = {NORTH,EAST,SOUTH,WEST};

    //Use the index of the direction to determine how to add to a row or column
    //For example, if NORTH (index o), the we subtract 1 from row, and add 0 to column
    //direction
    protected final int[] dirRow = {-1,0,1,0};
    protected final int[] dirCol = {0, 1, 0, -1};

    //The following are the color labels available to creatures
    public final static char LAB_BLACK='k';
    public final static char LAB_BLUE='b';
    public final static char LAB_RED='r';
    public final static char LAB_YELLOW='y';
    public final static char LAB_ORANGE='o';
    public final static char LAB_PINK='p';
    public final static char LAB_MAGENTA='m';
    public final static char LAB_CYAN='c';
    public final static char LAB_GREEN='g';
    public final static char LAB_GRAY='e';


    // 
    
    //current direction facing
    private int dir;

    //current point in grid
    private GridPoint point;

    private GridPoint lastPoint;
    private final Creature copyOf;;

    //current color label for the point
    protected char lab;

    //random instance
    protected final Random rand;

    //City in which this creature lives so that it can update it's
    //location and get other information it might need (like the
    //location of other creatures) when making decisions about which
    //direction to move and what not.
    protected City city;


    public Creature(int r, int c, City cty, Random rnd){
        //DEFAULT Constructor
        point = new GridPoint(r,c);
        lastPoint = new GridPoint(point);
        city = cty;
        rand = rnd;
        lab = LAB_GRAY;
        dir = rand.nextInt(NUM_DIRS);
        copyOf = null;
    }

    public Creature(City cty, Random rnd){
        rand = rnd;
        city = cty;
        point = new GridPoint(rand.nextInt(City.MAX_ROW), rand.nextInt(City.MAX_COL));
        lastPoint = new GridPoint(point);
        lab = LAB_GRAY;
        dir = rand.nextInt(NUM_DIRS);
        copyOf = null;
    }

    public Creature(Creature src){ //used for deep copies
        this.rand = src.rand;
        this.city = src.city;
        this.point = src.getGridPoint();
        this.lastPoint = src.getLastGridPoint();
        this.lab = src.lab;
        this.dir = src.dir;
        this.copyOf = src;
    }

    protected void move(){
        lastPoint = new GridPoint(point);
        point.row += dirRow[dir];
        point.col += dirCol[dir];
        point.row = Math.floorMod(point.row, City.MAX_ROW-1);
        point.col = Math.floorMod(point.col, City.MAX_COL-1);
        city.updateCG(this);
    }

    public void rmSelf(){
        if(copyOf != null){
            city.queueRmCreature(copyOf);
        }
        else{
            city.queueRmCreature(this);
        }
    }




    //abstract methods you'll need to realize in children classes It's
    //also possible to change these from abstract if you have good
    //reason (e.g., inheritance) for implementing them here, but you
    //should explain that in your UML and README file.    
    public abstract void maybeTurn(); //randomnly turn if suppose to
    public abstract void takeAction(); //take an action based on a location in the city
    public abstract boolean die(); //return true if should die, false otherwise
    public abstract void step(); //take a step in the given direction in the city
    public abstract Creature getDeepCopy();

    protected void randomTurn(){
        dir = (rand.nextInt(NUM_DIRS) + dir) % NUM_DIRS;
    }

    protected void rightTurn(){
        dir = (1 + dir) % NUM_DIRS;
    }

    protected boolean setDir(int d){
        for(int i = 0; i < DIRS.length;i++){
            if(d == DIRS[i]){
                dir = d;
                return true;
            }
        }
        return false;
    }

    public int getDir(){
        return dir;
    }


    //You get these getters for free :)
    public int getRow(){
        return point.row;
    }
    public int getCol(){
        return point.col;
    }
    public GridPoint getGridPoint(){
        return new GridPoint(point); //return a copy to preseve
                                     //encapsulation
    }
    public GridPoint getLastGridPoint(){
        return new GridPoint(lastPoint); //return a copy to preseve
                                     //encapsulation
    }
    
    //You also get this distance function for free as well. Distance
    //is defined in GridPoint.
    public int dist(Creature c){
        int d = point.dist(c.getGridPoint());
        GridPoint[] tpoints = c.getGridPoint().getTorusPoints();
        for(int i = 0; i < tpoints.length; i++){
            if(point.dist(tpoints[i]) < d){
                d = point.dist(tpoints[i]);
            }
        }
        return d;
    }

    public void findClosestPath(Creature c){

        GridPoint gp = c.getGridPoint();

        GridPoint[] tpoints = c.getGridPoint().getTorusPoints();

        int d = point.dist(tpoints[0]);

        for(int i = 0; i < tpoints.length; i++){
            if(point.dist(tpoints[i]) < d){
                d = point.dist(tpoints[i]);
                gp = tpoints[i];
            }
        }        
        if(point.coldist(gp) > point.rowdist(gp)){

            if(point.col > gp.col){
                dir = WEST;
            }
            else{
                dir = EAST;
            }
        }

        else{

            if(point.row > gp.row){
                dir = NORTH;
            }
            else{
                dir = SOUTH;
            }
        }

    }


    //To String so you can output a creature to the plotter
    public String toString() {
        //output in col,row format or (x,y) format
        return ""+this.point.col+" "+this.point.row+" "+lab;
    }

}
