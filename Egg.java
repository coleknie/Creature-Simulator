import java.util.*;
public class Egg extends Creature {
    protected int lifespan = 100;
    protected int turnsAlive = 0;
    
    public Egg(City cty, Random rnd){
        super(cty, rnd);
        lab = LAB_MAGENTA;
    }

    public Egg(int r, int c, City cty, Random rnd){
        super(r,c,cty,rnd);
        lab = LAB_MAGENTA;
    }

    public Egg(Egg src){
        super(src);
        lab = src.lab;
        lifespan = src.lifespan;
        turnsAlive = src.turnsAlive;
    }

    public void maybeTurn(){
        return;
    }

    public void takeAction(){
        if(turnsAlive % 10 == 0){
            flicker();
        }
    }

    private void flicker(){
        if(lab == LAB_MAGENTA){
            lab = LAB_ORANGE;
        }
        else{
            lab = LAB_MAGENTA;
        }
    }


    public boolean die(){
        if(turnsAlive > lifespan){
            city.queueAddCreature(new Bird(getGridPoint().row, getGridPoint().col, city, rand));
            return true;
        }
        return false;
    }

    public void step(){
        turnsAlive++;
        return;
    }

    public Egg getDeepCopy(){
        return new Egg(this);
    }
}
