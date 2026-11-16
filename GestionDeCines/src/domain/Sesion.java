package domain;

public class Sesion {

    public static final int NUM_PLAZAS_MOVILIDAD = 8;
    public static final int FILA_MOVILIDAD = -1;

    private int id;
    private Pelicula pelicula;
    private Sala sala;
    private String fecha;
    private String hora;
    private double precio;
    private Butaca[][] butacas;
    private Butaca[] plazasMovilidad;

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

        plazasMovilidad = new Butaca[NUM_PLAZAS_MOVILIDAD];
        for (int i = 0; i < NUM_PLAZAS_MOVILIDAD; i++) {
            plazasMovilidad[i] = new Butaca(FILA_MOVILIDAD, i);
        }

        marcarButacasVip();
    }

    private void marcarButacasVip() {
        int filaMedio = sala.getFilas() / 2;
        int pasillo = sala.getColumnas() / 2;

        for (int f = filaMedio - 1; f <= filaMedio; f++) {
            for (int c = pasillo - 2; c <= pasillo + 1; c++) {
                butacas[f][c].setVip(true);
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

    public Butaca getPlazaMovilidad(int numero) {
        return plazasMovilidad[numero];
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