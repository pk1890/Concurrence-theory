package lab2;

public class RepressedClient implements Runnable{
    private Shop AssignedShop;
    private int id;

    public RepressedClient(Shop AssignedShop, int id) {
        this.AssignedShop = AssignedShop;
        this.id = id;
    }

    public void  shop() throws InterruptedException {
        AssignedShop.giveBasket();
        System.out.println(id +":: Taken basket");
        try {
            System.out.println(id+ ":: Shopping...");
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AssignedShop.returnBasket();
        System.out.println(id +" :: Basket returned");
        Thread.sleep(1);
    }

    @Override
    public void run() {
        while (true){
            try {
                shop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
