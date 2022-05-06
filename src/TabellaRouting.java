import java.util.ArrayList;
import java.util.HashMap;

public class TabellaRouting {
    private ArrayList<Rotta> table;

    public TabellaRouting() {
        table = new ArrayList<>();
    }

    public void aggiungiRotta(Rotta rotta){
        table.add(rotta);
    }

    public ArrayList<Rotta> getTabella(){
        return table;
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
        for(Rotta rotta : table){
            tab+=String.format("%s -> %s [%d] \n", rotta.getInterfaccia().getLabel(), rotta.getRouter(), rotta.getCosto());
        }
        return tab;
    }


}
