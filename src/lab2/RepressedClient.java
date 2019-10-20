package lab2;

public class RepressedClient implements Runnable{
    private Shop AssignedShop;
    private int id;
    private Basket basket;

    public RepressedClient(Shop AssignedShop, int id) {
        this.AssignedShop = AssignedShop;
        this.id = id;
        basket = null;
    }

    public void  shop() throws InterruptedException {
        basket = AssignedShop.giveBasket();
        System.out.println(id +":: Taken basket no. " + basket.getNumber());
        try {
            System.out.println(id+ ":: Shopping...");
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AssignedShop.returnBasket(basket);
        System.out.println(id +" :: Basket no. " + basket.getNumber()+ " returned");
        basket = null;
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
