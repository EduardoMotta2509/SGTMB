package Modelos;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;

public class Mesa {
    private int numero;
    private boolean ocupada;
    private String horaInicio;

    private static final double TARIFA_POR_HORA = 12.0;

    public Mesa(int numero) {
        this.numero = numero;
        this.ocupada = false;
        this.horaInicio = "-";
    }

    public int getNumero() {
        return numero;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public double calcularPrecio(String horaFin) {
        if (this.horaInicio.equals("-")) {
            return 0.0;
        }

        try {
            LocalTime inicio = LocalTime.parse(this.horaInicio);
            LocalTime fin = LocalTime.parse(horaFin);

            // Calculamos la diferencia exacta en minutos
            long minutosJugados = ChronoUnit.MINUTES.between(inicio, fin);

            // Manejo del caso en el que la partida cruza la medianoche (ej. inicio 23:00, fin 01:00)
            if (minutosJugados < 0) {
                minutosJugados += 24 * 60; // Se le suma un día completo en minutos
            }

            // Cálculo matemático del precio: (minutos totales * tarifa por hora) / 60
            double tarifaPorMinuto = TARIFA_POR_HORA / 60.0;
            return minutosJugados * tarifaPorMinuto;

        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de hora inválido. Asegúrese de usar HH:mm (ej. 15:45)");
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return "Mesa N° " + numero + " | Estado: " + (ocupada ? "OCUPADA (Desde: " + horaInicio + ")" : "DISPONIBLE");
    }
}