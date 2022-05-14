import java.util.Queue;

public class GestoreCodaPacchettiEntrata implements Runnable{
    private Thread thread;
    private Queue<Pacchetto> codaPacchettiEntrata;
    private Router router;

    public GestoreCodaPacchettiEntrata(Router router, Queue<Pacchetto> codaPacchettiEntrata) {
        this.router = router;
        this.codaPacchettiEntrata = codaPacchettiEntrata;
        this.thread = new Thread(this);
    }

    public void start(){
        thread.start();
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!codaPacchettiEntrata.isEmpty()) {
                Pacchetto pacchetto = codaPacchettiEntrata.remove();
                router.inviaPacchetto(pacchetto);
            }
        }
    }
}
