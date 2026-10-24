import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ej1Cafeteria {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 5; i++) {
            executor.execute(new Pedido(i));
        }
        executor.shutdown();
    }
}

class Pedido implements Runnable {
    private int numero;

    public Pedido(int numero) {
        this.numero = numero;
    }

    public void run() {
        System.out.println("El trabajador (" + Thread.currentThread().getName() + ") está preparando el pedido " + numero);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Pedido " + numero + " finalizado por " + Thread.currentThread().getName());
    }
}