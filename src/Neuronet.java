// Java Neuronet class by Vaskorr
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class Drawing extends JPanel {
    int yg[][] =  Neuronet.coords;
    int l = Neuronet.l;
    int xg[] =  new int[1000];
    int ng = 1000;

    @Override
    protected void paintComponent(Graphics gh) {
        for (int i = 0; i < 1000; i++){
            xg[i] = i+1;
        }
        Graphics2D drp = (Graphics2D)gh;
        //drp.drawLine(1, 100, 1000, 100);
        drp.drawString("Neuronet by Vaskorr", 1000 - 150, 20);
        System.out.println(l);
        if(l == 0){
            drp.drawString("Error is not null", l + 10, 40);
        }else{
            drp.drawString("Null error in : " + String.valueOf(l) + " epoch", l + 10, 40);
        }
        drp.drawLine(l, 10, l, 200);
        for (int i = 0; i <= 10; i++){
            drp.drawLine(0, i*10, 2, i*10);
            //drp.drawString("Ошибка (%)", 10, 0);
        }
        for (int i = 1; i <= 100; i++){
            //drp.drawString(String.valueOf(100-yg[i*10-1]), xg[i*10-1], yg[i*10-1]);
            //drp.drawString("Ошибка (%)", 10, 0);
        }
        //drp.drawLine(10, 10, 100, 100);
        //drp.drawLine(0, 0, 200, 200);
        drp.drawLine(0,100, 1000, 100);
        drp.drawPolyline(xg, yg[0], ng);
        drp.drawPolyline(xg, yg[1], ng);
        drp.drawPolyline(xg, yg[2], ng);
        drp.drawPolyline(xg, yg[3], ng);
        //drp.drawString("Neuronet", 10, 10);
    }
}

public class Neuronet extends JFrame{
    public ArrayList<double[]> layers;
    public ArrayList<double[][]> sinapses;
    public ArrayList<double[][]> deltas;
    public int answer;
    public double result;
    public double error;
    private double E;
    private double A;
    public static int[][] coords;
    public static int leng;
    public static int l;
    public static int crd;

    public Neuronet () {
        super("Результат обучения");
        JPanel jcp = new JPanel(new BorderLayout());
        setContentPane(jcp);
        jcp.add(new Drawing (), BorderLayout.CENTER);
        jcp.setBackground(Color.gray);
        setSize(1000, 200);
        setLocation(0, crd);
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void init(int[] lays, double dE, double dA){
        layers = new ArrayList<>();
        sinapses = new ArrayList<>();
        deltas = new ArrayList<>();
        for(int i = 0; i < lays.length; i++){
            layers.add(new double[lays[i]]);
        }
        for(int i = 0; i < lays.length-1; i++){
            sinapses.add(new double[lays[i]][lays[i+1]]);
            deltas.add(new double[lays[i]][lays[i+1]]);
        }
        E = dE;
        A = dA;
    }                                    // инициализация сети +
    public void fillWeights(){
        Random r = new Random();
        for (int j = 0; j < sinapses.size(); j++){
            for (int jj = 0; jj < sinapses.get(j).length; jj++){
                for (int jjj = 0; jjj < sinapses.get(j)[jj].length; jjj++){
                    sinapses.get(j)[jj][jjj] = Math.round(r.nextDouble()*1000)/1000.0;
                    deltas.get(j)[jj][jjj] = 0;
                }
            }
        }
    }                                                             // заполнение весов +
    public void setInput(int[] input){
        for (int i = 0; i < input.length - 1; i++){
            layers.get(0)[i] = input[i];
        }
        answer = input[input.length-1];
    }                                                     // заполнение входных данных +
    public double getResult(){
        for (int i = 1; i < layers.size(); i++){
            for (int j = 0; j < layers.get(i).length; j++){
                double f = 0;
                for (int h = 0; h < layers.get(i-1).length; h++){
                    f += layers.get(i-1)[h]*sinapses.get(i-1)[h][j];  //!!!
                }
                layers.get(i)[j] = sigmoid(f);
            }
        }
        result = layers.get(layers.size()-1)[layers.get(layers.size()-1).length-1];
        error = (Math.pow(answer - result, 2))/1;
        return layers.get(layers.size()-1)[layers.get(layers.size()-1).length-1];
    }                                                             // получение результата рассчета +
    public void calibrate(){
        double[] d = new double[1];
        d[0] = (answer - result)*((1-result)*result);
        //System.out.println("answer = " + answer + " result = " + result);
        //System.out.println("dOUT = " + d[0]);
        for (int i = layers.size()-2; i > -1; i--){
            //System.out.println("Working with layer " + i);
            double[] ds = new double[layers.get(i).length];
            //System.out.println("Neurons: " + layers.get(i).length);
            for (int j = 0; j < layers.get(i).length; j++){
                //System.out.println("Woking with neuron " + j);
                double g = 0;
                for (int h = 0; h < layers.get(i+1).length; h++){
                    g += d[h]*sinapses.get(i)[j][h];
                }
                double dd = ((1-layers.get(i)[j])*layers.get(i)[j])*g;
                //System.out.println("Neuron delta = " + dd);
                ds[j] = dd;
                for (int h = 0; h < sinapses.get(i)[j].length; h++){
                    double grad = layers.get(i)[j]*d[h];
                    double dw = E*grad+(A*deltas.get(i)[j][h]);
                    //System.out.println("Sinaps " + h + " delta = " + dw);
                    deltas.get(i)[j][h] = dw;
                    sinapses.get(i)[j][h] = sinapses.get(i)[j][h] + dw;
                }
            }
            d = ds;
        }
    }                                                               // калибровка весов +
    public void autotrain(int epoch_count, int[][] database) throws InterruptedException {
        System.out.println("Starting autotrain mode...");
        int[][] f = new int[database.length][epoch_count];
        l = 0;
        for(int e_count = 0; e_count < epoch_count; e_count++) {
            //System.out.println("---------- " + e_count + " ----------");
            //System.out.println();
            int a_mid = 0;
            for (int iteration = 0; iteration < database.length; iteration++) {
                setInput(database[iteration]);
                getResult();
                //System.out.println(Math.round(error*100));
                f[iteration][e_count] = 100-(int)Math.round(error*100);
                a_mid += (int)Math.round(error*100);
                calibrate();
            }
            a_mid /= 4;
            //System.out.println("Средняя ошибка: " + String.valueOf(a_mid));
            if(a_mid == 0 && l == 0){
                l = e_count;
                //System.out.println(leng);
            }
        }
        coords = f;
        Neuronet.printStat();
    } // автоматическая тренировка +
    public static double sigmoid(double x){
        double o = 1.0/(1+Math.pow(Math.E, -x));
        return o;
    }                                                // сигмоид +
    public static void printStat() throws InterruptedException {
        Neuronet g = new Neuronet();
        g.setVisible(true);
        //sleep(2000);
        //g.setVisible(false);
        crd += 200;
    }                           // вывод статистики обучения +
}