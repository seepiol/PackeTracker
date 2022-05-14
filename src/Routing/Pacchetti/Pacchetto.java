package Routing.Pacchetti;

import Routing.Router;

import java.util.ArrayList;

/**
 * Classe che descrive un pacchetto generico
 */
public class Pacchetto {
    /**
     * Contenuto del pacchetto
     */
    private Object contenuto;
    /**
     * Tipo del pacchetto
     */
    private TipoPacchetto tipo;
    /**
     * Lista dei router passati dal pacchetto
     */
    private ArrayList<Router> rotta;
    /**
     * Router sorgente del pacchetto
     */
    private Router sorgente;
    /**
     * Router destinatario del pacchetto
     */
    private Router destinazione;
    /**
     * Numero massimo di hop
     * TODO
     */
    private int maxHop;

    /**
     * Crea un'istanza della classe pacchetto
     * @param contenuto Contenuto del pacchetto
     * @param sorgente router sorgente
     * @param destinazione router destinatario
     * @param tipo tipo pacchetto
     * @param maxHop numero massimo di hop
     */
    public Pacchetto(Object contenuto, Router sorgente, Router destinazione, TipoPacchetto tipo, int maxHop) {
        this.contenuto = contenuto;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.maxHop = maxHop;
        this.tipo = tipo;
        rotta = new ArrayList<>();
    }

    /**
     * Crea un'istanza della classe pacchetto
     * @param contenuto Contenuto del pacchetto
     * @param sorgente router sorgente
     * @param destinazione router destinatario
     * @param tipo tipo pacchetto
     */
    public Pacchetto(Object contenuto, Router sorgente, Router destinazione, TipoPacchetto tipo){
        this.contenuto = contenuto;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.maxHop = 15;
        this.tipo = tipo;
        rotta = new ArrayList<>();
    }

    public Router getSorgente() {
        return sorgente;
    }

    public Router getDestinazione() {
        return destinazione;
    }

    public void setPassaggio(Router router){
        rotta.add(router);
    }

    public TipoPacchetto getTipo() {
        return tipo;
    }

    public Object getContenuto() {
        return contenuto;
    }

    public ArrayList<Router> getRotta() {
        return rotta;
    }
}
