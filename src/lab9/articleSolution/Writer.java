package lab9.articleSolution;

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
        buffer.in.acquire();
        buffer.out.acquire();
        if(buffer.ctrout == buffer.ctrin){
            buffer.out.release();
        }else {
            buffer.wait = true;
            buffer.out.release();
            buffer.wrt.acquire();
            buffer.wait = false;
        }
        end = System.nanoTime();
        logger.addMeasurement(end-start, id, false);
        buffer.buffer = no;
        buffer.in.release();
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
