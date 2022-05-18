package Routing.Model;

import java.util.ArrayList;

public class MainModel {
    private final ArrayList<Router> listaRouter;
    private final ArrayList<Collegamento> listaCollegamenti;

    public MainModel() {
        listaRouter = new ArrayList<>();
        listaCollegamenti = new ArrayList<>();
    }

    public ArrayList<Collegamento> getListaCollegamenti() {
        return listaCollegamenti;
    }

    public void addCollegamento(Collegamento collegamento){
        listaCollegamenti.add(collegamento);
    }

    public void addRouter(Router router){
        listaRouter.add(router);
    }

    public ArrayList<Router> getListaRouter(){
        return listaRouter;
    }
}
