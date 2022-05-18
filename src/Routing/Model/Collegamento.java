package Routing.Model;

/**
 * Classe che descrive il collegamento fra due interfacce
 */
public class Collegamento {
    /**
     * prima interfaccia
     */
    private final Interfaccia nodo1;
    /**
     * seconda interfaccia
     */
    private final Interfaccia nodo2;
    /**
     * costo del collegamento
     */
    private final int costo;

    public Collegamento(Interfaccia nodo1, Interfaccia nodo2, int costo) {
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.costo = costo;
    }

    public boolean collegamentoValido(){
        return nodo1!=null && nodo2!=null;
    }

    public Interfaccia getNodo1() {
        return nodo1;
    }

    public Interfaccia getNodo2() {
        return nodo2;
    }

    /**
     * Recupera altro nodo del collegamento
     * @param nodo nodo conosciuto del collegamento
     * @return secondo nodo del collegamento
     */
    public Interfaccia getAltroNodo(Interfaccia nodo){
        if(nodo.equals(nodo1)){
            return nodo2;
        }else if(nodo.equals(nodo2)){
            return nodo1;
        }else {
            return null;
        }
    }

    /**
     * Verifica se un collegamento contiene un determinato nodo
     * @param a nodo da verificare
     * @return <b>true</b> se contenuto, <b>false</b> se non contenuto
     */
    public boolean contieneNodo(String a){
        if(nodo1 != null && nodo1.getAddress().equals(a)){
            return true;
        }
        return nodo2 != null && nodo2.getAddress().equals(a);
    }

    public int getCosto() {
        return costo;
    }

    @Override
    public String toString() {
        return "Collegamento "+ nodo1 +" - "+ nodo2;
    }
}
