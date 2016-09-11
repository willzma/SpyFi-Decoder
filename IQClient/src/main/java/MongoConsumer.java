import com.mongodb.MongoClient;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.*;

public class MongoConsumer implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                IQSetPacket iqsp = Main.setQueue.take();
                File binFile = new File("dataSets/" + iqsp.filename());
                double numerator = 0;
                byte[] bytes = Files.readAllBytes(Paths.get(binFile.getPath() + ".bin"));
                //byte[] bytes = Files.readAllBytes(Paths.get("dataSets/2016-09-11-01-53-17.bin"));

                // Q, I
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                while (bb.hasRemaining()) {
                    numerator += Math.abs((((double) bb.getShort()) / 2048) / iqsp.sampleSize());
                    bb.getShort();
                }
                System.out.println(numerator);
                binFile.delete();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }

        // Good for learning about Java's low-level stream APIs
        /*try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
                final WatchKey watchKey = Paths.get("dataSets/").register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
                while (true) {
                    final WatchKey wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                            IQSetPacket iqsp = Main.setQueue.take();
                            File binFile = new File("dataSets/" + iqsp.filename());
                            long numerator = 0;
                            byte[] bytes = Files.readAllBytes(Paths.get(binFile.getPath() + ".bin"));
                            for (int i = 0; i < bytes.length; i++) {
                                System.out.println(bytes[i]);
                            }
                        }
                    }

                    boolean valid = wk.reset();
                    if (!valid) {
                        System.out.println("Key unregistered");
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }*/
    }
}
