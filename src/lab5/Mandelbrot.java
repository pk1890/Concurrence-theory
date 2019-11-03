package lab5;

import jdk.internal.util.xml.impl.Pair;
import lab3.printers.Client;
import lab3.waiters.Colors;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.*;

import static java.lang.System.nanoTime;
import static java.lang.System.setOut;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 570;
    private final double ZOOM = 150;
    private BufferedImage I;
    private ExecutorService executor;

    private class Fragment{
        public int xMin;
        public int xMax;
        public int yMin;
        public int yMax;
        public List<Integer> data;

        public Fragment(int xMin, int xMax, int yMin, int yMax){
            this.xMax = xMax;
            this.xMin = xMin;
            this.yMax = yMax;
            this.yMin = yMin;
            data = new ArrayList<>();
        }

    }

    public Fragment calculateFragmentMandelbrot(int xMin, int xMax, int yMin, int yMax){
       Fragment result = new Fragment(xMin, xMax, yMin, yMax);
        double zx, zy, cX, cY, tmp;
        for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {
                zx = zy = 0;
                cX = (x - 400) / ZOOM;
                cY = (y - 300) / ZOOM;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                result.data.add(iter | (iter << 8));
            }
        }
        return result;

    }

    public Mandelbrot(int threads, TaskModes taskMode) {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        executor = Executors.newFixedThreadPool(threads);

        int PARTSy = 1, PARTSx = 1;

        switch (taskMode) {
            case OneToOne:
                PARTSx = 1;
                PARTSy = threads;
                break;
            case PixelIsTask:
                PARTSx = getWidth();
                PARTSy = getHeight();
                break;
            case TanTasksForThread:
                PARTSx = 5;
                PARTSy = 2*threads;
                break;

        }


        List<Future<Fragment>> values = new ArrayList<>();

        for (int i = 0; i < PARTSy; i++){
            for (int j = 0; j < PARTSx; j++){
                final int x = j;
                final int y = i;
                int finalPARTSx = PARTSx;
                int finalPARTSy = PARTSy;
                values.add(executor.submit( () -> {
                    return calculateFragmentMandelbrot(x*getWidth()/ finalPARTSx,
                            (x+1)*getWidth()/ finalPARTSx,
                            y*getHeight()/ finalPARTSy,
                            (y+1)*getHeight()/ finalPARTSy);
                }));
            }
        }

        for(int i = 0; i < PARTSx*PARTSy; i++){
            try {
                Fragment fragment = values.get(i).get();
                int counter = 0;
                for(int y = fragment.yMin; y < fragment.yMax; y++){
                    for(int x = fragment.xMin; x < fragment.xMax; x++){
                        I.setRGB(
                                x,
                                y,
                                fragment.data.get(counter)
                        );

                        counter++;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

//        for (int y = 0; y < getHeight(); y++) {
//            for (int x = 0; x < getWidth(); x++) {
//                executor.execute(() -> );
//                zx = zy = 0;
//                cX = (x - 400) / ZOOM;
//                cY = (y - 300) / ZOOM;
//                int iter = MAX_ITER;
//                while (zx * zx + zy * zy < 4 && iter > 0) {
//                    tmp = zx * zx - zy * zy + cX;
//                    zy = 2.0 * zx * zy + cY;
//                    zx = tmp;
//                    iter--;
//                }
//                I.setRGB(x, y, iter | (iter << 8));
//            }
//        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    private enum TaskModes{
        OneToOne,
        TanTasksForThread,
        PixelIsTask
    }

    private static class Stats {
        public double mean;
        public double stddev;
        public boolean isSpecial;

        public Stats(double mean, double stddev) {
            this.stddev = stddev;
            this.mean = mean;
            this.isSpecial = false;
        }

        @Override
        public String toString() {
            if(isSpecial)
                return Colors.ANSI_GREEN +  "( " + String.format("%3.2e", mean) + "; " + String.format("%3.2e", stddev) + " )" + Colors.ANSI_RESET;
            return "( " + String.format("%3.2e", mean) + "; " + String.format("%3.2e", stddev) + " )";
        }
    }

    public static void main(String[] args) {


//        new Mandelbrot(4, TaskModes.TanTasksForThread).setVisible(true);

        int [] threadCount = {1, 4, 8};

        TaskModes [] taskModes = TaskModes.values();

        Stats [][] statsList = new Stats[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                statsList[i][j] = new Stats(0,0);
            }
        }

        Stats bestMean, bestDeviation;

        bestDeviation = statsList[0][0];
        bestMean = statsList[0][0];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.println(i + ";"+j);
                List<Long> times = new ArrayList<>();
                long start, end;
                for (int iter = 0; iter < 10; iter++) {
                    start = System.nanoTime();
                    new Mandelbrot(threadCount[i], taskModes[j]);
                    end = System.nanoTime();
                    times.add(end - start);
                }

                float mean = times.stream().reduce((a, b) -> a+b).get()/(float)times.size();
                float stddev = 0;
                for(double num: times) {
                    stddev += Math.pow(num - mean, 2);
                }
                    statsList[i][j].mean = mean;
                    statsList[i][j].stddev = Math.sqrt(stddev);
                if(bestDeviation.stddev  > statsList[i][j].stddev) bestDeviation = statsList[i][j];
                if(bestMean.mean  > statsList[i][j].mean) bestMean = statsList[i][j];

            }
        }

        bestDeviation.isSpecial = true;
        bestMean.isSpecial = true;

//        System.out.println(statsList);
//        String format = "| %-15s | %-4d |%n";
        System.out.println("          | 1 task 1 thread | 10 task 1 thread | 1 task 1 pix");
        System.out.println("1 thread " + statsList[0][0] + statsList[0][1] + statsList [0][2]);
        System.out.println("4 threads " + statsList[1][0] + statsList[1][1] + statsList [1][2]);
        System.out.println("8 threads " + statsList[2][0] + statsList[2][1] + statsList [2][2]);

//
//        System.out.println(times);
//        System.out.println(mean);

    }


}

