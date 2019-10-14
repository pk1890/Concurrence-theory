package lab2;

public class RepressedClient {
    private Shop AssignedShop;
    private int id;

    public RepressedClient(Shop AssignedShop, int id) {
        this.AssignedShop = AssignedShop;
        this.id = id;
    }

    public void  shop(){
        AssignedShop.giveBasket();
        System.out.println(id +":: Taken basket");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AssignedShop.returnBasket();
    }
}
