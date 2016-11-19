package ru.hackrussia.SMT.BVFParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class BVFContent {
    private InnerStructureTree root;
    private ArrayList<InnerStructureTree> order;
    private Integer frameCount;
    private Double frameTime;

    private BVFContent() {}
    /** Parse BVF file
     * @throws BVFParseException
     */
    public BVFContent(InputStream input) throws BVFParseException {
        Scanner in = new Scanner(input);
        //Parse Header
        try {
            if (!in.next().equals("HIERARCHY")) {
                throw new BVFParseException();
            }
            order = new ArrayList<InnerStructureTree>();
            root = new InnerStructureTree(in, order);
            if (!in.next().equals("MOTION")) {
                throw new BVFParseException();
            }
            if (!in.next().equals("Frames:")) {
                throw new BVFParseException();
            }
            frameCount = in.nextInt();
            if (!in.next().equals("Frame")) {
                throw new BVFParseException();
            }
            if (!in.next().equals("Time:")) {
                throw new BVFParseException();
            }
            frameTime = in.nextDouble();
            for (int i = 0; i < frameCount; i++) {
                for (InnerStructureTree bone : order) {
                    ArrayList<Double> channelsValue = new ArrayList<Double>();
                    for (int k = 0; k < bone.channels.size(); k++) {
                        channelsValue.add(in.nextDouble());
                    }
                    bone.channelsValuesPerTime.add(channelsValue);
                }
            }

        }
        catch (Exception e) {
            throw new BVFParseException();
        }
    }

    /** Return RelativeSkeleton for frame number
     * @throws NoSuchFrameException
     */
    public Skeleton getRelativeSkeleton(Integer frame) throws NoSuchFrameException {return null;}
    public Integer getFramesCount() {
        return frameCount;
    }

    private class InnerStructureTree {
        private String name;
        private InnerStructureTree parent;
        private ArrayList<Double> offset;
        private ArrayList<String> channels;
        private ArrayList<InnerStructureTree> joints;
        private ArrayList<ArrayList<Double>> channelsValuesPerTime;

        private InnerStructureTree() {}
        private InnerStructureTree(Double offx, Double offy, Double offz) {
            name = "END";
            parent = null;
            offset = new ArrayList<Double>();
            offset.add(offx);
            offset.add(offy);
            offset.add(offz);
            channels = new ArrayList<String>();
            joints = new ArrayList<InnerStructureTree>();
            channelsValuesPerTime = new ArrayList<ArrayList<Double>>();
        }
        public InnerStructureTree(Scanner input, ArrayList<InnerStructureTree> order) throws BVFParseException {
            order.add(this);
            name = input.next();
            parent = null;
            if (!input.next().equals("{")) {
                throw new BVFParseException();
            }
            //Parse Offset
            offset = new ArrayList<Double>();
            if (!input.next().equals("OFFSET")) {
                throw new BVFParseException();
            }
            for (int i = 0; i < 3; i++) {
                offset.add(input.nextDouble());
            }
            //Parse Channels
            channels = new ArrayList<String>();
            if (!input.next().equals("CHANNELS")) {
                throw new BVFParseException();
            }
            Integer channelsCount = input.nextInt();
            for (int i = 0; i < channelsCount; i++) {
                channels.add(input.next());
            }
            //Parse Joins
            joints = new ArrayList<InnerStructureTree>();
            String tmp = input.next();
            while (!tmp.equals("}")) {
                if (tmp.equals("End")) {
                    //Read End Site
                    continue;
                }
                if (!tmp.equals("JOINT")) {
                    throw new BVFParseException();
                }
                InnerStructureTree tree = new InnerStructureTree(input, order);
                tree.parent = this;
                joints.add(tree);
            }
            channelsValuesPerTime = new ArrayList<ArrayList<Double>>();
        }
    }

    public class Skeleton {
        private ArrayList<Double> relativePosition;
        private ArrayList<Skeleton> joints;
        private HashMap<String, Skeleton> namedBones;

        private Skeleton() {}
        private Skeleton(Integer frame, InnerStructureTree tree) {}
        private Skeleton(Integer frame, InnerStructureTree tree, ArrayList<Double> parentAbsolutePosition) {}

        /** Return bone with such name */
        public Skeleton getByName(String name) {return null;}
        /** Return offset of bone related to parent bone */
        public ArrayList<Double> getRelativeOffset() {return null;}
        /** Return rotation of bone related to parent bone */
        public ArrayList<Double> getRelativeRotation() {return null;}
        /** Return absolute offset from skeleton root */
        public ArrayList<Double> getAbsoluteOffset() {return null;}
        /** Return absolute rotation from skeleton root */
        public ArrayList<Double> getAbsoluteRotation() {return null;}
        /** Return absolute position from world center */
        public ArrayList<Double> getAbsolutePoint() {return null;}
        /** Return ArrayList of child Joints */
        public ArrayList<Skeleton> getJoints() {return null;}
    }

}
