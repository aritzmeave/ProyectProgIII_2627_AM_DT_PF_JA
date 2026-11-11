package domain;

public class Pelicula {
	
    private int id;           
    private String titulo;
    private String director;
    private int duracion;
    private Genero genero;
    private Clasificacion clasificacion;
 
    public Pelicula(int id, String titulo, String director, int duracion,
                    Genero genero, Clasificacion clasificacion) {
        this.id = id;
        setTitulo(titulo);
        setDirector(director);
        setDuracion(duracion);
        this.genero = genero;
        this.clasificacion = clasificacion;
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getTitulo() {
        return titulo;
    }
 
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio.");
        }
        this.titulo = titulo;
    }
 
    public String getDirector() {
        return director;
    }
 
    public void setDirector(String director) {
        if (director == null || director.isEmpty()) {
            throw new IllegalArgumentException("El director no puede estar vacio.");
        }
        this.director = director;
    }
 
    public int getDuracion() {
        return duracion;
    }
 
    public void setDuracion(int duracion) {
        if (duracion < 1 || duracion > 300) {
            throw new IllegalArgumentException("La duracion debe estar entre 1 y 300 minutos.");
        }
        this.duracion = duracion;
    }
 
    public Genero getGenero() {
        return genero;
    }
 
    public void setGenero(Genero genero) {
        this.genero = genero;
    }
 
    public Clasificacion getClasificacion() {
        return clasificacion;
    }
 
    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }
 
    @Override
    public String toString() {
        return titulo + " (" + duracion + " mins)";
    }
}
