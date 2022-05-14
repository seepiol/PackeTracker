import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Classe che descrive un router
 */
public class Router implements Runnable{
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
    private Queue<Pacchetto> codaPacchettiUscita;
    private Thread thread;


    public Router(String label) {
        this.thread = new Thread(this);
        this.interfacce = new ArrayList<>();
        this.label = label;
        this.tabellaRouting = new TabellaRouting();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean aggiungiInterfaccia(Interfaccia i){
        if(i != null){
            interfacce.add(i);
            // imposta il router come router dell'interfaccia
            i.setRouter(this);
            return true;
        }
        return false;
    }

    public void convergenza(){
        TabellaRouting tabellaRoutingVicino;
        Interfaccia interfacciaLocale;
        int costo;
        Router router;
        Router routerConnesso;
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
                        System.out.printf("%s conosce rotta per %s passando da %s (interfaccia %s, costo %d)\n", this, routerConnesso, router, interfacciaLocale, costo);
                        tabellaRouting.aggiungiRotta(new Rotta(routerConnesso, interfacciaLocale, costo));
                    }
                }
            }
        }
    }


    public void broadcastPacchetto(Pacchetto p){
        for(Rotta rotta : tabellaRouting.getTabella()) {
            inviaPacchetto(new Pacchetto(getTabellaRouting(), this, rotta.getRouter()));
        }
    }

    public TabellaRouting getTabellaRouting(){
        return tabellaRouting;
    }

    public void stampaTabellaRouting(){
        System.out.println("ROUTER "+label);
        System.out.println(tabellaRouting.toString());
    }

    public void scopriConnessioniDirette(){
        for(Interfaccia interfaccia : interfacce){
            Interfaccia interfacciaAltroRouter = interfaccia.getCollegamento().getAltroNodo(interfaccia);
            if(interfaccia.getCollegamento().getAltroNodo(interfaccia)!=null){
                System.out.printf("%s conosce rotta per %s collegato direttamente sull'interfaccia %s, costo %d\n", this, interfacciaAltroRouter.getRouter(), interfaccia, interfaccia.getCollegamento().getCosto());
                tabellaRouting.aggiungiRotta(new Rotta(interfacciaAltroRouter.getRouter(), interfaccia, interfaccia.getCollegamento().getCosto()));
            }
        }
    }

    public void inviaPacchetto(Pacchetto pacchetto){
        Interfaccia nextHop;
        pacchetto.setPassaggio(this);

        if(pacchetto.getDestinazione() == this){
            System.out.println(this+" Pacchetto ARRIVATO!");
            System.out.println(pacchetto.getRotta().toString());
            codaPacchettiEntrata.add(pacchetto);
        }else {
            nextHop = tabellaRouting.getInterfacciaPerRouter(pacchetto.getDestinazione());

            if (nextHop != null) {
                System.out.println(this+" Pacchetto INOLTRATO!");
                nextHop.getCollegamento().getAltroNodo(nextHop).getRouter().inviaPacchetto(pacchetto);
            }else{
                System.out.println(this+" Pacchetto DROPPATO! (nessuna rotta per il router di destinazione "+pacchetto.getDestinazione()+")");
            }
        }
    }

    public void start() {
        thread.start();
    }

    public Router nameToRouter(String routerName){
        for(Rotta rotta : tabellaRouting.getTabella()){
            if(rotta.getRouter().getLabel().equalsIgnoreCase(routerName)){
                return rotta.getRouter();
            }
        }
        return null;
    }

    public String routerToName(Router router){
        return router.getLabel();
    }

    public ArrayList<Interfaccia> getInterfacce() {
        return interfacce;
    }

    public void run(){
        scopriConnessioniDirette();
        while (true){
            convergenza();
            if(label.equals("r1")){
                System.out.println(tabellaRouting);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Router %s", label);
    }
}
