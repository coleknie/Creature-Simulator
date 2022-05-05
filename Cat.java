import java.util.*;
public class Cat extends Creature {
    protected int jumpDistance = 2;
    protected int chaseDistance = 20;
    protected int timeSinceLastEat = 0;
    protected int maxTimeWithoutEat = 50;
    protected char baseLab = LAB_YELLOW;
    protected char chaseLab = LAB_CYAN;
    protected boolean chase = false;

    public Cat(City cty, Random rnd){
        super(cty, rnd);
        lab = baseLab;

    }

    public Cat(int r, int c, City cty, Random rnd){
        super(r,c,cty,rnd);
        lab = baseLab;
    }

    public Cat(Cat src){
        super(src);
        this.jumpDistance = src.jumpDistance;
        this.chaseDistance = src.chaseDistance;
        this.timeSinceLastEat = src.timeSinceLastEat;
        this.maxTimeWithoutEat = src.maxTimeWithoutEat;
        this.baseLab = src.baseLab;
        this.chaseLab = src.chaseLab;
        lab = baseLab;
    }

    public void maybeTurn(){
        if(rand.nextInt(20) == 1){
            randomTurn();
        }
    }

    public void takeAction(){
        performKills();
    }

    public boolean die(){
        if(timeSinceLastEat > maxTimeWithoutEat){
            city.queueAddCreature(new ZombieCat(this.getGridPoint().row, this.getGridPoint().col, city, rand));
            return true;
        }
        return false;
    }

    public void step(){
        lab = baseLab;
        maybeTurn();
        performChase();
        timeSinceLastEat++;
        for(int i = 0; i < jumpDistance; i++){
            move();
        }
    }

    public Cat getDeepCopy(){
        return new Cat(this);
    }

    private void performChase(){
        boolean chase = false;
        int closest = -1;
        int cur = -1;
        LinkedList<Creature> adjacent = city.getAllCreatures();
        for(Creature c: adjacent){
            cur = dist(c);
            if(maybeChase(c) && (chase == false || cur < closest)){
                findClosestPath(c);
                lab = chaseLab;
                chase = true;
                closest = cur;
                if(cur == jumpDistance){
                    return;
                }
            }
        }
    }


    protected boolean maybeChase(Creature c){
        return (c instanceof Mouse || c instanceof Bird) && dist(c) <= chaseDistance;
    }


    private void performKills(){
        LinkedList<Creature> adjacent = city.getCreaturesAtGP(getGridPoint());
        for(Creature c: adjacent){
            if(maybeKill(c)){
                c.rmSelf();
                timeSinceLastEat = 0;
            }
        }
    }


    protected boolean maybeKill(Creature c){
        return c instanceof Mouse || c instanceof Bird;
    }
    
}
