package Routing.Model;

import Routing.Controller.MainController;
import Routing.Model.Pacchetti.Pacchetto;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe che descrive un router
 */
public class Router implements Runnable{
    /**
     * Contatore dentificatore univoco router
     */
    private static int contatoreIdentificatore = 0;

    /**
     * Identificatore univoco router
     */
    private int id;
    /**
     * Nome identificativo router
     */
    private String label;
    /**
     * Lista delle interfacce del router
     */
    private ArrayList<Interfaccia> interfacce;
    /**
     * Tabella di routing
     */
    private TabellaRouting tabellaRouting;
    /**
     * Pacchetti in ricezione
     */
    private Queue<Pacchetto> codaPacchettiEntrata;
    /**
     * Gestore coda pacchetti in entrata
     */
    private GestoreCodaPacchettiEntrata gestoreCoda;
    /**
     * Cronologia pacchetti ricevuti
     */
    private ArrayList<Pacchetto> pacchetti;
    /**
     * Canvas per disegnare il router nella GUI
     */
    private Canvas canvas;
    /**
     * Istanza di maincontroller necessaria per inviare i log
     */
    private MainController mainController;
    /**
     * Thread router
     */
    private Thread thread;


    /**
     * Crea l'istanza di un router
     * @param label etichetta identificativa del router
     */
    public Router(String label, MainController mainController) {
        this.thread = new Thread(this);
        this.interfacce = new ArrayList<>();
        this.label = label;
        this.tabellaRouting = new TabellaRouting();
        this.id = contatoreIdentificatore++;
        this.codaPacchettiEntrata = new LinkedList<>();
        this.gestoreCoda = new GestoreCodaPacchettiEntrata(this, codaPacchettiEntrata);
        this.pacchetti = new ArrayList<>();
        this.mainController = mainController;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Aggiunge un pacchetto alla coda dei pacchetti di enrata del router
     * @param pacchetto pacchetto da aggiungere alla coda
     */
    public void riceviPacchetto(Pacchetto pacchetto){
        codaPacchettiEntrata.add(pacchetto);
    }

    /**
     * Aggiunge un'interfaccia al router
     * @param i interfaccia da aggiungere
     * @return risultato operazione
     */
    public boolean aggiungiInterfaccia(Interfaccia i){
        if(i != null){
            interfacce.add(i);
            // imposta il router come router dell'interfaccia
            i.setRouter(this);
            return true;
        }
        return false;
    }

    /**
     * Esegue il processo di convergenza secondo l'algoritmo di Bellman-Ford
     * Richiede ad ogni router presente nella propria tabella di routing (inizialmente riempita con le connessioni
     * dirette) le rotte contenute nella loro tabella di routing.
     * Ogni rotta viene salvata nella tabella di routing del router corrente
     */
    public void convergenza(){
        TabellaRouting tabellaRoutingVicino;
        Interfaccia interfacciaLocale;
        int costo;
        Router router;
        Router routerConnesso;
        mainController.log(String.format("[%s]: Convergenza\n", this));
        for(int i=0; i<tabellaRouting.getTabella().size(); i++){
            router = tabellaRouting.getTabella().get(i).getRouter();
            tabellaRoutingVicino = router.getTabellaRouting();
            for(int k=0; k<tabellaRoutingVicino.getNumeroRotte(); k++){
                routerConnesso = tabellaRoutingVicino.getTabella().get(k).getRouter();
                if(routerConnesso != this){
                    interfacciaLocale = tabellaRouting.getInterfacciaPerRouter(router);
                    costo = tabellaRoutingVicino.getCostoPerRouter(routerConnesso) + tabellaRouting.getCostoPerRouter(router);

                    if(
                            !tabellaRouting.esisteRottaPerRouter(routerConnesso) ||
                            (tabellaRouting.esisteRottaPerRouter(routerConnesso) && tabellaRouting.getCostoPerRouter(routerConnesso) > costo)
                    ) {
                        // TODO: salvare anche rotte con costo maggiore di quella già presente, e decidere in fase di indirizzamento la più economica
                        tabellaRouting.rimuoviRotta(tabellaRouting.trovaRottaDaRouter(routerConnesso));
                        mainController.log(String.format("[%s]: Scoperta rotta per %s passando da %s (interfaccia %s, costo %d)\n", this, routerConnesso, router, interfacciaLocale, costo));
                        tabellaRouting.aggiungiRotta(new Rotta(routerConnesso, interfacciaLocale, costo));
                    }
                }
            }
        }
    }

    /**
     * esegue il broadcasting di un pacchetto (invia il pacchetto a tutti i router conosciuti)
     * @param p pacchetto da inviare
     */
    public void broadcastPacchetto(Pacchetto p){
        for(Rotta rotta : tabellaRouting.getTabella()) {
            inviaPacchetto(new Pacchetto(p.getContenuto(), this, rotta.getRouter(), p.getTipo()));
        }
    }

    public TabellaRouting getTabellaRouting(){
        return tabellaRouting;
    }

    public void stampaTabellaRouting(){
        System.out.println("ROUTER "+label);
        System.out.println(tabellaRouting.toString());
    }

    /**
     * Scopre i router connessi direttamente alle interfacce del router corrente
     */
    public void scopriConnessioniDirette(){
        mainController.log(String.format("[%s]: Ricerca Connessioni Dirette\n", this));
        for(Interfaccia interfaccia : interfacce){
            if(interfaccia.getCollegamento()!=null) {
                Interfaccia interfacciaAltroRouter = interfaccia.getCollegamento().getAltroNodo(interfaccia);
                if (interfaccia.getCollegamento().getAltroNodo(interfaccia) != null && !tabellaRouting.esisteRottaPerRouter(interfaccia.getCollegamento().getAltroNodo(interfaccia).getRouter())) {
                    mainController.log(String.format("[%s]: Scoperta rotta per %s collegato direttamente sull'interfaccia %s, costo %d\n", this, interfacciaAltroRouter.getRouter(), interfaccia, interfaccia.getCollegamento().getCosto()));
                    tabellaRouting.aggiungiRotta(new Rotta(interfacciaAltroRouter.getRouter(), interfaccia, interfaccia.getCollegamento().getCosto()));
                }
            }
        }
    }

    /**
     * Gestisce un pacchetto inviato al router corrente
     * @param pacchetto pacchetto da gestire
     */
    public void gestionePacchetto(Pacchetto pacchetto){
        switch(pacchetto.getTipo()){
            case TESTO:
                mainController.log(String.format("[%s]: Pacchetto ricevuto di tipo testuale: %s\n", this, pacchetto.getContenuto()));
                break;
        }
    }

    /**
     * Esegue l'inoltro del pacchetto passando per l'interfaccia appropriata
     * Se il pacchetto ha come destinazione il router corrente, viene chiamata <code>gestionePacchetto</code>
     * @param pacchetto pacchetto da inoltrare
     */
    public void inviaPacchetto(Pacchetto pacchetto){
        Interfaccia nextHop;
        pacchetto.setPassaggio(this);

        if(pacchetto.getDestinazione() == this){
            mainController.log(String.format("[%s]: Pacchetto Arrivato a destinazione\n", this));
            System.out.println(pacchetto.getRotta().toString());
            pacchetti.add(pacchetto);
            gestionePacchetto(pacchetto);
        }else {
            nextHop = tabellaRouting.getInterfacciaPerRouter(pacchetto.getDestinazione());
            if (nextHop != null) {
                if(pacchetto.inoltra()) {
                    mainController.log(String.format("[%s]: Pacchetto Inoltrato su %s\n", this, nextHop));
                    nextHop.getCollegamento().getAltroNodo(nextHop).getRouter().riceviPacchetto(pacchetto);
                }else{
                    mainController.log(String.format("[%s]: Pacchetto Droppato: Limite hop raggiunto\n", this));
                }
            }else{
                mainController.log(String.format("[%s]: Pacchetto Droppato: Nessuna rotta per %s\n", this, pacchetto.getDestinazione()));
            }
        }
    }

    /**
     * Avvia il thread del router
     */
    public void start() {
        mainController.log(String.format("[%s]: Inizializzato\n", this));
        gestoreCoda.start();
        thread.start();
    }

    /**
     * Risolve il nome del router
     * @param routerName nome router
     * @return istanza del router corrispondente
     */
    public Router nameToRouter(String routerName){
        for(Rotta rotta : tabellaRouting.getTabella()){
            if(rotta.getRouter().getLabel().equalsIgnoreCase(routerName)){
                return rotta.getRouter();
            }
        }
        return null;
    }

    /**
     * Risoluzione inversa del nome del router
     * @param router istanza del router
     * @return nome del router
     */
    public String routerToName(Router router){
        return router.getLabel();
    }

    public ArrayList<Interfaccia> getInterfacce() {
        return interfacce;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scopriConnessioniDirette();
            convergenza();
            //mainController.selezionatoRouterPerTabella();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Router %s", label);
    }
}
