package lab1;

public class Consumer implements Runnable {
    private Buffer buffer;
    private  int id;
    public Consumer(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    public void run() {

        for(int i = 0;  i < 5;   i++) {
            //try {
                String message = buffer.takeSem();
                System.out.println(message + "::"+id);
            //} catch (InterruptedException e) {
             //   e.printStackTrace();
            //}
        }

    }
}
