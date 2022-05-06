public class ThreadRouter implements Runnable {
    private Router router;

    public ThreadRouter(Router router) {
        this.router = router;
        router.scopriConnessioniDirette();
    }

    public void start(){
        this.start();
    }

    public void run(){
        while (true){
            router.convergenza();
            router.stampaTabellaRouting();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
