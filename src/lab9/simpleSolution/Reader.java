package lab9.simpleSolution;

import lab9.Logger;

public class Reader implements Runnable{
    private final Buffer buffer;
    private final int id;
    private final Logger logger;

    public Reader(Buffer buffer, int id, Logger logger){
        this.id = id;
        this.buffer = buffer;
        this.logger = logger;
    }

    public int read() throws InterruptedException {
        long start, end;
        start = System.nanoTime();
        buffer.mutex.acquire();
        if(++buffer.counter == 1) buffer.writing.acquire();
        buffer.mutex.release();
        end = System.nanoTime();
        logger.addMeasurement(end-start, id, true);

        int ret = buffer.buffer;

        buffer.mutex.acquire();
        if(--buffer.counter == 0) buffer.writing.release();
        buffer.mutex.release();

        return ret;
    }

    @Override
    public void run() {
        while (true) {
            try {
                read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
