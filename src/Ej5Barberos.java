import java.util.concurrent.Semaphore;

public class Ej5Barberos {
    public static void main(String[] args) {
        Barberia barberia = new Barberia();
        for (int i = 1; i <= 2; i++) {
            final int idB = i;
            new Thread(() -> barberia.siguienteCliente(idB), "Barbero" + i).start();
        }
        for (int i = 1; i <= 10; i++) {
            final int idC = i;
            new Thread(() -> barberia.entrarCliente(idC), "Cliente" + i).start();
            try { Thread.sleep(600); } catch (InterruptedException e) {}
        }
    }
}

class Barberia {
    private final int NUM_SILLAS = 3;
    private Semaphore sillasEspera = new Semaphore(NUM_SILLAS);
    private Semaphore barberosListos = new Semaphore(0);
    private Semaphore clientesListos = new Semaphore(0);
    private Semaphore asientoBarbero = new Semaphore(0);
    public boolean entrarCliente(int idCliente) {
        if (sillasEspera.tryAcquire()) {
            System.out.println("Cliente " + idCliente + " se sienta en la sala de espera");
            try {
                clientesListos.release();
                barberosListos.acquire();
                asientoBarbero.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sillasEspera.release();
            return true;
        } else {
            System.out.println("Cliente " + idCliente + " llega, no ve sillas libres y se marcha");
            return false;
        }
    }
    public void siguienteCliente(int idBarbero) {
        try {
            while (true) {
                System.out.println("Barbero " + idBarbero + " duerme esperando clientes");
                clientesListos.acquire();
                barberosListos.release();
                asientoBarbero.release();
                System.out.println("Barbero " + idBarbero + " está afeitando a un cliente");
                Thread.sleep(1500);
                System.out.println("Barbero " + idBarbero + " ha terminado el afeitado");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}