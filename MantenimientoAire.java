public class MantenimientoAire extends OrdenServicio {
    private int btu;

    public MantenimientoAire(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona, int btu) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("Los mantenimientos de aire acondicionado solo se aceptan en la zona metropolitana.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("La capacidad en BTU debe estar entre 9,000 y 60,000.");
        }
        this.btu = btu;
    }

    @Override
    public double calcularCosto() {
        double tarifaBase = 100.0;
        return tarifaBase + (horasEstimadas * 110.0) + 80.0;
    }

    @Override
    public int obtenerDiasEstimados() {
        return 2;
    }

    @Override
    public boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla) {
        return cuadrilla.isCertificadaRefrigeracion();
    }

    @Override
    public int getVisitasMaximas() {
        return 1;
    }

    @Override
    public String getNombreCategoria() {
        return "Mantenimiento Aire Acondicionado";
    }
}