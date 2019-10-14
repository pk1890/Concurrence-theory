package lab2;

public class Shop {
    private CountingSemaphore carts;

    public Shop(int cartNo){
        carts = new CountingSemaphore(cartNo);
    }
    public void giveBasket(){
        carts.P();
    }
    public void returnBasket(){
        carts.V();
    }
}
