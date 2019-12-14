package lab9.simpleSolution;

import lab9.Logger;

public class Writer implements Runnable{
    private final Buffer buffer;
    private final int id;
    private final Logger logger;

    public Writer(Buffer buffer, int id, Logger logger){
        this.id = id;
        this.buffer = buffer;
        this.logger = logger;
    }

    public void write(int no) throws InterruptedException {
        long start, end;
        start = System.nanoTime();
        buffer.writing.acquire();
        end = System.nanoTime();
        logger.addMeasurement(end-start, id, false);
        buffer.buffer = no;
        buffer.writing.release();
    }

    @Override
    public void run() {
        while (true) {
            try {
                write(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
