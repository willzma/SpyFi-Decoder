import java.util.ArrayList;

public class IQSetPacket {
    private ArrayList<IQPacket> packetSet;
    private String filename;
    private long numSamples;
    private long sampleRate;
    private long bandwidth;
    private long frequency;

    public IQSetPacket(String filename, long numSamples, long sampleRate, long bandwidth, long frequency) {
        packetSet = new ArrayList<>();
        this.filename = filename;
        this.numSamples = numSamples;
        this.sampleRate = sampleRate;
        this.bandwidth = bandwidth;
        this.frequency = frequency;
    }

    public ArrayList<IQPacket> samples() { return packetSet; }
    public String filename() { return filename; }
    public long sampleSize() { return numSamples; }
    public long sampleRate() { return sampleRate; }
    public long bandwidth() { return bandwidth; }
    public long frequency() { return frequency; }
}
