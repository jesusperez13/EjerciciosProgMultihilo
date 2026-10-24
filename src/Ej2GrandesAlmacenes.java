import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Ej2GrandesAlmacenes {
    public static void main(String[] args) {
        Almacen almacen = new Almacen();
        ExecutorService executor = Executors.newFixedThreadPool(50);
        for (int i = 1; i <= 300; i++) {
            executor.execute(new Cliente(i, almacen));
        }
        executor.shutdown();
    }
}

class Almacen {
    private int stock = 100;
    private final ReentrantLock puertaLock = new ReentrantLock();
    public boolean intentarEntrar() {
        try {
            return puertaLock.tryLock(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }
    public void salirYComprar() {
        try {
            if (stock > 0) {
                stock--;
                System.out.println(Thread.currentThread().getName() + " entró y consiguió un producto. Quedan: " + stock);
            } else {
                System.out.println(Thread.currentThread().getName() + " entró, pero ya no quedan productos");
            }
        } finally {
            puertaLock.unlock();
        }
    }
}

class Cliente implements Runnable {
    private int id;
    private Almacen almacen;
    public Cliente(int id, Almacen almacen) {
        this.id = id;
        this.almacen = almacen;
    }
    public void run() {
        int intentos = 0;
        boolean entro = false;
        while (intentos < 10 && !entro) {
            entro = almacen.intentarEntrar();
            if (!entro) {
                intentos++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        if (entro) {
            almacen.salirYComprar();
        } else {
            System.out.println("Cliente " + id + " desistió tras 10 intentos y se marchó");
        }
    }
}