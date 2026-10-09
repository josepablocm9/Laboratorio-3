import java.util.*;

public class Main {
    private static Map<String, OrdenServicio> ordenes = new HashMap<>();
    private static Map<String, Cuadrilla> cuadrillas = new HashMap<>();
    private static List<Jornada> jornadas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorJornadas = 1;

    public static void main(String[] args) {
        cargarDatosIniciales();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== TECNIHOGAR - SISTEMA DE GESTIÓN =====");
            System.out.println("1. Cotizar servicio");
            System.out.println("2. Registrar nueva orden de servicio");
            System.out.println("3. Registrar cuadrilla");
            System.out.println("4. Gestionar Jornadas (Crear, Asignar, Retirar, Iniciar)");
            System.out.println("5. Registrar resultados de visitas");
            System.out.println("6. Consultar órdenes y cuadrillas");
            System.out.println("7. Generar reportes gerenciales");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> cotizarServicio();
                    case 2 -> registrarOrden();
                    case 3 -> registrarCuadrilla();
                    case 4 -> menuJornadas();
                    case 5 -> registrarResultadosVisita();
                    case 6 -> consultarInformacion();
                    case 7 -> generarReportes();
                    case 8 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        System.out.println("¡Gracias por utilizar el sistema!");
    }

    private static void cargarDatosIniciales() {
        try {
            Cuadrilla c1 = new Cuadrilla("CUA-01", 8.0, true);
            Cuadrilla c2 = new Cuadrilla("CUA-02", 6.0, false);
            Cuadrilla c3 = new Cuadrilla("CUA-03", 7.5, false);
            cuadrillas.put(c1.getCodigo(), c1);
            cuadrillas.put(c2.getCodigo(), c2);
            cuadrillas.put(c3.getCodigo(), c3);

            agregarOrdenInic(new Diagnostico("ORD-101", "Ana Gómez", "Zona 10", 1.0, Zona.METROPOLITANA));
            agregarOrdenInic(new Diagnostico("ORD-102", "Carlos Ruiz", "Mixco", 1.5, Zona.PERIFERICA));

            agregarOrdenInic(new Instalacion("ORD-103", "Empresa X", "Zona 4", 3.0, Zona.METROPOLITANA, 2));
            agregarOrdenInic(new Instalacion("ORD-104", "Mario López", "Zona 14", 2.5, Zona.METROPOLITANA, 5));

            agregarOrdenInic(new InstalacionCertificada("ORD-105", "Lucía Méndez", "Carretera a El Salvador", 2.0, Zona.PERIFERICA, 4, 1500.0));
            agregarOrdenInic(new InstalacionCertificada("ORD-106", "Edificio Plaza", "Zona 9", 4.0, Zona.METROPOLITANA, 6, 3000.0));

            agregarOrdenInic(new MantenimientoAire("ORD-107", "Hospital Norte", "Zona 1", 2.0, Zona.METROPOLITANA, 12000));
            agregarOrdenInic(new MantenimientoAire("ORD-108", "Oficinas Centrales", "Zona 10", 3.5, Zona.METROPOLITANA, 24000));

            System.out.println(">> Carga inicial realizada exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar datos iniciales: " + e.getMessage());
        }
    }

    private static void agregarOrdenInic(OrdenServicio o) {
        o.cobrarRegistro();
        ordenes.put(o.getCodigo(), o);
    }

    private static void cotizarServicio() {
        System.out.println("\n--- COTIZAR SERVICIO ---");
        OrdenServicio temp = construirOrdenFormulario("TEMP");
        if (temp != null) {
            System.out.printf("COTIZACIÓN RESULTANTE:\nCosto estimado: Q%.2f\nTiempo estimado de atención: %d días hábiles\n",
                    temp.calcularCosto(), temp.obtenerDiasEstimados());
        }
    }

    private static void registrarOrden() {
        System.out.println("\n--- REGISTRAR ORDEN DE SERVICIO ---");
        System.out.print("Ingrese código único para la orden: ");
        String codigo = scanner.nextLine().trim();
        if (ordenes.containsKey(codigo)) {
            throw new IllegalArgumentException("Ya existe una orden con el código: " + codigo);
        }

        OrdenServicio nuevaOrden = construirOrdenFormulario(codigo);
        if (nuevaOrden != null) {
            nuevaOrden.cobrarRegistro();
            ordenes.put(codigo, nuevaOrden);
            System.out.printf("¡Orden %s registrada con éxito! Monto cobrado: Q%.2f\n", codigo, nuevaOrden.getIngresosRegistro());
        }
    }

    private static OrdenServicio construirOrdenFormulario(String codigo) {
        System.out.println("Seleccione Categoría: 1. Diagnóstico | 2. Instalación | 3. Instalación Certificada | 4. Mantenimiento Aire");
        int op = Integer.parseInt(scanner.nextLine());

        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();
        System.out.print("Dirección: ");
        String dir = scanner.nextLine();
        System.out.print("Horas estimadas: ");
        double horas = Double.parseDouble(scanner.nextLine());
        System.out.print("Zona (1. Metropolitana | 2. Periférica): ");
        Zona zona = (Integer.parseInt(scanner.nextLine()) == 1) ? Zona.METROPOLITANA : Zona.PERIFERICA;

        return switch (op) {
            case 1 -> new Diagnostico(codigo, cliente, dir, horas, zona);
            case 2 -> {
                System.out.print("Cantidad de puntos a instalar: ");
                int pts = Integer.parseInt(scanner.nextLine());
                yield new Instalacion(codigo, cliente, dir, horas, zona, pts);
            }
            case 3 -> {
                System.out.print("Cantidad de puntos a instalar: ");
                int pts = Integer.parseInt(scanner.nextLine());
                System.out.print("Valor declarado de materiales (Q): ");
                double mat = Double.parseDouble(scanner.nextLine());
                yield new InstalacionCertificada(codigo, cliente, dir, horas, zona, pts, mat);
            }
            case 4 -> {
                System.out.print("Capacidad en BTU (9000 - 60000): ");
                int btu = Integer.parseInt(scanner.nextLine());
                yield new MantenimientoAire(codigo, cliente, dir, horas, zona, btu);
            }
            default -> throw new IllegalArgumentException("Categoría no válida.");
        };
    }

    private static void registrarCuadrilla() {
        System.out.println("\n--- REGISTRAR CUADRILLA ---");
        System.out.print("Código de cuadrilla: ");
        String cod = scanner.nextLine().trim();
        if (cuadrillas.containsKey(cod)) {
            throw new IllegalArgumentException("Ya existe una cuadrilla con este código.");
        }
        System.out.print("Capacidad máxima de horas por jornada: ");
        double cap = Double.parseDouble(scanner.nextLine());
        System.out.print("¿Está certificada en refrigeración? (1. Sí | 2. No): ");
        boolean cert = Integer.parseInt(scanner.nextLine()) == 1;

        Cuadrilla c = new Cuadrilla(cod, cap, cert);
        cuadrillas.put(cod, c);
        System.out.println("Cuadrilla registrada exitosamente.");
    }

    private static void menuJornadas() {
        System.out.println("\n--- GESTIÓN DE JORNADAS ---");
        System.out.println("1. Crear Jornada");
        System.out.println("2. Asignar Orden a Jornada");
        System.out.println("3. Retirar Orden de Jornada");
        System.out.println("4. Iniciar Jornada");
        System.out.print("Seleccione: ");
        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("Ingrese código de la cuadrilla: ");
                String cCod = scanner.nextLine();
                Cuadrilla c = cuadrillas.get(cCod);
                if (c == null) throw new IllegalArgumentException("Cuadrilla no encontrada.");
                
                String idJornada = "JOR-" + (contadorJornadas++);
                Jornada j = new Jornada(idJornada, c);
                jornadas.add(j);
                System.out.println("Jornada creada con éxito: " + idJornada);
            }
            case 2 -> {
                Jornada j = buscarJornadaEnPreparacion();
                System.out.print("Ingrese código de la orden PENDIENTE: ");
                String oCod = scanner.nextLine();
                OrdenServicio o = ordenes.get(oCod);
                if (o == null) throw new IllegalArgumentException("Orden no encontrada.");
                
                for (Jornada jx : jornadas) {
                    if (jx.getEstado() != EstadoJornada.CERRADA && jx.getOrdenesAsignadas().contains(o)) {
                        throw new IllegalStateException("La orden ya se encuentra asignada a la jornada " + jx.getId());
                    }
                }
                
                j.agregarOrden(o);
                System.out.println("Orden asignada correctamente a la jornada " + j.getId());
            }
            case 3 -> {
                Jornada j = buscarJornadaEnPreparacion();
                System.out.print("Ingrese código de la orden a retirar: ");
                String oCod = scanner.nextLine();
                OrdenServicio o = ordenes.get(oCod);
                j.removerOrden(o);
                System.out.println("Orden retirada de la jornada.");
            }
            case 4 -> {
                Jornada j = buscarJornadaEnPreparacion();
                j.iniciarJornada();
                System.out.println("Jornada " + j.getId() + " iniciada. Las órdenes están en atención y los cargos adicionales aplicados.");
            }
        }
    }

    private static Jornada buscarJornadaEnPreparacion() {
        System.out.print("Ingrese ID de la jornada en preparación: ");
        String id = scanner.nextLine();
        for (Jornada j : jornadas) {
            if (j.getId().equalsIgnoreCase(id) && j.getEstado() == EstadoJornada.EN_PREPARACION) {
                return j;
            }
        }
        throw new IllegalArgumentException("Jornada no encontrada o no está en preparación.");
    }

    private static void registrarResultadosVisita() {
        System.out.println("\n--- REGISTRAR RESULTADO DE VISITAS ---");
        System.out.print("Ingrese ID de la Jornada INICIADA: ");
        String id = scanner.nextLine();
        Jornada j = null;
        for (Jornada item : jornadas) {
            if (item.getId().equalsIgnoreCase(id) && item.getEstado() == EstadoJornada.INICIADA) {
                j = item;
                break;
            }
        }
        if (j == null) throw new IllegalArgumentException("Jornada no encontrada o no está iniciada.");

        for (OrdenServicio orden : new ArrayList<>(j.getOrdenesAsignadas())) {
            if (orden.getEstado() == EstadoOrden.EN_ATENCION) {
                System.out.printf("Procesando Orden %s (Cliente: %s):\n", orden.getCodigo(), orden.getCliente());
                System.out.print("¿Resultado de la visita? (1. Exitosa/Completada | 2. Fallida): ");
                int res = Integer.parseInt(scanner.nextLine());
                j.registrarResultadoVisita(orden, res == 1);
            }
        }
        System.out.println("Resultados procesados. Estado actual de la jornada: " + j.getEstado());
    }

    private static void consultarInformacion() {
        System.out.println("\n=== CONSULTA GENERAL ===");
        System.out.println("\n--- CUADRILLAS ---");
        cuadrillas.values().forEach(System::println);

        System.out.println("\n--- ÓRDENES DE SERVICIO ---");
        ordenes.values().forEach(System::println);
    }

    private static void generarReportes() {
        System.out.println("\n=============================================");
        System.out.println("             REPORTES GERENCIALES            ");
        System.out.println("=============================================");

        System.out.println("\n1. ÓRDENES POR CATEGORÍA Y ESTADO:");
        Map<String, Map<EstadoOrden, Integer>> resumen = new HashMap<>();

        for (OrdenServicio o : ordenes.values()) {
            resumen.putIfAbsent(o.getNombreCategoria(), new HashMap<>());
            Map<EstadoOrden, Integer> con = resumen.get(o.getNombreCategoria());
            con.put(o.getEstado(), con.getOrDefault(o.getEstado(), 0) + 1);
        }

        for (var catEntry : resumen.entrySet()) {
            System.out.println("Categoría: " + catEntry.getKey());
            for (EstadoOrden eo : EstadoOrden.values()) {
                int cant = catEntry.getValue().getOrDefault(eo, 0);
                System.out.printf("   - %-12s: %d\n", eo, cant);
            }
        }

        double totalRegistro = 0;
        double totalVisitasAdic = 0;
        Map<String, Double> ingresosPorCat = new HashMap<>();

        for (OrdenServicio o : ordenes.values()) {
            totalRegistro += o.getIngresosRegistro();
            totalVisitasAdic += o.getIngresosVisitasAdicionales();
            ingresosPorCat.put(o.getNombreCategoria(),
                    ingresosPorCat.getOrDefault(o.getNombreCategoria(), 0.0) + o.getIngresoTotal());
        }

        System.out.println("\n2. REPORTES FINANCIEROS:");
        System.out.printf("   - Total Ingresos por Registro:          Q%.2f\n", totalRegistro);
        System.out.printf("   - Total Ingresos Visitas Adicionales:  Q%.2f\n", totalVisitasAdic);
        System.out.printf("   - INGRESO TOTAL GENERAL:               Q%.2f\n", (totalRegistro + totalVisitasAdic));

        System.out.println("\n   Ingresos por Categoría:");
        ingresosPorCat.forEach((cat, ing) -> System.out.printf("   - %-35s: Q%.2f\n", cat, ing));

        System.out.println("\n3. JORNADAS NO CERRADAS (EN PREPARACIÓN O INICIADAS):");
        boolean hayAbiertas = false;
        for (Jornada j : jornadas) {
            if (j.getEstado() != EstadoJornada.CERRADA) {
                hayAbiertas = true;
                System.out.printf("   Jornada ID: %s | Estado: %s | Cuadrilla: %s | Horas Asignadas: %.2f | Capacidad Disponible: %.2f hrs\n",
                        j.getId(), j.getEstado(), j.getCuadrilla().getCodigo(), j.getHorasAsignadas(), j.getCapacidadDisponible());
            }
        }
        if (!hayAbiertas) {
            System.out.println("   No hay jornadas abiertas en este momento.");
        }
        System.out.println("=============================================\n");
    }
}

