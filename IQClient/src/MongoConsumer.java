public class MongoConsumer implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Main.setQueue.take();
            } catch (InterruptedException e) {

            }
        }
    }
}
