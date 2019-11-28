package lab7.judge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Table {
    public final int FORKS_NO;
    public List<Semaphore> forks = new ArrayList<>();
    public Semaphore changingForks = new Semaphore(1);

    void init_forks(){
        for (int i = 0; i < FORKS_NO; i++){
            forks.add(new Semaphore(1));
        }
    }
    public Table(int forks_no){
        FORKS_NO = forks_no;
        init_forks();
    }
    public String getForksStatus(){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(Semaphore s: forks){
            builder.append(s.availablePermits());
        }
        builder.append("]");
        return builder.toString();
    }
}
