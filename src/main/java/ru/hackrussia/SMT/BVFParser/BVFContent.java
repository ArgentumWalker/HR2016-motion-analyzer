package ru.hackrussia.SMT.BVFParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BVFContent {
    private InnerStructureTree root;

    private BVFContent() {}
    /** Parse BVF file
     * @throws BVFParseException
     */
    public BVFContent(InputStream input) throws BVFParseException {}

    /** Return RelativeSkeleton for frame number
     * @throws NoSuchFrameException
     */
    public Skeleton getRelativeSkeleton(Integer frame) throws NoSuchFrameException {return null;}
    public Integer getFramesCount() {return null;}

    private class InnerStructureTree {
        private String name;
        private InnerStructureTree parent;
        private ArrayList<Double> offset;
        private ArrayList<String> channels;
        private ArrayList<InnerStructureTree> joints;
        private ArrayList<ArrayList<Double>> channelsValuesPerTime;

        private InnerStructureTree() {}
        public InnerStructureTree(InputStream input) {}
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
