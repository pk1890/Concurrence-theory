package lab3.out;


public class Producer implements Runnable {
    private Buffer buffer;
    private BoundedBuffer buf;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }
    public Producer(BoundedBuffer buf){
        this.buf =  buf;
    }

    public void run() {

        for(int i = 0;  i < 10;   i++) {
           // try {
//                buffer.putSem("message "+i);
            try {
                buf.put(new String("Message" + i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //   } catch (InterruptedException e) {
           //     e.printStackTrace();
            //}
        }

    }
}
