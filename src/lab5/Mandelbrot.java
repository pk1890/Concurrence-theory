package lab5;

import lab3.printers.Client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 570;
    private final double ZOOM = 150;
    private BufferedImage I;
    private ExecutorService executor;
    private double zx, zy, cX, cY, tmp;

    public List<Integer> calculateFragmentMandelbrot(int xMin, int xMax, int yMin, int yMax){
        List<Integer> result = new LinkedList<>();
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
                result.add(iter | (iter << 8));
            }
        }
        return result;

    }

    public Mandelbrot() {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        executor = Executors.newFixedThreadPool(16);
//
//
    int PARTS = 4;

    List<Future<List<Integer>>> values = new LinkedList<>();

        for (int i = 0; i < PARTS; i++){
            for (int j = 0; j < PARTS; j++){
                final int x = j;
                final int y = i;
                values.add(executor.submit( () -> {
                    return calculateFragmentMandelbrot(x*getWidth()/PARTS,
                            (x+1)*getWidth()/PARTS,
                            y*getHeight()/PARTS,
                            (y+1)*getHeight()/PARTS);
                }));
            }
        }

        for(int i = 0; i < PARTS*PARTS; i++){
            try {
                List <Integer> list = values.get(i).get();
                int counter = 0;
                for(int y = i/PARTS *getHeight()/PARTS; y < (i+1)/PARTS *getHeight()/PARTS; y++){
                    for(int x = i%PARTS *getWidth()/PARTS; x < (i+1)%PARTS *getWidth()/PARTS; x++){
                        I.setRGB(
                                x,
                                y,
                                list.get(counter)
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

    public static void main(String[] args) {
        new Mandelbrot().setVisible(true);
    }
}

