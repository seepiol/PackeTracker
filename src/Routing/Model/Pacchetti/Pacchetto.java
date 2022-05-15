package Routing.Model.Pacchetti;

import Routing.Model.Router;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Classe che descrive un pacchetto generico
 */
public class Pacchetto {
    /**
     * Contenuto del pacchetto
     */
    private String contenuto;
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
    public Pacchetto(String contenuto, Router sorgente, Router destinazione, TipoPacchetto tipo, int maxHop) {
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
    public Pacchetto(String contenuto, Router sorgente, Router destinazione, TipoPacchetto tipo){
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

    public String getContenuto() {
        return contenuto;
    }

    public int getMaxHop() {
        return maxHop;
    }


    public ObservableList<Router> getRottaObservableList(){
        return FXCollections.observableList(rotta);
    }

    public boolean inoltra(){
        if(maxHop-1 >= 0){
            maxHop--;
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Router> getRotta() {
        return rotta;
    }
}
