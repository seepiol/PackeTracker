package Routing.Model;

/**
 * Classe che descrive il collegamento fra due interfacce
 */
public class Collegamento {
    /**
     * prima interfaccia
     */
    private Interfaccia nodo1;
    /**
     * seconda interfaccia
     */
    private Interfaccia nodo2;
    /**
     * costo del collegamento
     */
    private int costo;

    public Collegamento(Interfaccia nodo1, Interfaccia nodo2, int costo) {
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.costo = costo;
    }

    public Collegamento() {
    }

    /**
     * imposta i nodi collegati
     * @param int1 primo nodo
     * @param int2 secondo nodo
     */
    public void setNodi(Interfaccia int1, Interfaccia int2){
        nodo1 = int1;
        nodo2 = int2;
    }

    public Interfaccia getNodo1() {
        return nodo1;
    }

    public void setNodo1(Interfaccia nodo1) {
        this.nodo1 = nodo1;
    }

    public Interfaccia getNodo2() {
        return nodo2;
    }

    public void setNodo2(Interfaccia nodo2) {
        this.nodo2 = nodo2;
    }

    /**
     * Recupera altro nodo del collegamento
     * @param nodo nodo conosciuto del collegamento
     * @return secondo nodo del collegamento
     */
    public Interfaccia getAltroNodo(Interfaccia nodo){
        if(nodo.equals(nodo1)){
            return nodo2;
        }
        if(nodo.equals(nodo2)){
            return nodo1;
        }
        return null;
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
        if(nodo2 != null && nodo2.getAddress().equals(a)){
            return true;
        }
        return false;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Collegamento "+ nodo1 +" - "+ nodo2;
    }
}
