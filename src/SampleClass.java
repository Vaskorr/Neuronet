import javax.swing.*;

import static java.lang.Thread.sleep;

public class SampleClass extends JFrame{
    static int[][] base;
    public static int[][] y_coords;

    public static void main(String[] args) throws InterruptedException {
        for(int h = 0 ; h < 4; h++) {
            Neuronet neuronet = new Neuronet();
            init_database();
            neuronet.init(new int[] {2, 4, 1}, 0.5, 0.9);
            neuronet.fillWeights();
            neuronet.autotrain(1000, base);
        }
    }

    public static void init_database(){
        base = new int[4][3];
        base[0][0] = 0;
        base[0][1] = 0;
        base[0][2] = 0;

        base[1][0] = 0;
        base[1][1] = 1;
        base[1][2] = 1;

        base[2][0] = 1;
        base[2][1] = 0;
        base[2][2] = 1;

        base[3][0] = 1;
        base[3][1] = 1;
        base[3][2] = 0;
    }
}