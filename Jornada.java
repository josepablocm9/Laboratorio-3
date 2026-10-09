import java.util.ArrayList;
import java.util.List;

public class Jornada {
    private String id;
    private Cuadrilla cuadrilla;
    private List<OrdenServicio> ordenesAsignadas;
    private EstadoJornada estado;
    private double horasAsignadas;

    public Jornada(String id, Cuadrilla cuadrilla) {
        if (!cuadrilla.estaDisponible()) {
            throw new IllegalStateException("La cuadrilla " + cuadrilla.getCodigo() + " ya tiene una jornada sin cerrar.");
        }
        this.id = id;
        this.cuadrilla = cuadrilla;
        this.ordenesAsignadas = new ArrayList<>();
        this.estado = EstadoJornada.EN_PREPARACION;
        this.horasAsignadas = 0.0;
        cuadrilla.setJornadaActiva(this);
    }

    public void agregarOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("Solo se pueden agregar órdenes a jornadas en preparación.");
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("La orden debe estar en estado PENDIENTE.");
        }
        if (!orden.esCompatibleConCuadrilla(cuadrilla)) {
            throw new IllegalArgumentException("La orden no es compatible con la cuadrilla (requiere certificación en refrigeración).");
        }
        if (horasAsignadas + orden.getHorasEstimadas() > cuadrilla.getCapacidadHoras()) {
            throw new IllegalArgumentException("La orden excede la capacidad disponible de la cuadrilla.");
        }
        
        ordenesAsignadas.add(orden);
        horasAsignadas += orden.getHorasEstimadas();
    }

    public void removerOrden(OrdenServicio orden) {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("Solo se pueden retirar órdenes de jornadas en preparación.");
        }
        if (ordenesAsignadas.remove(orden)) {
            horasAsignadas -= orden.getHorasEstimadas();
        } else {
            throw new IllegalArgumentException("La orden no pertenece a esta jornada.");
        }
    }

    public void iniciarJornada() {
        if (estado != EstadoJornada.EN_PREPARACION) {
            throw new IllegalStateException("La jornada ya fue iniciada o cerrada.");
        }
        if (ordenesAsignadas.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar una jornada sin órdenes asignadas.");
        }

        this.estado = EstadoJornada.INICIADA;
        for (OrdenServicio orden : ordenesAsignadas) {
            orden.registrarInicioVisita();
        }
    }

    public void registrarResultadoVisita(OrdenServicio orden, boolean exitosa) {
        if (estado != EstadoJornada.INICIADA) {
            throw new IllegalStateException("Solo se pueden registrar resultados en una jornada INICIADA.");
        }
        if (!ordenesAsignadas.contains(orden)) {
            throw new IllegalArgumentException("La orden no forma parte de esta jornada.");
        }
        if (orden.getEstado() != EstadoOrden.EN_ATENCION) {
            throw new IllegalStateException("La orden ya tiene un resultado registrado en esta jornada.");
        }

        if (exitosa) {
            orden.registrarVisitaExitosa();
        } else {
            orden.registrarVisitaFallida();
        }

        verificarCierre();
    }

    private void verificarCierre() {
        boolean todasProcesadas = true;
        for (OrdenServicio o : ordenesAsignadas) {
            if (o.getEstado() == EstadoOrden.EN_ATENCION) {
                todasProcesadas = false;
                break;
            }
        }
        if (todasProcesadas) {
            this.estado = EstadoJornada.CERRADA;
            cuadrilla.setJornadaActiva(null);
        }
    }

    public String getId() { return id; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public List<OrdenServicio> getOrdenesAsignadas() { return ordenesAsignadas; }
    public EstadoJornada getEstado() { return estado; }
    public double getHorasAsignadas() { return horasAsignadas; }
    public double getCapacidadDisponible() { return cuadrilla.getCapacidadHoras() - horasAsignadas; }

    @Override
    public String toString() {
        return String.format("Jornada ID: %s | Cuadrilla: %s | Horas: %.2f/%.2f | Estado: %s",
                id, cuadrilla.getCodigo(), horasAsignadas, cuadrilla.getCapacidadHoras(), estado);
    }
}