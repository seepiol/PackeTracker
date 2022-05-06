import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Classe che descrive un router
 */
public class Router {
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
    private Queue<Pacchetto> codaPacchettiRicezione;


    public Router(String label) {
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
            for(int k=0; k<tabellaRoutingVicino.getTabella().size(); k++){
                routerConnesso = tabellaRoutingVicino.getTabella().get(k).getRouter();
                if(routerConnesso != this){
                    interfacciaLocale = tabellaRouting.getInterfacciaPerRouter(router);
                    costo = tabellaRoutingVicino.getCostoPerRouter(routerConnesso) + tabellaRouting.getCostoPerRouter(router);

                    if(
                            !tabellaRouting.getTabella().contains(routerConnesso) ||
                            (tabellaRouting.getTabella().contains(routerConnesso) && tabellaRouting.getCostoPerRouter(routerConnesso) > costo)
                    ) {
                        tabellaRouting.aggiungiRotta(new Rotta(router, interfacciaLocale, costo));
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
            if(interfaccia.getCollegamento().getAltroNodo(interfaccia)!=null){
                tabellaRouting.aggiungiRotta(new Rotta(interfaccia.getCollegamento().getAltroNodo(interfaccia).getRouter(), interfaccia, interfaccia.getCollegamento().getCosto()));
            }
        }
    }

    public void inviaPacchetto(Pacchetto pacchetto){
        Interfaccia nextHop;
        pacchetto.setPassaggio(this);

        if(pacchetto.getDestinazione() == this){
            System.out.println(this+" Pacchetto ARRIVATO!");
            System.out.println(pacchetto.getRotta().toString());
            codaPacchettiRicezione.add(pacchetto);
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

    @Override
    public String toString() {
        return String.format("Router %s", label);
    }
}
