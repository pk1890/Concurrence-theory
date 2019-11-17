package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    public int size;
    public int [] data;
    public int [] processingStep;
    public int producerPos, consumerPos;


    private List<Integer> processorPos;
    private List<Lock> bufferCellLock;
    private List<Condition> bufferCellCondition;

    public Buffer(int size){
        this.size = size;
        this.data = new int[size];
        this.processingStep = new int[size];

        this.bufferCellCondition = new ArrayList<>();
        this.bufferCellLock = new ArrayList<>();
        this.processorPos = new ArrayList<>();

        this.producerPos = 0;
        this.consumerPos = 0;

        for(int i = 0; i < size; i++){
            processingStep[i] = -1;
            bufferCellLock.add(new ReentrantLock());
            bufferCellCondition.add(bufferCellLock.get(i).newCondition());
        }
    }

    public int registerProcessor(){
        int id = processorPos.size();
        processorPos.add(0);
        return id;
    }

    public void produce(){
        Lock currLock = bufferCellLock.get(producerPos);
        currLock.lock();

        try {
            while(processingStep[producerPos] != -1){
                bufferCellCondition.get(producerPos).await();
            }
            data[producerPos] = 0;
            processingStep[producerPos] = 0;
            System.out.println(" [ "+ producerPos +" ] Producing...");
            bufferCellCondition.get(producerPos).signalAll();
            producerPos = producerPos + 1 == size ? 0 : producerPos+1;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            currLock.unlock();
        }

    }

    public void consume(){
        Lock currLock = bufferCellLock.get(consumerPos);
        currLock.lock();

        try {
            while(processingStep[consumerPos] != processorPos.size()){
                bufferCellCondition.get(consumerPos).await();
            }
            System.out.println(" [ "+ consumerPos +" ] Consuming value... " + data[consumerPos]);
            data[consumerPos] = 0;
            processingStep[consumerPos] = -1;
            bufferCellCondition.get(consumerPos).signalAll();
            consumerPos = consumerPos + 1 == size ? 0 : consumerPos+1;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            currLock.unlock();
        }

    }


    public void process(Processor processor){
        int position = processorPos.get(processor.getId());
        Lock currLock = bufferCellLock.get(position);
        currLock.lock();

        try {
            while(processingStep[position] != processor.getNumberInSequence()){
                bufferCellCondition.get(position).await();
            }
            data[position]++;
            processingStep[position]++;
            System.out.println(" [ "+ position +" ] Processing step "+ processor.getNumberInSequence() + " ...");
            bufferCellCondition.get(position).signalAll();
            processorPos.set(processor.getId(), position + 1 == size ? 0 : position + 1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            currLock.unlock();
        }
    }
}
