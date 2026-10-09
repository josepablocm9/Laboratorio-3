public class Diagnostico extends OrdenServicio {

    public Diagnostico(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException("Un diagnóstico no puede superar las 1.5 horas estimadas.");
        }
    }

    @Override
    public double calcularCosto() {
        return (zona == Zona.METROPOLITANA) ? 150.0 : 225.0;
    }

    @Override
    public int obtenerDiasEstimados() {
        return (zona == Zona.METROPOLITANA) ? 1 : 2;
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
        return "Diagnóstico";
    }
}