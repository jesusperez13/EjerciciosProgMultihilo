import java.util.concurrent.Semaphore;

public class Ej6Filosofos {
    public static void main(String[] args) {
        int numFilosofos = 5;
        Semaphore[] tenedores = new Semaphore[numFilosofos];
        for (int i = 0; i < numFilosofos; i++) {
            tenedores[i] = new Semaphore(1);
        }
        Semaphore limitadorMesa = new Semaphore(numFilosofos - 1);
        for (int i = 0; i < numFilosofos; i++) {
            Semaphore izq = tenedores[i];
            Semaphore der = tenedores[(i + 1) % numFilosofos];
            new Thread(new Filosofo(i + 1, izq, der, limitadorMesa)).start();
        }
    }
}

class Filosofo implements Runnable {
    private int id;
    private Semaphore tenedorIzquierda;
    private Semaphore tenedorDerecha;
    private Semaphore limitadorMesa;
    public Filosofo(int id, Semaphore tenedorIzquierda, Semaphore tenedorDerecha, Semaphore limitadorMesa) {
        this.id = id;
        this.tenedorIzquierda = tenedorIzquierda;
        this.tenedorDerecha = tenedorDerecha;
        this.limitadorMesa = limitadorMesa;
    }
    public void run() {
        try {
            while (true) {
                System.out.println("El filosofo " + id + " está pensando");
                Thread.sleep(1000 + (long)(Math.random() * 4000));
                limitadorMesa.acquire();
                tenedorIzquierda.acquire();
                tenedorDerecha.acquire();
                System.out.println("Filosofo " + id + " comiendo");
                Thread.sleep(1500);
                tenedorDerecha.release();
                tenedorIzquierda.release();
                limitadorMesa.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}