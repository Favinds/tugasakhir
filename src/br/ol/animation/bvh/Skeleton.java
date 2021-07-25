package br.ol.animation.bvh;

import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton class.
 * 
 *
 */
public class Skeleton {
    
    private Node rootNode;
    private Motion motion;
    private final List<Node> nodes = new ArrayList<Node>();

    public Skeleton(String resource) {
        ParseData parseData = new ParseData();
        parseData.load(resource);
        rootNode = new Node(parseData);
        motion = new Motion(parseData);
        rootNode.fillNodesList(nodes);
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Motion getMotion() {
        return motion;
    }

    public List<Node> getNodes() {
        return nodes;
    }
    
    public int getFrameSize() {
        return motion.getFrameSize();
    }
    
    public void setPose(int frameIndex) {
        if (frameIndex < 0) {
            rootNode.setPose(null);
        }
        else {
            rootNode.setPose(motion.getData(frameIndex));
        }
    }
    
}
