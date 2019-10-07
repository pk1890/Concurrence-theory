public class Counter{
    private int val;

    public synchronized void inc(){
        val = val+1;
    }

    public int getVal(){
        return val;
    }

    public synchronized void dec() {
        val = val-1;
    }


    public Counter(){
        this.val = 0;
    }
}
