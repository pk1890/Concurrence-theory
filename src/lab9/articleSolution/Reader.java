package lab9.articleSolution;

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
        buffer.in.acquire();
        buffer.ctrin++;
        buffer.in.release();

        end = System.nanoTime();
        logger.addMeasurement(end-start, id, true);

        int ret = buffer.buffer;

        buffer.out.acquire();
        buffer.ctrout++;
        if(buffer.wait && buffer.ctrout == buffer.ctrin){
            buffer.wrt.release();
        }
        buffer.out.release();

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
