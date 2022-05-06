public class Percorso {
    private Router router;
    private int peso;
    private Interfaccia interfaccia;

    public Percorso(Router router, int peso, Interfaccia interfaccia){
        this.router = router;
        this.peso = peso;
        this.interfaccia = interfaccia;
    }

    public Interfaccia getInterfaccia() {
        return interfaccia;
    }

    public void setInterfaccia(Interfaccia interfaccia) {
        this.interfaccia = interfaccia;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
}
