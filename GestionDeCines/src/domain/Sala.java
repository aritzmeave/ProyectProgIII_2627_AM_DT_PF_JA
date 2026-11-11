package domain;

public class Sala {
	private int numero;
    private int filas;
    private int columnas;
 
    public Sala(int numero, int filas, int columnas) {
        this.numero = numero;
        this.filas = filas;
        this.columnas = columnas;
    }
 
    public int getNumero() {
        return numero;
    }
 
    public void setNumero(int numero) {
        this.numero = numero;
    }
 
    public int getFilas() {
        return filas;
    }
 
    public void setFilas(int filas) {
        this.filas = filas;
    }
 
    public int getColumnas() {
        return columnas;
    }
 
    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }
 
    public int getCapacidad() {
        return filas * columnas;
    }
 
    @Override
    public String toString() {
        return "Sala " + numero;
    }
}
