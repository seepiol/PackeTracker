package Routing.Model;

/**
 * Classe che descrive la rotta per un router
 */
public class Rotta {
    /**
     * Router di destinazione della rotta
     */
    private final Router router;
    /**
     * Interfaccia sulla quale inviare il pacchetto
     */
    private final Interfaccia interfaccia;
    /**
     * Costo totale del tragitto router corrente - router destinazione
     */
    private final int costo;

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
