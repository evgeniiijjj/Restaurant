public class Waiter implements Runnable {
    private final Restaurant restaurant;
    private final long orderDeliveryTime;

    public Waiter(Restaurant restaurant, int orderDeliveryTime) {
        this.restaurant = restaurant;
        this.orderDeliveryTime = orderDeliveryTime;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        String name = thread.getName();
        System.out.printf("Официант %s прибыл и ожидает приема заказов от посетителей\n", name);
        while (!thread.isInterrupted()) {
            Order order = restaurant.getFromWaitingOrders();
            if (order != null) {
                System.out.printf("Официант %s принял заказ номер %d и передал на кухню\n", name, order.getId());
                restaurant.putInAcceptedOrders(order);
                System.out.printf("Официант %s проверяет наличие готовых заказов\n", name);
            }
            order = restaurant.getFromReadyOrders();
            if (order != null) {
                System.out.printf("Официант %s принял готовый заказ номер %d и направился к Посетителю%d\n", name, order.getId(), order.getId());
                try {
                    Thread.sleep(orderDeliveryTime);
                } catch (InterruptedException ignored) {
                }
                System.out.printf("Официант %s доставил заказ номер %d посетителю\n", name, order.getId());
                restaurant.deliverOrder(order);
            }
        }
        System.out.printf("Официант %s закончил работу\n", name);
    }
}
