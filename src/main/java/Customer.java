public class Customer implements Runnable {
    private final Restaurant restaurant;
    private final long orderPlacingTime;
    private final long mealTime;

    public Customer(Restaurant restaurant, int orderPlacingTime, int mealTime) {
        this.restaurant = restaurant;
        this.orderPlacingTime = orderPlacingTime;
        this.mealTime = mealTime;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.printf("Посетитель %s вошел в ресторан, занял свободный столик, ознакомился с меню и занялся составлением заказа\n", name);
        try {
            Thread.sleep(orderPlacingTime);
        } catch (InterruptedException ignored) { }
        Order order = new Order();
        restaurant.addWaitingOrders(order);
        System.out.printf("Посетитель %s составил заказ номер %d и передал официанту\n", name, order.getId());
        restaurant.customerWait(order);
        System.out.printf("Посетитель %s получил заказ номер %d и приступил к приему пищи\n", name, order.getId());
        try {
            Thread.sleep(mealTime);
        } catch (InterruptedException ignored) { }
        System.out.printf("Посетитель %s закончил прием пищи и покинул ресторан\n", name);
    }
}
