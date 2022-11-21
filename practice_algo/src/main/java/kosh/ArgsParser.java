package kosh;

import org.silentsoft.arguments.parser.Arguments;
import org.silentsoft.arguments.parser.ArgumentsParser;
import org.silentsoft.arguments.parser.InvalidArgumentsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgsParser {
    public static ParsedArgs parseArgs(String[] args) {
        String fileNameOpt = "-f";
        String clustersNumOpt = "-k";
        String fileName;
        int k;
        Arguments arguments;
        try {
            arguments = ArgumentsParser.parse(args);
        } catch (InvalidArgumentsException e) {
            e.printStackTrace();
            return null;
        }

        if (arguments.containsKey(clustersNumOpt)) {
            k = Integer.parseInt(arguments.get(clustersNumOpt).getValue());
            if (k < 0) {
                return null;
            }
        } else {
            return null;
        }
        if (arguments.containsKey(fileNameOpt)) {
            fileName = arguments.get(fileNameOpt).getValue();
        } else {
            return null;
        }

        return new ParsedArgs(fileName, k);
    }

    public static void parseBands(boolean[] activeBands) {
        List<String> sIntegersList = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        Pattern intPattern = Pattern.compile("\\d+");
        Matcher matcher;
        String input;
        boolean error;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                sIntegersList.clear();
                integers.clear();
                error = false;
                input = scanner.nextLine();
                matcher = intPattern.matcher(input);

                while (matcher.find()) {
                    sIntegersList.add(matcher.group());
                }
                if (sIntegersList.size() < 3) {
                    continue;
                }
                for (String sInt : sIntegersList) {
                    int num = Integer.parseInt(sInt);
                    int arrIdx = num - 1;
                    if (arrIdx < 0 || arrIdx > activeBands.length) {
                        error = true;
                        break;
                    }
                    integers.add(arrIdx);
                }
                if (!error) {
                    for (Integer num : integers) {
                        activeBands[num] = true;
                    }
                    break;
                }
                System.out.println("Select bands to show(rgb):");
            }
        }
    }

}
