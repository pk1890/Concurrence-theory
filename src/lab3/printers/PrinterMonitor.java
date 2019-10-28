package lab3.printers;

import lab3.waiters.Colors;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterMonitor {

    private List<Printer> printers = new LinkedList<Printer>();
    private int lastPrinter = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition allReserved = lock.newCondition();
    private int reservedPrinters;

    public PrinterMonitor(int printerNo){
        for(int i = 0; i < printerNo; i++){
            printers.add(new Printer(i));
        }
        reservedPrinters = 0;
    }

    public Printer reservePrinter() throws InterruptedException {
        lock.lock();
        while (reservedPrinters == printers.size()){
            System.out.println(Colors.ANSI_GREEN  + "Waiting for any printer to free" +Colors.ANSI_RESET);
            allReserved.await();
        }
        while(printers.get(lastPrinter).reserved){
            lastPrinter++;
            if(lastPrinter >= printers.size()) lastPrinter = 0;
        }
        reservedPrinters++;
        Printer p = printers.get(lastPrinter);
        p.reserved = true;
        lock.unlock();
        return p;

    }

    public void unlockPrinter(Printer p) {
        lock.lock();
        p.reserved = false;
        reservedPrinters--;
        allReserved.signalAll();
        lock.unlock();
    }
}
