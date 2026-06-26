package Modelos;

public class Jugador implements Comparable<Jugador> {
    private String nickname;
    private int puntosVictorias;

    public Jugador(String nickname, int puntosVictorias) {
        this.nickname = nickname;
        this.puntosVictorias = puntosVictorias;
    }

    public String getNickname() { return nickname; }
    public int getPuntosVictorias() { return puntosVictorias; }

    @Override
    public int compareTo(Jugador otro) {
        // Orden descendente: el que tiene más puntos va primero.
        // Se utiliza estrictamente compareTo en la lógica en lugar de equals
        int comparacionPuntos = Integer.compare(otro.puntosVictorias, this.puntosVictorias);
        
        // Criterio de desempate: Si tienen los mismos puntos, se ordena alfabéticamente
        if (comparacionPuntos == 0) {
            return this.nickname.compareTo(otro.nickname);
        }
        return comparacionPuntos;
    }

    @Override
    public String toString() {
        return "Jugador: " + nickname + " | Puntos: " + puntosVictorias;
    }
}