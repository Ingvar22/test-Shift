import java.io.*;
import java.util.LinkedList;
import java.lang.String;

public class util {
    public static int comEl = 0;
    public static int intCount = 0;
    public static int wordsCount = 0;
    public static int strCount = 0;
    public static int floatCount = 0;
    public static long minInt = 0;
    public static long maxInt = 0;
    public static double minFlt = 0.0;
    public static double maxFlt = 0.0;
    public static long sumInt = 0;
    public static double sumFlt = 0.0;
    public static int maxLenStr = 0, minLenStr = 0;
    public static String minStr = "";

    public static boolean isInteger(String line)
    {
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
        if(stat == 's' || stat == 'f'){
            System.out.println("Количество элементов: " + comEl);
            System.out.println("Файл со строками: " + wordsCount + " слов, " + strCount + " строк");
            System.out.println("Файл с целыми числами: " + intCount);
            System.out.println("Файл с числами с плавающей точкой: " + floatCount);
            if(stat == 'f') {
                System.out.println("\n\nЦелые числа:\n");
                System.out.println("Минимальный: " + minInt);
                System.out.println("Максимальный: " + maxInt);
                System.out.println("Сумма: " + sumInt);
                System.out.println("Среднее: " + sumInt/intCount);
                System.out.println("\n\nЧисла с плавающей точкой:\n");
                System.out.println("Минимальный: " + minFlt);
                System.out.println("Максимальный: " + maxFlt);
                System.out.println("Сумма: " + sumFlt);
                System.out.println("Среднее: " + sumFlt/floatCount);
                System.out.println("\n\nСтроки:\n");
                System.out.println("Минимальная длина: " + minLenStr);
                System.out.println("Максимальный длина: " + maxLenStr);
            }
        }
        System.out.println("\n#############################\n");
    }


    public static void fileProcessing(BufferedReader reader, String path, String prefix, String[] nameResFiles, char stat) {
        try {
            String line = reader.readLine();
            while(line != null)
            {
                try {
                    if (isInteger(line)) {
                        integerProcessing(line, path, prefix, nameResFiles[0], stat);
                    } else if (isFloat(line)) {
                        floatProcessing(line, path, prefix, nameResFiles[1], stat);
                    } else {
                        stringProcessing(line, path, prefix, nameResFiles[2], stat);
                    }
                } catch (IOException ex) {
                    System.out.println("Ошибка при записи в файл: " + ex.getMessage());
                }
                line = reader.readLine();
            }
        } catch (IOException ex) {
            System.out.println("Ошибка при чтении файла: " + ex.getMessage());
        }
    }

    public static void integerProcessing(String line, String path, String prefix, String nameResFile, char stat) throws IOException{
        try (FileWriter writer = new FileWriter(path + prefix + nameResFile, true)) {
            if(stat == 's' || stat == 'f') {
                if(!line.isEmpty()) {
                    ++comEl;
                    ++intCount;
                    if(stat == 'f') {
                        if(minInt == 0) minInt = Long.parseLong(line);
                        sumInt += Long.parseLong(line);
                        if (Long.parseLong(line) < minInt) {
                            minInt = Long.parseLong(line);
                        } else if (Long.parseLong(line) > maxInt) {
                            maxInt = Long.parseLong(line);
                        }
                    }
                }
            }

            writer.write(line + "\n");
        }
    }

    public static void floatProcessing(String line, String path, String prefix, String nameResFile, char stat) throws IOException{
        try (FileWriter writer = new FileWriter(path + prefix + nameResFile, true)) {
            if(stat == 's' || stat == 'f') {
                if(!line.isEmpty()) {
                    ++comEl;
                    ++floatCount;
                    if(stat == 'f') {
                        if(minFlt == 0.0) minFlt = Double.parseDouble(line);
                        sumFlt += Double.parseDouble(line);
                        if (Double.parseDouble(line) < minFlt) {
                            minFlt = Double.parseDouble(line);
                        } else if (Double.parseDouble(line) > maxFlt) {
                            maxFlt = Double.parseDouble(line);
                        }
                    }
                }
            }
            writer.write(line + "\n");
        }
    }

    public static void stringProcessing(String line, String path, String prefix, String nameResFile, char stat) throws IOException{
        try (FileWriter writer = new FileWriter(path + prefix + nameResFile, true)) {
            if(stat == 's' || stat == 'f') {
                if(!line.isEmpty()) {
                    ++comEl;
                    ++wordsCount;
                    ++strCount;
                    for(int j = 0; j < line.length(); ++j) {
                        if(line.charAt(j) == ' ') {
                            ++comEl;
                            ++wordsCount;
                        }
                    }
                }
                if(stat == 'f') {
                    if(minStr.isEmpty()) minStr = line;
                    if(line.compareTo(minStr) < 0) {
                        minStr = line;
                        minLenStr = line.length();
                    } else {
                        maxLenStr = line.length();
                    }
                }
            }
            writer.write(line + "\n");
        }
    }

    public static void main(String[] args) {
        String path = "";
        String prefix = "";
        String[] nameResFiles = {"integers.txt", "floats.txt", "strings.txt"};
        boolean writeFormat = true;
        char stat = ' ';

        LinkedList<String> listFiles = new LinkedList<>();

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-o":  {
                    path = args[i + 1];
                    ++i;
                    break;
                } case "-p": {
                    prefix = args[i + 1];
                    ++i;
                    break;
                } case "-a": {
                    writeFormat = false;
                    break;
                } case "-s": {
                    stat = 's';
                    break;
                } case "-f": {
                    stat = 'f';
                    break;
                } default: {
                    if(!args[i].endsWith(".txt")) {
                        System.out.println("Введите файлы необходимого расщирения(.txt)");
                        System.exit(0);
                    }
                    listFiles.add(args[i]);
                }
            }
        }
       
        if(listFiles.isEmpty()){
            System.out.println("Файл(ы) не был(и) введены.");
            System.exit(0);
        }

        if(!path.isEmpty() && path.charAt(path.length()-1) != '/') path += '/';

        File directory = new File(path);
        if(!path.isEmpty() && !directory.exists()) {
            if(directory.mkdirs()) {
                System.out.println("Директория создана: " + path);
            } else System.out.println("Не удалось создать директорию: " + path);
        }

        if(writeFormat) {
            File file;
            for(String fileName : nameResFiles) {
                file = new File(path + prefix + fileName);
                System.out.print(prefix+fileName);
                if(file.exists()) {
                    if(file.delete()) {
                        System.out.println(" перезаписан.");
                    }
                } else if(path.isEmpty()) System.out.println(" добавлен в текущую директорию.");
                else System.out.println(" в путь " + path);
            }
        } else {
            File file;
            for(String fileName : nameResFiles) {
                file = new File(path + prefix + fileName);
                if(file.exists()) {
                    System.out.println(prefix+fileName + " добавились новые данные.");
                } else {
                    System.out.println("Невозможно добавить новые данные.\n" +
                            "В данной директории нет файла для взаимодейсвия.");
                    System.exit(0);
                }
            }
        }

        for(String fileName : listFiles) {
            try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                fileProcessing(reader, path, prefix, nameResFiles, stat);
            } catch (IOException ex) {
                System.out.println("Ошибка при чтении файла: " + ex.getMessage());
            }
        }
        if(stat != ' ') printStatistic(stat);
    }
}
