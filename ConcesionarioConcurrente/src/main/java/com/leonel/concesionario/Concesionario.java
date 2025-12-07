package com.leonel.concesionario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Concesionario {
    public static void main(String[] args) {
        Semaphore semaforo = new Semaphore(4, true); // 4 vehículos
        List<Vehiculo> pool = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            pool.add(new Vehiculo(i));
        }

        int[] tiempos = {15, 10, 20, 5, 12, 8, 7, 18, 6};
        List<Thread> clientes = new ArrayList<>();

        for (int i = 0; i < tiempos.length; i++) {
            Cliente c = new Cliente("Cliente " + (i + 1), semaforo, pool, tiempos[i]);
            clientes.add(c);
            c.start();
        }

        for (Thread t : clientes) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Todos los clientes han terminado sus pruebas.");
    }
}
