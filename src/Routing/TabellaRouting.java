package Routing;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Classe che descrive la tabella di routing di un router
 */
public class TabellaRouting {
    /**
     * Lista delle rotte
     */
    private ArrayList<Rotta> table;
    public Semaphore semaforo;

    public TabellaRouting() {
        table = new ArrayList<>();
        semaforo = new Semaphore(1);
    }

    public void aggiungiRotta(Rotta rotta){
        table.add(rotta);
    }

    public ArrayList<Rotta> getTabella(){
        ArrayList<Rotta> tabella = table;
        return tabella;
    }

    public int getNumeroRotte(){
        return table.size();
    }

    public boolean esisteRottaPerRouter(Router router){
        for(Rotta rotta : table){
            if(rotta.getRouter().getId() == router.getId()){
                return true;
            }
        }
        return false;
    }

    public Interfaccia getInterfacciaPerRouter(Router router){
        return trovaRottaDaRouter(router).getInterfaccia();
    }

    public Rotta trovaRottaDaRouter(Router router){
        for(int i=0; i<table.size(); i++){
            if(table.get(i).getRouter() == router){
                return table.get(i);
            }
        }
        return null;

    }

    public int getCostoPerRouter(Router router){
        return trovaRottaDaRouter(router).getCosto();
    }

    public ArrayList<Router> getRouterDaInterfaccia(Interfaccia interfaccia){
        ArrayList<Router> routers = new ArrayList<>();
        for(Rotta rotta : table){
            if(rotta.getInterfaccia() == interfaccia)
                routers.add(rotta.getRouter());
        }
        return routers;
    }

    @Override
    public String toString() {
        String tab = "";
        try{
            semaforo.acquire();
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        for(Rotta rotta : table){
            tab+=String.format("%s -> %s [%d] \n", rotta.getInterfaccia().getLabel(), rotta.getRouter(), rotta.getCosto());
        }
        semaforo.release();
        return tab;
    }


}
