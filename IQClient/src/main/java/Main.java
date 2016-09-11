import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<IQSetPacket> setQueue = new ArrayBlockingQueue<>(1000);
    public static MongoClient mongoClient = new MongoClient("66.175.209.202", 27017);
    public static MongoDatabase mongoDatabase = mongoClient.getDatabase("dataSets");

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH);

    private enum OperatingSystem {
        Linux, Mac, Windows
    } private static OperatingSystem currentOperatingSystem;

    public static void main(String[] args) {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            currentOperatingSystem = OperatingSystem.Windows;
        } else if (osName.contains("Mac OS X")) {
            currentOperatingSystem = OperatingSystem.Mac;
        } else {
            currentOperatingSystem = OperatingSystem.Linux;
        }

        MongoConsumer mongoConsumer = new MongoConsumer();
        Thread t = new Thread(mongoConsumer);
        t.start();

        while (true) {
            collectNewDataSet(500000, 1000000, 28000000, 2465000000L);
        }
    }

    public static void collectNewDataSet(long numSamples, long sampleRate, long bandwidth, long frequency) {
        File testScript = null;
        String filename = dateFormat.format(new Date());
        switch (currentOperatingSystem) {
            case Windows: {
                testScript = new File("testScript.bat");
            } break;case Mac: {
                testScript = new File("testScript.sh");
            } break;case Linux: {
                testScript = new File("testScript.sh");
            }
        }

        if (testScript.exists()) {
            testScript.delete();
        }

        if (Files.notExists(Paths.get("dataSets"), LinkOption.NOFOLLOW_LINKS)) {
            File dataSets = new File("dataSets");
            dataSets.mkdir();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(testScript));
            switch (currentOperatingSystem) {
                case Windows: {

                } break;case Mac: {
                    writer.write("bladeRF-cli -l hostedx40-latest.rbf\n");
                    writer.write("bladeRF-cli -e \"set samplerate " + sampleRate + "\"\n");
                    writer.write("bladeRF-cli -e \"set bandwidth " + bandwidth + "\"\n");
                    writer.write("bladeRF-cli -e \"set frequency " + frequency + "\"\n");
                    writer.write("bladeRF-cli -e \"rx config file=dataSets/" + filename + ".bin format=bin n="
                            + numSamples + "; rx start; rx wait;\"\n");
                    writer.write("osascript -e 'tell application \"Terminal\" to quit' &\n");
                    writer.write("exit\n");
                } break;case Linux: {
                    writer.write("bladeRF-cli -l hostedx40-latest.rbf\n");
                    writer.write("bladeRF-cli -e \"set samplerate " + sampleRate + "\"\n");
                    writer.write("bladeRF-cli -e \"set bandwidth " + bandwidth + "\"\n");
                    writer.write("bladeRF-cli -e \"set frequency " + frequency + "\"\n");
                    writer.write("bladeRF-cli -e \"rx config file=dataSets/" + filename + ".bin format=bin n="
                            + numSamples + "; rx start; rx wait;\"\n");
                    writer.write("exit\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {}
        }

        try {
            Runtime.getRuntime().exec("chmod u+x " + testScript.getName());
            Process p = Runtime.getRuntime().exec("./" + testScript.getName());

            // Print output from BladeRF-cli
            /*BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = bf.readLine()) != null) {
                System.out.println(line);
            }*/

            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setQueue.add(new IQSetPacket(filename, numSamples, sampleRate, bandwidth, frequency));

        testScript.delete();
    }
}
