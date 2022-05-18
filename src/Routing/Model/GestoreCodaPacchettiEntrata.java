package Routing.Model;

import Routing.Model.Pacchetti.Pacchetto;

import java.util.Queue;

/**
 * Gestore della coda dei pacchetti in entrata del router
 */
public class GestoreCodaPacchettiEntrata implements Runnable{
    /**
     * Thread di esecuzione del gestore
     */
    private Thread thread;
    /**
     * coda dei pacchetti in entrata del router
     */
    private final Queue<Pacchetto> codaPacchettiEntrata;
    /**
     * Router
     */
    private final Router router;

    public GestoreCodaPacchettiEntrata(Router router, Queue<Pacchetto> codaPacchettiEntrata) {
        this.router = router;
        this.codaPacchettiEntrata = codaPacchettiEntrata;
        this.thread = new Thread(this);
    }

    public void start(){
        this.thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        thread.stop();
    }

    /**
     * Se la coda dei pacchetti in arrivo non è vuota, rimuove il primo pacchetto e lo passa alla funzione
     * <code>inviaPacchetto</code>
     */
    public void run(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!codaPacchettiEntrata.isEmpty()) {
                Pacchetto pacchetto = codaPacchettiEntrata.remove();
                router.inviaPacchetto(pacchetto);
            }
        }
    }
}
