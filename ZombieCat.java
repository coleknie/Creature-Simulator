import java.util.*;
public class ZombieCat extends Cat {


    public ZombieCat(City cty, Random rnd){
        super(cty, rnd);
        lab = LAB_RED;
        baseLab = LAB_RED;
        chaseLab = LAB_BLACK;
        chaseDistance = 40;
        maxTimeWithoutEat = 100;
        jumpDistance = 3;
    }

    public ZombieCat(int r, int c, City cty, Random rnd){
        super(r,c,cty,rnd);
        lab = LAB_RED;
        baseLab = LAB_RED;
        chaseLab = LAB_BLACK;
        chaseDistance = 40;
        maxTimeWithoutEat = 100;
        jumpDistance = 3;
    }

    public ZombieCat(ZombieCat src){
        super(src);
    }

    @Override
    protected boolean maybeKill(Creature c) {
        if(c instanceof Cat && !(c instanceof ZombieCat)){
            city.queueAddCreature(new ZombieCat(getGridPoint().row, getGridPoint().col, city, rand));
            return true;
        }
        return super.maybeKill(c);
    }

    @Override
    protected boolean maybeChase(Creature c) {

        if(this.dist(c) < chaseDistance && c instanceof Cat && !(c instanceof ZombieCat)){
            return true;
        }
        return super.maybeChase(c);
    }

    @Override
    public boolean die() {
        return timeSinceLastEat > maxTimeWithoutEat;
    }

    public ZombieCat getDeepCopy(){
        return new ZombieCat(this);
    }
}
