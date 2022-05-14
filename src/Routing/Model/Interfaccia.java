package Routing.Model;

import java.util.Objects;

/**
 * Classe che descrive un'interfaccia del router
 */
public class Interfaccia {
    /**
     * nome dell'interfaccia
     */
    private String label;
    /**
     * collegamento dell'interfaccia
     */
    private Collegamento collegamento;
    /**
     * indirizzo univoco dell'interfaccia
     */
    private String address;
    /**
     * router contenente l'interfaccia
     */
    private Router router;

    public Interfaccia(String label, Collegamento collegamento, String address) {
        this.label = label;
        this.collegamento = collegamento;
        this.address = address;
    }

    public Interfaccia(String label, String address) {
        this.label = label;
        this.address = address;
    }

    public Interfaccia(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Collegamento getCollegamento() {
        return collegamento;
    }

    public void setCollegamento(Collegamento collegamento) {
        this.collegamento = collegamento;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interfaccia that = (Interfaccia) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s @ %s", address, label, router.getLabel());
    }
}
