import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {
    private final Deque<Order> waitingOrders = new LinkedList<>();
    private final Deque<Order> acceptedOrders = new LinkedList<>();
    private final Deque<Order> readyOrders = new LinkedList<>();

    private final ReentrantLock lockForWaitingOrders = new ReentrantLock();
    private final ReentrantLock lockForAcceptedOrders = new ReentrantLock();
    private final ReentrantLock lockForReadyOrders = new ReentrantLock();

    private final Condition conditionForWaitingOrders = lockForWaitingOrders.newCondition();
    private final Condition conditionForAcceptedOrders = lockForAcceptedOrders.newCondition();

    public void addWaitingOrders(Order order) {
        lockForWaitingOrders.lock();
        try {
            waitingOrders.addLast(order);
            conditionForWaitingOrders.signalAll();
        } finally {
            lockForWaitingOrders.unlock();
        }
    }

    public void customerWait(Order order) {
        synchronized (order) {
            try {
                order.wait();
            } catch (InterruptedException ignored) { }
        }
    }

    public Order getFromWaitingOrders() {
        Order order = null;
        if (lockForWaitingOrders.tryLock()) {
            try {
                if (waitingOrders.size() > 0) {
                    order = waitingOrders.pollFirst();
                }
            } finally {
                lockForWaitingOrders.unlock();
            }
        }
        return order;
    }

    public void putInAcceptedOrders(Order order) {
        lockForAcceptedOrders.lock();
        try {
            acceptedOrders.addLast(order);
            conditionForAcceptedOrders.signalAll();
        } finally {
            lockForAcceptedOrders.unlock();
        }
    }

    public Order getFromAcceptedOrders() {
        Order order;
        lockForAcceptedOrders.lock();
        while (acceptedOrders.size() == 0) {
            try {
                conditionForAcceptedOrders.await();
            } catch (InterruptedException ignored) { }
        }
        try {
            order = acceptedOrders.pollFirst();
        } finally {
            lockForAcceptedOrders.unlock();
        }
        return order;
    }

    public void putInReadyOrders(Order order) {
        lockForReadyOrders.lock();
        try {
            readyOrders.addLast(order);
        } finally {
            lockForReadyOrders.unlock();
        }
    }

    public Order getFromReadyOrders() {
        Order order = null;
        if (lockForReadyOrders.tryLock()) {
            try {
                if (readyOrders.size() > 0) {
                    order = readyOrders.pollFirst();
                }
            } finally {
                lockForReadyOrders.unlock();
            }
        }
        return order;
    }

    public void deliverOrder(Order order) {
        synchronized (order) {
            order.notify();
        }
    }

    public void createThread(Runnable runnable, String name) {
        Thread thread = new Thread(runnable, name);
        thread.setDaemon(true);
        thread.start();
    }
}
