package SprintThree;

import java.util.Arrays;

public class SOSRecord {
    public int[] coordinateStart;
    public int[] coordinateEnd;
    public String color;
    public SOSRecord(int[] coordinateStart, int[] coordinateEnd, String color) {
        this.coordinateStart = coordinateStart;
        this.coordinateEnd = coordinateEnd;
        this.color = color;
    }

    //Source of idea: https://stackoverflow.com/questions/16069106/how-to-compare-two-java-objects
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SOSRecord)) {
            return false;
        }

        SOSRecord that = (SOSRecord) other;

        System.out.println("start: " + this.coordinateStart[0] + ", " + this.coordinateStart[1] + " ||| " + that.coordinateStart[0] + ", " + that.coordinateStart[1]);
        System.out.println("end: " + this.coordinateEnd[0] + ", " + this.coordinateEnd[1] + " ||| " + that.coordinateEnd[0] + ", " + that.coordinateEnd[1] + "\n");

        // We don't really need to check the other fields, just checking if an SOS already exists with the same coordinates.
        return (Arrays.equals(this.coordinateStart, that.coordinateStart)
                && Arrays.equals(this.coordinateEnd, that.coordinateEnd))
                || (Arrays.equals(this.coordinateStart, that.coordinateEnd)
                    && Arrays.equals(this.coordinateEnd, that.coordinateStart));
    }
}
