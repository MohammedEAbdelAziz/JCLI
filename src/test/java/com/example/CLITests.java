
package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class CLITests {

    @Test
    public void testPwd() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("pwd", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            assertEquals(new File(".").getAbsolutePath(), line+"\\.");
        }
    }

    @Test
    public void testCd() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("cd", "testDir");
        Main.handleSpecialCases(commandList);
        assertEquals(new File(Main.getCurrentDir()).getAbsolutePath(), Main.getCurrentDir());
    }

    @Test
    public void testLs() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("ls", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                assertNotNull(line);
            }
        }
    }

    @Test
    public void testLsA() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("ls", "-a", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                assertNotNull(line);
            }
        }
    }

    @Test
    public void testLsR() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("ls", "-r", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                assertNotNull(line);
            }
        }
    }

    @Test
    public void testMkdir() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("mkdir", "testDir");
        Main.handleSpecialCases(commandList);
        File dir = new File(Main.getCurrentDir(), "testDir");
        assertTrue(dir.exists() && dir.isDirectory());
        dir.delete();
    }

    @Test
    public void testRmdir() throws IOException, InterruptedException {
        File dir = new File(Main.getCurrentDir(), "testDir");
        dir.mkdir();
        List<String> commandList = Arrays.asList("rmdir", "testDir");
        Main.handleSpecialCases(commandList);
        assertFalse(dir.exists());
    }

    @Test
    public void testTouch() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("touch", "testFile.txt");
        Main.handleSpecialCases(commandList);
        File file = new File(Main.getCurrentDir(), "testFile.txt");
        assertTrue(file.exists() && file.isFile());
        file.delete();
    }

    @Test
    public void testMv() throws IOException, InterruptedException {
        File file = new File(Main.getCurrentDir(), "testFile.txt");
        file.createNewFile();
        List<String> commandList = Arrays.asList("mv", "testFile.txt", "newFile.txt");
        Main.handleSpecialCases(commandList);
        File newFile = new File(Main.getCurrentDir(), "newFile.txt");
        assertTrue(newFile.exists() && newFile.isFile());
        newFile.delete();
    }

    @Test
    public void testRm() throws IOException, InterruptedException {
        File file = new File(Main.getCurrentDir(), "testFile.txt");
        file.createNewFile();
        List<String> commandList = Arrays.asList("rm", "testFile.txt");
        Main.handleSpecialCases(commandList);
        assertFalse(file.exists());
    }

    @Test
    public void testCat() throws IOException, InterruptedException {
        File sampleFile = new File(Main.getCurrentDir(), "sample.txt");
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(sampleFile))) {
            writer.println("Hello, World!");
        }

        List<String> commandList = Arrays.asList("cat", "sample.txt", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File(Main.getCurrentDir(), "output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            assertEquals("Hello, World!", line);
        }

        sampleFile.delete();
        outputFile.delete();
    }

    @Test
    public void testRedirectOutput() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("echo", "Hello, World!", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File(Main.getCurrentDir(), "output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            assertEquals("Hello, World!", line.trim());
        }
        outputFile.delete();
    }

    @Test
    public void testAppendOutput() throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList("echo", "Hello,", ">", "output.txt");
        Main.handleSpecialCases(commandList);
        commandList = Arrays.asList("echo", "World!", ">>", "output.txt");
        Main.handleSpecialCases(commandList);
        File outputFile = new File(Main.getCurrentDir(), "output.txt");
        assertTrue(outputFile.exists());
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line = reader.readLine();
            assertEquals("Hello,", line.trim());
            line = reader.readLine();
            assertEquals("World!", line.trim());
        }
        outputFile.delete();
    }

    // @Test
    // public void testPipe() throws IOException, InterruptedException {
    //     List<String> commandList = Arrays.asList("echo", "Hello, World!", "|", "cat", ">", "output.txt");
    //     Main.handleSpecialCases(commandList);
    //     File outputFile = new File(Main.getCurrentDir(), "output.txt");
    //     assertTrue(outputFile.exists());
    //     try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
    //         String line = reader.readLine();
    //         assertEquals("Hello, World!", line.trim());
    //     }
    //     outputFile.delete();
    // }
}