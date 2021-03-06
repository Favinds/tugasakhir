package br.ol.animation.bvh;

import br.ol.math.Mat4;
import br.ol.math.Vec4;
import java.util.ArrayList;
import java.util.List;

/**
 * Node class.
 * 
 *
 */
public class Node {
    
    public static enum Type { ROOT, JOINT, END }
    
    private final Type type;
    private String name;
    private final Vec4 offset = new Vec4();
    private String[] channels;
    private final Node parent;
    private final List<Node> childrens = new ArrayList<Node>();
    
    private static final Mat4 transformTmp = new Mat4();
    private final Mat4 transform = new Mat4();
    private final Vec4 position = new Vec4();
    
    public Node(ParseData parseData) {
        this(parseData, Type.ROOT, null);
    }
    
    public Node(ParseData parseData, Type type, Node parent) {
        this.type = type;
        this.parent = parent;
        switch (type) {
            case ROOT:
                parseData.expect("HIERARCHY");
                name = parseData.expect("ROOT")[1];
                break;
            case JOINT:
                name = parseData.expect("JOINT")[1];
                break;
            case END:
                name = parseData.expect("End")[1];
        }
        parseData.expect("{");
        setOffset(parseData.expect("OFFSET"));
        if (parseData.getLine().startsWith("CHANNELS")) {
            setChannels(parseData.expect("CHANNELS"));
        }
        while (parseData.getLine().startsWith("JOINT")) {
            childrens.add(new Node(parseData, Type.JOINT, this));
        }
        if (parseData.getLine().startsWith("End")) {
            childrens.add(new Node(parseData, Type.END, this));
        }
        parseData.expect("}");
    }
    
    private void setOffset(String[] offsetStr) {
        offset.setX(Double.parseDouble(offsetStr[1]));
        offset.setY(Double.parseDouble(offsetStr[2]));
        offset.setZ(Double.parseDouble(offsetStr[3]));
        offset.setW(1);
    }

    private void setChannels(String[] channelsTmp) {
        int size = Integer.parseInt(channelsTmp[1]);
        this.channels = new String[size];
        for (int i = 0; i < size; i++) {
            this.channels[i] = channelsTmp[2 + i].toLowerCase();
        }
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Vec4 getOffset() {
        return offset;
    }

    public Vec4 getPosition() {
        return position;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public String[] getChannels() {
        return channels;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildrens() {
        return childrens;
    }
    
    public void fillNodesList(List<Node> nodes) {
        if (type == Type.ROOT) {
            nodes.clear();
        }
        if (!nodes.contains(this)) {
            nodes.add(this);
        }
        for (Node children : childrens) {
            children.fillNodesList(nodes);
        }
    }
    private static final int[] DATA_INDEX = { 0 };
    
    public void setPose(double[] data) {
        transform.setIdentity();
        DATA_INDEX[0] = 0;
        setPose(data, DATA_INDEX);
    }

    private void setPose(double[] data, int[] dataIndex) {
        if (type == Type.ROOT) {
            transform.setTranslation(offset);
        }
        else {
            transform.set(parent.getTransform());
            transformTmp.setTranslation(offset);
            transform.multiply(transformTmp);
        }
        
        if (channels != null && data != null) {
            for (int c = 0; c < channels.length; c++) {
                String channel = channels[c];
                double value = data[dataIndex[0]++];
                if (channel.equals("xposition")) {
                    transformTmp.setTranslation(value, 0, 0);
                }
                else if (channel.equals("yposition")) {
                    transformTmp.setTranslation(0, value, 0);
                }
                else if (channel.equals("zposition")) {
                    transformTmp.setTranslation(0, 0, value);
                }
                else if (channel.equals("zrotation")) {
                    transformTmp.setRotationZ(Math.toRadians(value));
                }
                else if (channel.equals("yrotation")) {
                    transformTmp.setRotationY(Math.toRadians(value));
                }
                else if (channel.equals("xrotation")) {
                    transformTmp.setRotationX(Math.toRadians(value));
                }
                transform.multiply(transformTmp);
            }
        }
        
        position.set(0, 0, 0, 1);
        transform.multiply(position);
        
        for (Node children : childrens) {
            children.setPose(data, dataIndex);
        }
    }
    
}
