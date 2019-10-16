package lab2;

public class Shop {
    private CountingSemaphore carts;

    public Shop(int cartNo){
        carts = new CountingSemaphore(cartNo);
    }
    public void giveBasket(){
        carts.P();
        System.out.println("[SHOP] : basket gived. Free_baskets:" + carts.getValue());
    }
    public void returnBasket(){
        carts.V();
        System.out.println("[SHOP] : basket returned. Free_baskets:" + carts.getValue());
    }

}
