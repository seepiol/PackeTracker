package Routing.Model;

import java.util.ArrayList;

public class MainModel {
    private ArrayList<Router> listaRouter;

    public MainModel() {
        listaRouter = new ArrayList<>();
    }

    public void addRouter(Router router){
        listaRouter.add(router);
    }

    public ArrayList<Router> getListaRouter(){
        return listaRouter;
    }
}
