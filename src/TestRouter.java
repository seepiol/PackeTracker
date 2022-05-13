

public class TestRouter {
    public static void main(String[] args) {
    	
    	System.out.println("prova");
        // creazione router
        Router r1 = new Router("r1");
        Router r2 = new Router("r2");
        Router r3 = new Router("r3");
        Router r4 = new Router("r4");

        // creazione interfacce
        Interfaccia eth1r1 = new Interfaccia("eth1", "1");
        Interfaccia eth2r1 = new Interfaccia("eth2", "6");
        Interfaccia eth1r2 = new Interfaccia("eth1", "2");
        Interfaccia eth2r2 = new Interfaccia("eth2", "3");
        Interfaccia eth1r3 = new Interfaccia("eth1", "4");
        Interfaccia eth1r4 = new Interfaccia("eth1", "5");

        // aggiunta interfacce a router
        r1.aggiungiInterfaccia(eth1r1);
        r1.aggiungiInterfaccia(eth2r1);
        r2.aggiungiInterfaccia(eth1r2);
        r2.aggiungiInterfaccia(eth2r2);
        r3.aggiungiInterfaccia(eth1r3);
        r4.aggiungiInterfaccia(eth1r4);

        // creazione link
        Collegamento link1 = new Collegamento(eth1r1, eth1r2, 2);
        Collegamento link2 = new Collegamento(eth1r3, eth2r2, 1);
        Collegamento link3 = new Collegamento(eth1r4, eth2r1, 1);

        // impostazione link alle interfacce
        eth1r1.setCollegamento(link1);
        eth2r1.setCollegamento(link3);
        eth1r2.setCollegamento(link1);
        eth1r3.setCollegamento(link2);
        eth2r2.setCollegamento(link2);
        eth1r4.setCollegamento(link3);

        Runnable tr1 = new ThreadRouter(r1);
        Thread threadR1 = new Thread(tr1, "Router 1");
        threadR1.start();

        Runnable tr2 = new ThreadRouter(r2);
        Thread threadR2 = new Thread(tr2, "Router 2");
        threadR2.start();

    }
}
