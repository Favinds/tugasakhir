package br.ol.animation.bvh;

/**
 * Motion class.
 * 
 *
 */
public class Motion {

    private int frameSize;
    private double frameTime;
    private double[][] data;
    
    public Motion(ParseData parseData) {
        parseData.expect("MOTION");
        frameSize = Integer.parseInt(parseData.expect("Frames:")[1]);
        frameTime = Double.parseDouble(parseData.expect("Frame Time:")[2]);
        data = new double[frameSize][];
        for (int f = 0; f < frameSize; f++) {
            String[] values = parseData.getLine().split("\\ ");
            data[f] = new double[values.length];
            for (int d = 0; d < values.length; d++) {
                data[f][d] = Double.parseDouble(values[d]);
            }
            parseData.nextLine();
        }        
    }

    public int getFrameSize() {
        return frameSize;
    }

    public double getFrameTime() {
        return frameTime;
    }

    public double[] getData(int frame) {
        return data[frame];
    }
    
}
