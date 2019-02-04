package killergame ;

public class KillerRule { 
 
    public KillerRule(){
        
    }
    
    public int test(VisibleObject a, int out) {
        int state = a.getState();
        if (a instanceof Autonomous) {
            ((Autonomous) a).setState(0);
        }
        
        return state;
    }
    
    public void test(VisibleObject a, VisibleObject b) {
        int stateA = a.getState();
        int stateB = b.getState();
        if (a instanceof Autonomous) {
            ((Autonomous) a).setState(0);
        }
    }
}
