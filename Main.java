import java.io.*;
import java.util.LinkedList;

public class Main {
    private static int totalElements = 0;
    private static int integerCount = 0;
    private static int wordCount = 0;
    private static int stringCount = 0;
    private static int floatCount = 0;
    private static long minInteger = Long.MAX_VALUE;
    private static long maxInteger = Long.MIN_VALUE;
    private static double minFloat = Double.MAX_VALUE;
    private static double maxFloat = Double.MIN_VALUE;
    private static long sumInteger = 0;
    private static double sumFloat = 0.0;
    private static int maxLengthString = 0;
    private static int minLengthString = Integer.MAX_VALUE;

    public static boolean isInteger(String line) {
        try {
            Long.parseLong(line);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String line) {
        try {
            Double.parseDouble(line);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void printStatistic(char stat) {
        System.out.println(stat == 's' ? "\n\n###### Краткая статистика ######\n" : "\n\n###### Полная статистика ######\n");
        if (stat == 's' || stat == 'f') {
            System.out.println("Amount of elements: " + totalElements);
            System.out.println("String file: " + wordCount + " words, " + stringCount + " strings");
            System.out.println("Integer file: " + integerCount);
            System.out.println("Float file: " + floatCount);
            if (stat == 'f') {
                System.out.println("Минимальное целое число: " + minInteger);
                System.out.println("Max int: " + maxInteger);
                System.out.println("Min float: " + minFloat);
                System.out.println("Max float: " + maxFloat);
                System.out.println("Sum int: " + sumInteger);
                System.out.println("Average int: " + (integerCount == 0 ? 0 : sumInteger / (double) integerCount));
                System.out.println("Sum float: " + sumFloat);
                System.out.println("Average float: " + (floatCount == 0 ? 0 : sumFloat / floatCount));
                System.out.println("Min length string: " + (minLengthString == Integer.MAX_VALUE ? 0 : minLengthString));
                System.out.println("Max length string: " + maxLengthString);
            }
        }
        System.out.println("\n#############################\n");
    }

    public static void processFile(String filePath, String outputPath, String prefix, String[] outputFileNames, char stat) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (isInteger(line)) {
                        processInteger(line, outputPath, prefix, outputFileNames[0], stat);
                    } else if (isFloat(line)) {
                        processFloat(line, outputPath, prefix, outputFileNames[1], stat);
                    } else {
                        processString(line, outputPath, prefix, outputFileNames[2], stat);
                    }
                } catch (IOException ex) {
                    System.out.println("Error writing to file: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }
    }

    public static void processInteger(String line, String outputPath, String prefix, String outputFileName, char stat) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath + prefix + outputFileName, true)) {
            if (!line.isEmpty()) {
                ++totalElements;
                ++integerCount;
                long value = Long.parseLong(line);
                sumInteger += value;
                if (stat == 'f') {
                    minInteger = Math.min(minInteger, value);
                    maxInteger = Math.max(maxInteger, value);
                }
            }
            writer.write(line + "\n");
        }
    }

    public static void processFloat(String line, String outputPath, String prefix, String outputFileName, char stat) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath + prefix + outputFileName, true)) {
            if (!line.isEmpty()) {
                ++totalElements;
                ++floatCount;
                double value = Double.parseDouble(line);
                sumFloat += value;
                if (stat == 'f') {
                    minFloat = Math.min(minFloat, value);
                    maxFloat = Math.max(maxFloat, value);
                }
            }
            writer.write(line + "\n");
        }
    }

    public static void processString(String line, String outputPath, String prefix, String outputFileName, char stat) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath + prefix + outputFileName, true)) {
            if (!line.isEmpty()) {
                ++totalElements;
                ++wordCount;
                ++stringCount;
                for (int j = 0; j < line.length(); ++j) {
                    if (line.charAt(j) == ' ') {
                        ++totalElements;
                        ++wordCount;
                    }
                }
                if (stat == 'f') {
                    minLengthString = Math.min(minLengthString, line.length());
                    maxLengthString = Math.max(maxLengthString, line.length());
                }
            }
            writer.write(line + "\n");
        }
    }

    public static void main(String[] args) {
        String outputPath = "";
        String prefix = "";
        String[] outputFileNames = {"integers.txt", "floats.txt", "strings.txt"};
        boolean appendMode = false;
        char stat = ' ';

        LinkedList<String> inputFiles = new LinkedList<>();

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-o":
                    outputPath = args[++i];
                    break;
                case "-p":
                    prefix = args[++i];
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    stat = 's';
                    break;
                case "-f":
                    stat = 'f';
                    break;
                default:
                    if (!args[i].endsWith(".txt")) {
                        System.out.println("Please enter files with the .txt extension");
                        System.exit(0);
                    }
                    inputFiles.add(args[i]);
            }
        }

        if (inputFiles.isEmpty()) {
            System.out.println("No input files provided.");
            System.exit(0);
        }

        if (!outputPath.isEmpty() && outputPath.charAt(outputPath.length() - 1) != '/') {
            outputPath += '/';
        }

        File directory = new File(outputPath);
        if (!outputPath.isEmpty() && !directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + outputPath);
            } else {
                System.out.println("Failed to create directory: " + outputPath);
            }
        }

        if (!appendMode) {
            for (String fileName : outputFileNames) {
                File file = new File(outputPath + prefix + fileName);
                if (file.exists()) {
                    if (!file.delete()) {
                        System.out.println("Failed to reset file: " + fileName);
                    }
                }
            }
        } else {
            for (String fileName : outputFileNames) {
                File file = new File(outputPath + prefix + fileName);
                if (!file.exists()) {
                    System.out.println("Cannot append data. File not found: " + fileName);
                    System.exit(0);
                }
            }
        }

        for (String inputFile : inputFiles) {
            processFile(inputFile, outputPath, prefix, outputFileNames, stat);
        }

        if (stat != ' ') {
            printStatistic(stat);
        }
    }
}
