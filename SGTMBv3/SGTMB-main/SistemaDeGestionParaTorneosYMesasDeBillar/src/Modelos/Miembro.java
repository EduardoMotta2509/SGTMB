package Modelos;

public class Miembro implements Comparable<Miembro> {
    private String id;
    private String nombre;

    public Miembro(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public int compareTo(Miembro otro) {
        // Obligatorio para estructuras genéricas (como el AVL): 
        // Usar estrictamente compareTo en lugar de equals para las comparaciones.
        return this.id.compareTo(otro.getId());
    }

    @Override
    public String toString() {
        return id + " (" + nombre + ")";
    }
}