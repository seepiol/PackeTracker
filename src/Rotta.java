public class Rotta {
    private Router router;
    private Interfaccia interfaccia;
    private int costo;

    public Rotta(Router router, Interfaccia interfaccia, int costo) {
        this.router = router;
        this.interfaccia = interfaccia;
        this.costo = costo;
    }

    public Router getRouter() {
        return router;
    }

    public Interfaccia getInterfaccia() {
        return interfaccia;
    }

    public int getCosto() {
        return costo;
    }
}
