
package domain;

import java.util.ArrayList;

public class DatosParaCines {
    private ArrayList<Pelicula> peliculas = new ArrayList<>();
    private ArrayList<Sala> salas = new ArrayList<>();
    private ArrayList<Sesion> sesiones = new ArrayList<>();

    public DatosParaCines() {
        crearPeliculas();
        crearSalas();
        crearSesiones();
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public ArrayList<Sesion> getSesiones() {
        return sesiones;
    }

    private void crearPeliculas() {
        peliculas.add(new Pelicula(1, "Dune: Parte Dos", "Denis Villeneuve", 166, Genero.CIENCIA_FICCION, Clasificacion.MAS_12));
        peliculas.add(new Pelicula(2, "Del revés 2", "Kelsey Mann", 96, Genero.ANIMACION, Clasificacion.TP));
        peliculas.add(new Pelicula(3, "Oppenheimer", "Christopher Nolan", 180, Genero.DRAMA, Clasificacion.MAS_12));
        peliculas.add(new Pelicula(4, "Gladiator II", "Ridley Scott", 148, Genero.ACCION, Clasificacion.MAS_16));
        peliculas.add(new Pelicula(5, "Smile 2", "Parker Finn", 127, Genero.TERROR, Clasificacion.MAS_18));
        peliculas.add(new Pelicula(6, "Robot salvaje", "Chris Sanders", 102, Genero.ANIMACION, Clasificacion.TP));
    }

    private void crearSalas() {
        salas.add(new Sala(1, 8, 10));
        salas.add(new Sala(2, 10, 12));
        salas.add(new Sala(3, 6, 8));
    }

    private void crearSesiones() {
        Pelicula dune = peliculas.get(0);
        Pelicula delReves = peliculas.get(1);
        Pelicula oppenheimer = peliculas.get(2);
        Pelicula gladiator = peliculas.get(3);
        Pelicula smile = peliculas.get(4);
        Pelicula robot = peliculas.get(5);

        Sala sala1 = salas.get(0);
        Sala sala2 = salas.get(1);
        Sala sala3 = salas.get(2);

        anadirSesion(1, dune, sala1, "10/11/2026", "16:00", 7.50, 20);
        anadirSesion(2, delReves, sala1, "10/11/2026", "19:00", 7.50, 95);
        anadirSesion(3, oppenheimer, sala1, "10/11/2026", "22:00", 9.00, 50);
        anadirSesion(4, gladiator, sala2, "10/11/2026", "16:00", 7.50, 0);
        anadirSesion(5, smile, sala2, "10/11/2026", "19:00", 7.50, 70);
        anadirSesion(6, robot, sala2, "10/11/2026", "22:00", 9.00, 100);
        anadirSesion(7, dune, sala3, "10/11/2026", "16:00", 7.50, 35);
        anadirSesion(8, delReves, sala3, "10/11/2026", "19:00", 7.50, 92);
        anadirSesion(9, oppenheimer, sala3, "10/11/2026", "22:00", 9.00, 60);

        anadirSesion(10, gladiator, sala1, "11/11/2026", "16:00", 7.50, 10);
        anadirSesion(11, smile, sala1, "11/11/2026", "19:00", 7.50, 45);
        anadirSesion(12, robot, sala1, "11/11/2026", "22:00", 9.00, 80);
        anadirSesion(13, dune, sala2, "11/11/2026", "16:00", 7.50, 25);
        anadirSesion(14, delReves, sala2, "11/11/2026", "19:00", 7.50, 98);
        anadirSesion(15, oppenheimer, sala2, "11/11/2026", "22:00", 9.00, 5);
        anadirSesion(16, gladiator, sala3, "11/11/2026", "16:00", 7.50, 40);
        anadirSesion(17, smile, sala3, "11/11/2026", "19:00", 7.50, 75);
        anadirSesion(18, robot, sala3, "11/11/2026", "22:00", 9.00, 30);
    }

    private void anadirSesion(int id, Pelicula pelicula, Sala sala, String fecha, String hora,
                              double precio, int porcentajeOcupado) {
        Sesion sesion = new Sesion(id, pelicula, sala, fecha, hora, precio);
        ocuparButacas(sesion, porcentajeOcupado);
        sesiones.add(sesion);
    }

    private void ocuparButacas(Sesion sesion, int porcentaje) {
        Sala sala = sesion.getSala();
        for (int f = 0; f < sala.getFilas(); f++) {
            for (int c = 0; c < sala.getColumnas(); c++) {
                if (Math.random() * 100 < porcentaje) {
                    sesion.getButaca(f, c).setEstado(EstadoButaca.OCUPADA);
                }
            }
        }
    }
}
