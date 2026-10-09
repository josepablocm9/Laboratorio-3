public abstract class OrdenServicio {
    protected String codigo;
    protected String cliente;
    protected String direccion;
    protected double horasEstimadas;
    protected Zona zona;
    protected EstadoOrden estado;
    protected int visitasRealizadas;
    
    protected double ingresosRegistro;
    protected double ingresosVisitasAdicionales;

    public OrdenServicio(String codigo, String cliente, String direccion, double horasEstimadas, Zona zona) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas estimadas deben ser un valor positivo.");
        }
        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.horasEstimadas = horasEstimadas;
        this.zona = zona;
        this.estado = EstadoOrden.PENDIENTE;
        this.visitasRealizadas = 0;
        this.ingresosRegistro = 0.0;
        this.ingresosVisitasAdicionales = 0.0;
    }


    public abstract double calcularCosto();
    public abstract int obtenerDiasEstimados();
    public abstract boolean esCompatibleConCuadrilla(Cuadrilla cuadrilla);
    public abstract int getVisitasMaximas();
    public abstract String getNombreCategoria();

  
    public void cobrarRegistro() {
        this.ingresosRegistro = calcularCosto();
    }

    public void registrarInicioVisita() {
        this.visitasRealizadas++;
        if (this.visitasRealizadas > 1) {
            this.ingresosVisitasAdicionales += 40.0;
        }
        this.estado = EstadoOrden.EN_ATENCION;
    }

    public void registrarVisitaExitosa() {
        this.estado = EstadoOrden.COMPLETADA;
    }

    public void registrarVisitaFallida() {
        if (this.visitasRealizadas >= getVisitasMaximas()) {
            this.estado = EstadoOrden.CANCELADA;
        } else {
            this.estado = EstadoOrden.PENDIENTE;
        }
    }


    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public Zona getZona() { return zona; }
    public EstadoOrden getEstado() { return estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }
    public double getIngresosRegistro() { return ingresosRegistro; }
    public double getIngresosVisitasAdicionales() { return ingresosVisitasAdicionales; }
    public double getIngresoTotal() { return ingresosRegistro + ingresosVisitasAdicionales; }

    @Override
    public String toString() {
        return String.format("[%s] Cod: %s | Cliente: %s | Estado: %s | Horas Est: %.2f | Visitas: %d/%d | Costo Reg: Q%.2f",
                getNombreCategoria(), codigo, cliente, estado, horasEstimadas, visitasRealizadas, getVisitasMaximas(), calcularCosto());
    }
}