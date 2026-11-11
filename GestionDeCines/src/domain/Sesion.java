package domain;

public class Sesion {
	private int id;
    private Pelicula pelicula;
    private Sala sala;
    private String fecha;
    private String hora;
    private double precio;
    private Butaca[][] butacas;
 
    public Sesion(int id, Pelicula pelicula, Sala sala, String fecha, String hora, double precio) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
 
        butacas = new Butaca[sala.getFilas()][sala.getColumnas()];
        for (int f = 0; f < sala.getFilas(); f++) {
            for (int c = 0; c < sala.getColumnas(); c++) {
                butacas[f][c] = new Butaca(f, c);
            }
        }
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public Pelicula getPelicula() {
        return pelicula;
    }
 
    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }
 
    public Sala getSala() {
        return sala;
    }
 
    public String getFecha() {
        return fecha;
    }
 
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
 
    public String getHora() {
        return hora;
    }
 
    public void setHora(String hora) {
        this.hora = hora;
    }
 
    public double getPrecio() {
        return precio;
    }
 
    public void setPrecio(double precio) {
        this.precio = precio;
    }
 

    public Butaca getButaca(int fila, int columna) {
        return butacas[fila][columna];
    }
 
    public int getNumOcupadas() {
        int contador = 0;
        for (int f = 0; f < sala.getFilas(); f++) {
            for (int c = 0; c < sala.getColumnas(); c++) {
                if (butacas[f][c].getEstado() == EstadoButaca.OCUPADA) {
                    contador++;
                }
            }
        }
        return contador;
    }
 
    public double getPorcentajeOcupacion() {
        return getNumOcupadas() * 100.0 / sala.getCapacidad();
    }
 
    @Override
    public String toString() {
        return pelicula.getTitulo() + " - " + sala + " - " + fecha + " " + hora;
    }
}
