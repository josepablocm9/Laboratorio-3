public class Cuadrilla {
    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;
    private Jornada jornadaActiva;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de cuadrilla no puede estar vacío.");
        }
        if (capacidadHoras <= 0) {
            throw new IllegalArgumentException("La capacidad de horas debe ser mayor a 0.");
        }
        this.codigo = codigo;
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
        this.jornadaActiva = null;
    }

    public boolean estaDisponible() {
        return jornadaActiva == null;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
    public Jornada getJornadaActiva() { return jornadaActiva; }
    public void setJornadaActiva(Jornada jornada) { this.jornadaActiva = jornada; }

    @Override
    public String toString() {
        return String.format("Cuadrilla: %s | Capacidad: %.2f hrs | Cert. Ref: %s | Estado: %s",
                codigo, capacidadHoras, (certificadaRefrigeracion ? "SI" : "NO"),
                (estaDisponible() ? "Disponible" : "Con Jornada Activa"));
    }
}