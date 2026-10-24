import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Ej4ProductoresYConsumidores {
    public static void main(String[] args) {
        ColaLimitada cola = new ColaLimitada(5);
        new Thread(new Productor(cola), "Productor1").start();
        new Thread(new Consumidor(cola), "Consumidor1").start();
    }
}

class ColaLimitada {
    private Queue<Integer> cola = new LinkedList<>();
    private int capacidad;
    public ColaLimitada(int capacidad) {
        this.capacidad = capacidad;
    }
    public synchronized void producir(int valor) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(valor);
        System.out.println("Producido: " + valor + " | Tamaño cola: " + cola.size());
        notifyAll();
    }
    public synchronized int consumir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        int valor = cola.poll();
        System.out.println("Consumido: " + valor + " | Tamaño cola: " + cola.size());
        notifyAll();
        return valor;
    }
}

class Productor implements Runnable {
    private ColaLimitada cola;
    public Productor(ColaLimitada cola) {
        this.cola = cola;
    }
    public void run() {
        Random rand = new Random();
        try {
            for (int i = 0; i < 10; i++) {
                cola.producir(rand.nextInt(100));
                Thread.sleep(rand.nextInt(500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumidor implements Runnable {
    private ColaLimitada cola;
    public Consumidor(ColaLimitada cola) {
        this.cola = cola;
    }
    public void run() {
        Random rand = new Random();
        try {
            for (int i = 0; i < 10; i++) {
                cola.consumir();
                Thread.sleep(rand.nextInt(800));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}