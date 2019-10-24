package lab3.printers;

public class Client implements Runnable {
    private final int id;
    private PrinterMonitor monitor;

    public Client(PrinterMonitor monitor, int id){
        this.monitor = monitor;
        this.id = id;
    }



    @Override
    public void run() {
       while (true){
           Printer p = null;
           try {
               System.out.println("[CLIENT " + this.id + " ] : Reserving printer");
               p = monitor.reservePrinter();
               System.out.println("[CLIENT " + this.id + " ] : RESERVED PRINTER no. " + p.id);
           } catch (InterruptedException e) {
               e.printStackTrace();
               return;
           }
           p.print();
           monitor.unlockPrinter(p);
           System.out.println("[CLIENT " + this.id + " ] : UNLOCKED PRINTER no. " + p.id);
           try {
               Thread.sleep(200);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }

}
