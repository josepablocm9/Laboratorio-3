public class InstalacionCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionCertificada(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int puntos, double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);
        if (valorMateriales < 0) {
            throw new IllegalArgumentException("El valor de los materiales no puede ser negativo.");
        }
        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularCosto() {
        double costoBaseInstalacion = super.calcularCosto();
        double garantia = valorMateriales * 0.05;
        return costoBaseInstalacion + 120.0 + garantia;
    }

    @Override
    public int obtenerDiasEstimados() {
        return super.obtenerDiasEstimados() + 2;
    }

    @Override
    public int getVisitasMaximas() {
        return 2;
    }

    @Override
    public String getNombreCategoria() {
        return "Instalación Certificada";
    }
}