package lab4;

public class Processor implements Runnable {
    private int numberInSequence;
    private int id;
    private Buffer buff;
    private int sleepTime;

    public int getNumberInSequence() {
        return numberInSequence;
    }

    public int getId() {
        return id;
    }

    public Processor(int numberInSequence, Buffer buff, int sleepTime){
        this.numberInSequence = numberInSequence;
        this.buff = buff;
        this.id = buff.registerProcessor();
        this.sleepTime = sleepTime;
        System.out.println("Processor no "+ getNumberInSequence() +" registered at id "+ getId());
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(sleepTime);
                buff.process(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
