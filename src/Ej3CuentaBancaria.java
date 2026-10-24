import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Ej3CuentaBancaria {
    public static void main(String[] args) throws InterruptedException {
        CuentaBancaria cuenta = new CuentaBancaria();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 40; i++) executor.execute(new Transaccion(cuenta, 100));
        for (int i = 0; i < 20; i++) executor.execute(new Transaccion(cuenta, 50));
        for (int i = 0; i < 60; i++) executor.execute(new Transaccion(cuenta, 20));
        for (int i = 0; i < 40; i++) executor.execute(new Transaccion(cuenta, -100));
        for (int i = 0; i < 20; i++) executor.execute(new Transaccion(cuenta, -50));
        for (int i = 0; i < 60; i++) executor.execute(new Transaccion(cuenta, -20));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Saldo final en la cuenta: " + cuenta.getSaldo() + " €");
    }
}

class CuentaBancaria {
    private int saldo = 100;
    public synchronized void operar(int cantidad) {
        saldo += cantidad;
    }
    public int getSaldo() {
        return saldo;
    }
}

class Transaccion implements Runnable {
    private CuentaBancaria cuenta;
    private int cantidad;
    public Transaccion(CuentaBancaria cuenta, int cantidad) {
        this.cuenta = cuenta;
        this.cantidad = cantidad;
    }
    public void run() {
        cuenta.operar(cantidad);
    }
}