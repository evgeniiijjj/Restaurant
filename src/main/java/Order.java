public class Order {
    private static int number;
    private int id;

    public Order() {
        id = ++number;
    }

    public int getId() {
        return id;
    }
}
