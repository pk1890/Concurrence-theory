package lab1;

public class Buffer {

    private String buff;



    public synchronized void put(String s) throws InterruptedException {
        while (buff != null){
            wait();
        }
        buff = s;
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (buff == null){
            wait();
        }
        String tmp;
        tmp = buff;
        buff = null;
        notifyAll();
        return tmp;

    }
}
