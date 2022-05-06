public class GeneratoreCollegamenti {
    public static void collega(Router router1, Router router2, int costo){
        Interfaccia interfacciaRouter1 = new Interfaccia("eth"+(router1.getInterfacce().size()+1), "");
        router1.aggiungiInterfaccia(interfacciaRouter1);

        Interfaccia interfacciaRouter2 = new Interfaccia("eth"+(router2.getInterfacce().size()+1), "");
        router2.aggiungiInterfaccia(interfacciaRouter2);

        Collegamento collegamento = new Collegamento(interfacciaRouter1, interfacciaRouter2, costo);

        interfacciaRouter1.setCollegamento(collegamento);
        interfacciaRouter2.setCollegamento(collegamento);
    }
}
