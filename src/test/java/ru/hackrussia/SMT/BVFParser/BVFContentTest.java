package ru.hackrussia.SMT.BVFParser;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BVFContentTest {
    @Test
    public void ConstructorTest_OnlyRoot_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 0\n" +
                "    End Site\n" +
                "    {\n" +
                "         OFFSET 0.0 0.0 0.0\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 0\n" +
                "Frame Time: 0.017").getBytes());
        new BVFContent(inpStr);
    }
    @Test
    public void ConstructorTest_OnlyRootWithChannels_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 2 channel1 channel2\n" +
                "    End Site\n" +
                "    {\n" +
                "         OFFSET 0.0 0.0 0.0\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 0\n" +
                "Frame Time: 0.017").getBytes());
        new BVFContent(inpStr);
    }
    @Test
    public void ConstructorTest_SingleJointTest_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 0\n" +
                "    JOINTS leg\n" +
                "    {\n" +
                "        OFFSET 0.0 0.0 0.0\n" +
                "        CHANNELS 2 channel1 channel2" +
                "        End Site\n" +
                "        {\n" +
                "             OFFSET 0.0 0.0 0.0\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 0\n" +
                "Frame Time: 0.017").getBytes());
        new BVFContent(inpStr);
    }
    @Test
    public void ConstructorTest_MultiJointsTest_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 0\n" +
                "    JOINTS leg1\n" +
                "    {\n" +
                "        OFFSET 0.0 0.0 0.0\n" +
                "        CHANNELS 2 channel1 channel2" +
                "        End Site\n" +
                "        {\n" +
                "             OFFSET 0.0 0.0 0.0\n" +
                "        }\n" +
                "    }\n" +
                "    JOINTS leg2\n" +
                "    {\n" +
                "        OFFSET 0.0 0.0 0.0\n" +
                "        CHANNELS 2 channel1 channel2" +
                "        End Site\n" +
                "        {\n" +
                "             OFFSET 0.0 0.0 0.0\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 0\n" +
                "Frame Time: 0.017").getBytes());
        new BVFContent(inpStr);
    }
    @Test
    public void ConstructorTest_OnlyRootSingleFrame_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 3 channel1 channel2\n" +
                "    End Site\n" +
                "    {\n" +
                "         OFFSET 0.0 0.0 0.0\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 1\n" +
                "Frame Time: 0.017\n" +
                "0.000 0.000 0.000").getBytes());
        new BVFContent(inpStr);
        //TODO: check order of numbers
    }
    @Test
    public void ConstructorTest_OnlyRootMultiFrames_NotFail() throws Exception {
        InputStream inpStr = new ByteArrayInputStream(("HIERARCHY\n" +
                "ROOT Hips\n" +
                "{\n" +
                "    OFFSET 0.0 0.0 0.0\n" +
                "    CHANNELS 3 channel1 channel2\n" +
                "    End Site\n" +
                "    {\n" +
                "         OFFSET 0.0 0.0 0.0\n" +
                "    }\n" +
                "}\n" +
                "MOTION\n" +
                "\n" +
                "Frames: 4\n" +
                "Frame Time: 0.017\n" +
                "0.000 0.000 0.000\n" +
                "1.000 1.000 1.000\n" +
                "2.000 2.000 2.000\n" +
                "0.000 0.000 0.000\n").getBytes());
        new BVFContent(inpStr);
        //TODO: check order of numbers
    }
}