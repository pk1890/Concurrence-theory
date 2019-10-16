package lab2;

public class Main {
    private static final int Client_count = 5;
    public static void main(String[] args) throws InterruptedException {
        Shop biedronka = new Shop(3);
        RepressedClient [] repressedClients = new RepressedClient[Client_count];
        Thread [] clientThread = new Thread[Client_count];


        for (int i = 0; i < Client_count; i++){
            repressedClients[i] = new RepressedClient(biedronka, i);
            clientThread[i] = new Thread(repressedClients[i]);
            clientThread[i].start();
        }

        Thread.sleep(5);
        System.exit(0);

    }
}
