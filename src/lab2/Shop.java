package lab2;

import java.util.Stack;

public class Shop {
    private CountingSemaphore carts;

    private Stack<Basket> baskets;

    public Shop(int cartNo){
        carts = new CountingSemaphore(cartNo);
        baskets = new Stack<Basket>();
        for(int i = 0; i < cartNo; i++){
            baskets.push(new Basket(i));
        }
    }
    public Basket giveBasket(){
        carts.P();
        System.out.println("[SHOP] : basket gived. Free_baskets:" + carts.getValue());
        Basket basketToGive;
        synchronized (this){basketToGive = baskets.pop();}
        return basketToGive;
    }
    public void returnBasket(Basket basket){
        carts.V();
        synchronized (this) {baskets.push(basket);}
        System.out.println("[SHOP] : basket returned. Free_baskets:" + carts.getValue());
    }

}
