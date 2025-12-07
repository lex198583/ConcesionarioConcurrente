package com.leonel.concesionario;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Cliente extends Thread {
    private final String nombre;
    private final Semaphore semaforo;
    private final List<Vehiculo> poolVehiculos;
    private final int tiempoPrueba;

    public Cliente(String nombre, Semaphore semaforo, List<Vehiculo> poolVehiculos, int tiempoPrueba) {
        this.nombre = nombre;
        this.semaforo = semaforo;
        this.poolVehiculos = poolVehiculos;
        this.tiempoPrueba = tiempoPrueba;
    }

    @Override
    public void run() {
        Vehiculo asignado = null;
        try {
            semaforo.acquire(); // Solicita acceso
            synchronized (poolVehiculos) {
                if (!poolVehiculos.isEmpty()) {
                    asignado = poolVehiculos.remove(0);
                }
            }
            if (asignado != null) {
                System.out.printf("%s … probando vehículo … %d\n", nombre, asignado.getNumero());
                Thread.sleep(tiempoPrueba * 1000L);
                System.out.printf("%s … terminó de probar el vehículo … %d\n", nombre, asignado.getNumero());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (asignado != null) {
                synchronized (poolVehiculos) {
                    poolVehiculos.add(asignado);
                }
            }
            semaforo.release(); // Libera recurso
        }
    }
}
