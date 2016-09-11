import java.io.*;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<IQSetPacket> setQueue = new ArrayBlockingQueue<>(1000);

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

        while (true) {
            setQueue.add(collectNewDataSet(500000, 1000000, 28000000, 2465000000L));
        }
    }

    public static IQSetPacket collectNewDataSet(long numSamples, long sampleRate, long bandwidth, long frequency) {
        File testScript = null;
        UUID newSetID = UUID.randomUUID();
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
                    writer.write("bladeRF-cli -e \"rx config file=/dataSets/" + newSetID.toString() + ".bin format=bin n="
                            + numSamples + "; rx start; rx wait;\"\n");
                    writer.write("osascript -e 'tell application \"Terminal\" to quit' &\n");
                    writer.write("exit\n");
                } break;case Linux: {
                    writer.write("bladeRF-cli -l hostedx40-latest.rbf\n");
                    writer.write("bladeRF-cli -e \"set samplerate " + sampleRate + "\"\n");
                    writer.write("bladeRF-cli -e \"set bandwidth " + bandwidth + "\"\n");
                    writer.write("bladeRF-cli -e \"set frequency " + frequency + "\"\n");
                    writer.write("bladeRF-cli -e \"rx config file=/dataSets/" + newSetID.toString() + "bin format=bin n="
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
            runTestScript(testScript.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new IQSetPacket(newSetID.toString(), numSamples, sampleRate, bandwidth, frequency);
    }

    public static void runTestScript(String filename) throws Exception {
        Runtime.getRuntime().exec("chmod u+x " + filename);
        Process p = Runtime.getRuntime().exec("./" + filename);
        BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while ((line = bf.readLine()) != null) {
            System.out.println(line);
        }
        p.waitFor();
    }
}
