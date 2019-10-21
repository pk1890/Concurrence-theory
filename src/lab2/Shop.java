package lab2;

import java.util.Stack;

public class Shop {
    private CountingSemaphore carts;
    private BinarySemaphore stack_sem;

    private Stack<Basket> baskets;

    public Shop(int cartNo){
        carts = new CountingSemaphore(cartNo);
        stack_sem = new BinarySemaphore();
        baskets = new Stack<Basket>();
        for(int i = 0; i < cartNo; i++){
            baskets.push(new Basket(i));
        }
    }
    public Basket giveBasket(){
        carts.P();
        System.out.println("[SHOP] : basket gived. Free_baskets:" + carts.getValue());
        Basket basketToGive;
        stack_sem.P();
        basketToGive = baskets.pop();
        stack_sem.V();
        return basketToGive;
    }
    public void returnBasket(Basket basket){
        carts.V();
        stack_sem.P();
        baskets.push(basket);
        stack_sem.V();
        System.out.println("[SHOP] : basket returned. Free_baskets:" + carts.getValue());
    }

}
