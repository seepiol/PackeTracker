import java.util.ArrayList;

public class Pacchetto {
    private Object contenuto;
    private ArrayList<Router> rotta;
    private Router sorgente;
    private Router destinazione;
    private int maxHop;

    public Pacchetto(Object contenuto, Router sorgente, Router destinazione, int maxHop) {
        this.contenuto = contenuto;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.maxHop = maxHop;
        rotta = new ArrayList<>();
    }

    public Pacchetto(Object contenuto, Router sorgente, Router destinazione){
        this.contenuto = contenuto;
        this.sorgente = sorgente;
        this.destinazione = destinazione;
        this.maxHop = 15;
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

    public Object getContenuto() {
        return contenuto;
    }

    public ArrayList<Router> getRotta() {
        return rotta;
    }
}
