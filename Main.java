import ConsoleCF.ConsoleCF;
import GUICF.GUICF;
import RandomAI.RandomAI;
import VincentAI.VincentAI;

public class Main {
    public static void main(String[] args) {
        VincentAI vince = new VincentAI(6, 7, 8);
        // RandomAI randy = new RandomAI();
        GUICF game = new GUICF(vince);
    }
}