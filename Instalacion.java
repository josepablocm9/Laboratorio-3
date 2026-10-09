public class Instalacion extends OrdenServicio {
    protected int puntos;

    public Instalacion(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int puntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser un entero positivo.");
        }
        this.puntos = puntos;
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = (zona == Zona.METROPOLITANA) ? 100.0 : 175.0;
        double horasPorPuntos = puntos * 0.75;
        double horasFacturablesExactas = Math.max(horasEstimadas, horasPorPuntos);
        int horasFacturables = (int) Math.ceil(horasFacturablesExactas);
        return tarifaBase + (horasFacturables * 90.0);
    }

    @Override
    public int obtenerDiasEstimados() {
        return (zona == Zona.METROPOLITANA) ? 3 : 5;
    }

    @Override
    public boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla) {
        return true;
    }

    @Override
    public int getVisitasMaximas() {
        return 3;
    }

    @Override
    public String getNombreCategoria() {
        return "Instalación";
    }
}