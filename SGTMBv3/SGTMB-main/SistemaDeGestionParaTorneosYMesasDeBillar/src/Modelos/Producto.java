package Modelos;

public class Producto {
    private String nombre;
    private int precioVenta; // Lo que paga el cliente (El "Peso" de la mochila)
    private int margenGanancia; // Lo que gana el billar (El "Valor" a maximizar)

    public Producto(String nombre, int precioVenta, int margenGanancia) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.margenGanancia = margenGanancia;
    }

    public String getNombre() { return nombre; }
    public int getPrecioVenta() { return precioVenta; }
    public int getMargenGanancia() { return margenGanancia; }
    
    @Override
    public String toString() {
        return nombre + " (Precio: S/" + precioVenta + ", Ganancia Neta: S/" + margenGanancia + ")";
    }
}