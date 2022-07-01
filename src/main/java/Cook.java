public class Cook implements Runnable {
    private final Restaurant restraunt;
    private final long orderCookingTime;

    public Cook(Restaurant restaurant, int orderCookingTime) {
        this.restraunt = restaurant;
        this.orderCookingTime = orderCookingTime;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.printf("Повар %s прибыл и готовится к приему заказов\n", name);
        while (!thread.isInterrupted()) {
            Order order = restraunt.getFromAcceptedOrders();
            System.out.printf("Повар %s готовит заказ номер %d\n", name, order.getId());
            try {
                Thread.sleep(orderCookingTime);
            } catch (InterruptedException ignored) { }
            System.out.printf("Повар %s приготовил заказ номер %d\n", name, order.getId());
            restraunt.putInReadyOrders(order);
        }
        System.out.printf("Повар %s закончил работу\n", name);
    }
}
