import java.util.*;
import java.util.Random;
public class Mouse extends Creature {
    protected int lifespan = 30;
    protected int turnsAlive = 0;
    

    Mouse(int r, int c, City cty, Random rnd){
        super(r, c, cty, rnd);
        lab = LAB_BLUE;
    }

    Mouse(City cty, Random rnd){
        super(cty, rnd);
        lab = LAB_BLUE;
    }

    Mouse(Mouse src){
        super(src);
        this.lifespan = src.lifespan;
        this.turnsAlive = src.turnsAlive;
    }

    public void maybeTurn(){
        if(rand.nextInt(5) == 1){
            randomTurn();
        }
    }

    public boolean die(){
        return turnsAlive > lifespan;
    }

    public void step(){
        maybeTurn();
        move();
        turnsAlive++;
    }


    public void takeAction(){
        maybeBaby();
    }

    protected void maybeBaby(){
        if(turnsAlive == 20){
            city.queueAddCreature(new Mouse(this.getGridPoint().row, this.getGridPoint().col, city, rand));
        }
    }

    public Mouse getDeepCopy(){
        return new Mouse(this);
    }

}

