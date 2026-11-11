package domain;

public enum Clasificacion {
    TP("TP"), MAS_7("+7"), MAS_12("+12"), MAS_16("+16"), MAS_18("+18");

    private String texto;

    Clasificacion(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto;
    }
}