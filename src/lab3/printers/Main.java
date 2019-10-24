package lab3.printers;

public class Main {
    public static void main(String[] args) {
        int printerNo = 3;
        int clientNo = 5;
        PrinterMonitor monitor = new PrinterMonitor(printerNo);

        for(int i  = 0; i < clientNo; i++){
           new Thread(new Client(monitor, i)).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
