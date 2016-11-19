package ru.hackrussia.SMT.BVFParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
        Scanner in = new Scanner(input).useLocale(Locale.US);
        //Parse Header
        try {
            if (!in.next().equals("HIERARCHY")) {
                throw new BVFParseException();
            }
            if (!in.next().equals("ROOT")) {
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
        catch (BVFParseException e) {
            throw e;
        }
        catch (Exception e) {
            throw new BVFParseException();
        }
    }

    /** Return RelativeSkeleton for frame number
     * @throws NoSuchFrameException
     */
    public Skeleton getRelativeSkeleton(Integer frame) throws NoSuchFrameException {
        if (frame >= frameCount) {
            throw new NoSuchFrameException();
        }
        return new Skeleton(frame, root);
    }
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
            if (channelsCount != 6) {
                throw new BVFParseException();
            }
            for (int i = 0; i < channelsCount; i++) {
                channels.add(input.next());
            }
            //Parse Joins
            joints = new ArrayList<InnerStructureTree>();
            String tmp = input.next();
            while (!tmp.equals("}")) {
                if (tmp.equals("End")) {
                    if (!input.next().equals("Site")) {
                        throw new BVFParseException();
                    }
                    if (!input.next().equals("{")) {
                        throw new BVFParseException();
                    }
                    if (!input.next().equals("OFFSET")) {
                        throw new BVFParseException();
                    }
                    InnerStructureTree end = new InnerStructureTree(input.nextDouble(), input.nextDouble(), input. nextDouble());
                    end.parent = this;
                    joints.add(end);
                    if (!input.next().equals("}")){
                        throw new BVFParseException();
                    }
                    tmp = input.next();
                    continue;
                }
                if (!tmp.equals("JOINT")) {
                    throw new BVFParseException();
                }
                InnerStructureTree tree = new InnerStructureTree(input, order);
                tree.parent = this;
                joints.add(tree);
                tmp = input.next();
            }
            channelsValuesPerTime = new ArrayList<ArrayList<Double>>();
        }
    }

    public class Skeleton {
        private ArrayList<Double> relativeOffset;
        private ArrayList<Double> relativeRotation;
        private ArrayList<Double> absoluteOffset;
        private ArrayList<Double> absoluteRotation;
        private ArrayList<Double> absolutePosition;
        private ArrayList<Skeleton> joints;
        private HashMap<String, Skeleton> namedBones;

        private Skeleton() {}
        //Constructor for Root
        private Skeleton(Integer frame, InnerStructureTree tree) {
            relativeOffset = new ArrayList<Double>();
            relativeRotation = new ArrayList<Double>();
            absolutePosition = new ArrayList<Double>();
            absoluteOffset = new ArrayList<Double>();
            absoluteRotation = new ArrayList<Double>();
            joints = new ArrayList<Skeleton>();
            namedBones = new HashMap<String, Skeleton>();
            namedBones.put(tree.name, this);
            for (int i = 0; i < 3; i++) {
                relativeOffset.add(0.0);
                relativeRotation.add(0.0);
                absoluteOffset.add(0.0);
                absoluteRotation.add(0.0);
                absolutePosition.add(tree.channelsValuesPerTime.get(frame).get(i) + tree.offset.get(i));
            }
            ArrayList<Double> rotation = new ArrayList<Double>();
            for (int i = 0; i < 3; i++) {
                rotation.add(tree.channelsValuesPerTime.get(frame).get(i+3));
            }
            for (InnerStructureTree bone : tree.joints) {
                Skeleton sub = new Skeleton(frame, bone, this, rotation);
                joints.add(sub);
                namedBones.putAll(sub.namedBones);
            }
        }
        //Constructor for Joint
        private Skeleton(Integer frame, InnerStructureTree tree, Skeleton parent, ArrayList<Double> rotation) {
            relativeOffset = new ArrayList<Double>();
            relativeRotation = rotation;
            absolutePosition = new ArrayList<Double>();
            absoluteOffset = new ArrayList<Double>();
            absoluteRotation = new ArrayList<Double>();
            Double cx = Math.cos(rotation.get(0));
            Double sx = Math.sin(rotation.get(0));
            Double cy = Math.cos(rotation.get(1));
            Double sy = Math.sin(rotation.get(1));
            Double cz = Math.cos(rotation.get(2));
            Double sz = Math.sin(rotation.get(2));
            ArrayList<Double> a1 = new ArrayList<Double>();
            ArrayList<Double> a2 = new ArrayList<Double>();
            ArrayList<Double> a3 = new ArrayList<Double>();
            a1.add(cy * cz);
            a2.add(cz * sx * sy + cx * sz);
            a3.add(sx * sz - cx * cz * sy);
            a1.add(- cy * sz);
            a2.add(cx * cz - sx * sy * sz);
            a3.add(cz * cx + cx * sy * sz);
            a1.add(sy);
            a2.add(- cy * sx);
            a3.add(cy * cx);
            if (tree.channelsValuesPerTime.size() > 0) {
                for (int i = 0; i < 3; i++) {
                    relativeOffset.add(a1.get(i) * tree.offset.get(1) +
                            a2.get(i) * tree.offset.get(2) +
                            a3.get(i) * tree.offset.get(3) +
                            tree.channelsValuesPerTime.get(frame).get(i));
                    absolutePosition.add(parent.absolutePosition.get(i) + relativeOffset.get(i));
                    absoluteOffset.add(parent.absoluteOffset.get(i) + relativeOffset.get(i));
                    absoluteRotation.add(parent.absoluteRotation.get(i) + rotation.get(i));
                }
                ArrayList<Double> rot = new ArrayList<Double>();
                for (int i = 0; i < 3; i++) {
                    rotation.add(tree.channelsValuesPerTime.get(frame).get(i + 3));
                }
                for (InnerStructureTree bone : tree.joints) {
                    Skeleton sub = new Skeleton(frame, bone, this, rot);
                    joints.add(sub);
                    namedBones.putAll(sub.namedBones);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    relativeOffset.add(a1.get(i) * tree.offset.get(1) +
                            a2.get(i) * tree.offset.get(2) +
                            a3.get(i) * tree.offset.get(3));
                    absolutePosition.add(parent.absolutePosition.get(i) + relativeOffset.get(i));
                    absoluteOffset.add(parent.absoluteOffset.get(i) + relativeOffset.get(i));
                    absoluteRotation.add(parent.absoluteRotation.get(i) + rotation.get(i));
                }
            }
        }

        /** Return bone with such name */
        public Skeleton getByName(String name) {
            return namedBones.get(name);
        }
        /** Return offset of bone related to parent bone */
        public ArrayList<Double> getRelativeOffset() {
            return new ArrayList<Double>(relativeOffset);
        }
        /** Return rotation of bone related to parent bone */
        public ArrayList<Double> getRelativeRotation() {
            return new ArrayList<Double>(relativeRotation);
        }
        /** Return absolute offset from skeleton root */
        public ArrayList<Double> getAbsoluteOffset() {
            return new ArrayList<Double>(absoluteOffset);
        }
        /** Return absolute rotation from skeleton root */
        public ArrayList<Double> getAbsoluteRotation() {
            return new ArrayList<Double>(absoluteRotation);
        }
        /** Return absolute position from world center */
        public ArrayList<Double> getAbsolutePoint() {
            return new ArrayList<Double>(absolutePosition);
        }
        /** Return ArrayList of child Joints */
        public ArrayList<Skeleton> getJoints() {
            return new ArrayList<Skeleton>(joints);
        }
    }

}
