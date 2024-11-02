package com.example;

import java.io.*;
import java.util.*;

public class Main {
    private static String currentDir = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("cli> ");
            command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                break;
            } else if (command.equals("help")) {
                printHelp();
            } else {
                executeCommand(command);
            }
        }
    }

    public static void handleSpecialCases(List<String> commandList) throws IOException, InterruptedException {
        if (commandList.contains(">")) {
            int index = commandList.indexOf(">");
            List<String> cmd = commandList.subList(0, index);
            String file = commandList.get(index + 1);
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(currentDir, file)))) {
                executeCommand(cmd, writer);
            }
        } else if (commandList.contains(">>")) {
            int index = commandList.indexOf(">>");
            List<String> cmd = commandList.subList(0, index);
            String file = commandList.get(index + 1);
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(currentDir, file), true))) {
                executeCommand(cmd, writer);
            }
        } else if (commandList.contains("|")) {
            int index = commandList.indexOf("|");
            List<String> cmd1 = commandList.subList(0, index);
            List<String> cmd2 = commandList.subList(index + 1, commandList.size());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (PrintWriter writer = new PrintWriter(outputStream)) {
                executeCommand(cmd1, writer);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
                 PrintWriter writer = new PrintWriter(System.out)) {
                executeCommand(cmd2, reader, writer);
            }
        } else {
            executeCommand(commandList, new PrintWriter(System.out));
        }
    }

    private static void executeCommand(List<String> commandList, PrintWriter writer) throws IOException {
        executeCommand(commandList, new BufferedReader(new InputStreamReader(System.in)), writer);
    }

    private static void executeCommand(List<String> commandList, BufferedReader reader, PrintWriter writer) throws IOException {
        String command = commandList.get(0);
        switch (command) {
            case "pwd":
                writer.println(currentDir);
                break;
            case "cd":
                if (commandList.size() > 1) {
                    changeDirectory(commandList.get(1));
                } else {
                    writer.println("No directory specified");
                }
                break;
            case "ls":
                File dir = new File(currentDir);
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (commandList.contains("-a") || !file.isHidden()) {
                            writer.println(file.getName());
                        }
                    }
                }
                break;
            case "mkdir":
                if (commandList.size() > 1) {
                    File newDir = new File(currentDir, commandList.get(1));
                    if (!newDir.exists()) {
                        newDir.mkdir();
                    }
                } else {
                    writer.println("No directory specified");
                }
                break;
            case "rmdir":
                if (commandList.size() > 1) {
                    File rmDir = new File(currentDir, commandList.get(1));
                    if (rmDir.exists() && rmDir.isDirectory()) {
                        rmDir.delete();
                    }
                } else {
                    writer.println("No directory specified");
                }
                break;
            case "touch":
                if (commandList.size() > 1) {
                    File newFile = new File(currentDir, commandList.get(1));
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                } else {
                    writer.println("No file specified");
                }
                break;
            case "mv":
                if (commandList.size() > 2) {
                    File source = new File(currentDir, commandList.get(1));
                    File destination = new File(currentDir, commandList.get(2));
                    if (source.exists()) {
                        source.renameTo(destination);
                    }
                } else {
                    writer.println("Source or destination not specified");
                }
                break;
            case "rm":
                if (commandList.size() > 1) {
                    File file = new File(currentDir, commandList.get(1));
                    if (file.exists() && file.isFile()) {
                        file.delete();
                    }
                } else {
                    writer.println("No file specified");
                }
                break;
            case "cat":
                if (commandList.size() > 1) {
                    File catFile = new File(currentDir, commandList.get(1));
                    if (catFile.exists() && catFile.isFile()) {
                        try (BufferedReader fileReader = new BufferedReader(new FileReader(catFile))) {
                            String line;
                            while ((line = fileReader.readLine()) != null) {
                                writer.println(line);
                            }
                        }
                    } else {
                        writer.println("File not found: " + commandList.get(1));
                    }
                } else {
                    writer.println("No file specified");
                }
                break;
            case "echo":
                for (int i = 1; i < commandList.size(); i++) {
                    writer.print(commandList.get(i));
                    if (i < commandList.size() - 1) {
                        writer.print(" ");
                    }
                }
                writer.println();
                break;
            default:
                writer.println("Unknown command: " + command);
                break;
        }
        writer.flush();
    }

    public static String getCurrentDir() {
        return currentDir;
    }

    public static void changeDirectory(String newDir) {
        File dir = new File(currentDir, newDir);
        if (dir.exists() && dir.isDirectory()) {
            currentDir = dir.getAbsolutePath();
        } else {
            System.out.println("Directory not found: " + newDir);
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("pwd - Print working directory");
        System.out.println("cd <dir> - Change directory");
        System.out.println("ls - List directory contents");
        System.out.println("ls -a - List all directory contents including hidden files");
        System.out.println("ls -r - List directory contents reversed");
        System.out.println("mkdir <dir> - Create a new directory");
        System.out.println("rmdir <dir> - Remove a directory");
        System.out.println("touch <file> - Create a new file");
        System.out.println("mv <source> <destination> - Move or rename a file or directory");
        System.out.println("rm <file> - Remove a file");
        System.out.println("cat <file> - Display file contents");
        System.out.println("> <file> - Redirect output to a file");
        System.out.println(">> <file> - Append output to a file");
        System.out.println("| - Pipe output to another command");
    }

    private static void executeCommand(String command) throws IOException, InterruptedException {
        List<String> commandList = Arrays.asList(command.split(" "));
        handleSpecialCases(commandList);
    }
}
