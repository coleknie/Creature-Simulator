import java.util.*;
public class Bird extends Creature {
    protected int lifespan = 25;
    protected int turnsAlive = 0;
    
    public Bird(City cty, Random rnd){
        super(cty, rnd);
        lab = LAB_GREEN;
    }

    public Bird(int r, int c, City cty, Random rnd){
        super(r,c,cty,rnd);
        lab = LAB_GREEN;
    }

    public Bird(Bird src){
        super(src);
        lab = src.lab;
        lifespan = src.lifespan;
        turnsAlive = src.turnsAlive;
    }

    public void maybeTurn(){
        if(rand.nextInt(4) == 1){
            randomTurn();
        }
    }

    public void takeAction(){
        if (rand.nextInt(100) == 1){
            city.queueAddCreature(new Egg(getGridPoint().row, getGridPoint().col, city,rand));
        }
    }

    private int nearestPredatorDist(){
        List<Creature> creatures = city.getAllCreatures();
        int d = City.MAX_ROW + City.MAX_COL;
        for(Creature c: creatures){
            if((d < 0 || dist(c) < d) && isPredator(c)){
                d = dist(c);
            }
        }
        return d;
    }

    private boolean isPredator(Creature c){
        return c instanceof Cat;
    }

    public boolean die(){
        return turnsAlive > lifespan;
    }

    public void step(){

        if (nearestPredatorDist() < 50){
            maybeTurn();
            move();
            move();
            return;
        }

        Bird b;
        int dist = 0;
        for(int i = 0; i < NUM_DIRS; i++){
            b = new Bird(this);
            for(int j = 0; j < i; j++){
                b.rightTurn();
            }
            b.move();
            if(dist < b.nearestPredatorDist()){
                setDir(b.getDir());
                dist = b.nearestPredatorDist();
            }
        }
        move();
        for(int i = 0; i < NUM_DIRS; i++){
            b = new Bird(this);
            for(int j = 0; j < i; j++){
                b.rightTurn();
            }
            b.move();
            if(dist < b.nearestPredatorDist()){
                setDir(b.getDir());
                dist = b.nearestPredatorDist();
            }
        }
        move();
    }

    public Bird getDeepCopy(){
        return new Bird(this);
    }
}
