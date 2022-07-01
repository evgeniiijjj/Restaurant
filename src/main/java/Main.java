public class Main {
    static int orderCookingTime = 1000;
    static int orderDeliveryTime = 500;
    static int orderPlacingTime = 1000;
    static int mealTime = 2000;
    static long intervalBetweenVisitors = 500;
    static int customersNum = 5;
    static int cooksNum = 2;
    static int waitersNum = 3;

    public static void main(String[] args) throws InterruptedException {
        Restaurant restaurant = new Restaurant();
        System.out.println("Ресторан начал прием посетителей");
        for (int i = 0; i < cooksNum; i++) {
            restaurant.createThread(new Cook(restaurant, orderCookingTime), "Повар" + (i + 1));
        }
        for (int i = 0; i < waitersNum; i++) {
            restaurant.createThread(new Waiter(restaurant, orderDeliveryTime), "Официант" + (i + 1));
        }
        for (int i = 0; i < customersNum; i++) {
            Thread.sleep(intervalBetweenVisitors);
            restaurant.createThread(new Customer(restaurant, orderPlacingTime, mealTime), "Посетитель" + (i + 1));
        }
        Thread.sleep(5000);
        System.out.println("Ресторан завершил прием посетителей");
    }
}
