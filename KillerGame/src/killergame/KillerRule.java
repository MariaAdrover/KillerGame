package killergame ;

public class KillerRule { 
 
    public KillerRule(){
        
    }
    
    public static int requestRule (Object o, int lado) {
        int serverAction = 0; 
        // 0 --> El servidor no ha de hacer nada
        // -1 --> ha de mandar la bola al servidor de la izquierda
        // 1 --> ha de mandar la bola al servidor de la derecha
        
        // AÑADIR REGLAS Y METODOS PARA APLICARLAS...
        if ((o instanceof Autonomous) && lado == -1) {
            ((Autonomous) o).setState(0);
            serverAction = -1;
        } else if ((o instanceof Autonomous) && lado == 1) {
            ((Autonomous) o).setState(0);
            serverAction = 1;
        }
        
        return serverAction;
    }
    /*
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
    }*/
}
