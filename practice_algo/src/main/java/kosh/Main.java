package kosh;

import kosh.Kmeans.KMeans;
import org.silentsoft.arguments.parser.Arguments;
import org.silentsoft.arguments.parser.ArgumentsParser;
import org.silentsoft.arguments.parser.InvalidArgumentsException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE);
            return;
        }
        ArgsParser argsParser = new ArgsParser();
        ParsedArgs parsed = ArgsParser.parseArgs(args);
        if (parsed == null) {
            System.err.println(USAGE);
            return;
        }
        if (parsed.fileName() == null) {
            System.err.println("Couldn't get file name to download");
            return;
        }

        File file = new File(parsed.fileName());
        int k = parsed.k();
        System.out.println(parsed.fileName() + ", " + parsed.k());

        GdalFormater formater = new GdalFormater();
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }
        Data data = formater.loadData(null);

        System.out.println("Available num of bands: " + Arrays.toString(formater.getBandsDescription()));
        boolean[] activeBands = new boolean[formater.getBandsNumber()];
        System.out.println("Select bands to show(rgb):");
        ArgsParser.parseBands(activeBands);
        System.out.println(Arrays.toString(activeBands)); // debug


//        KMeans algo = new KMeans(data.getDataPoints(), k);
//        algo.run();

//        BandsAsRGB distribution = new BandsAsRGB(1, 3, 8);

        // отобразить список каналов
        // какие каналы загружать, какие отображать в качестве каких
        // после применения алгоритма
        // срдение цвета -- сумма по пикселям/ колв-во пикселей встретившихся/ случайные цвета
        // цвет для кластера,
//        ImageConstructor constructor = new ImageConstructor(data.getDataPoints()[1], data.getDataPoints()[4],
//                                                        data.getDataPoints()[7], data.getWidth(), data.getHeight());
//        BufferedImage img = constructor.constructImage();
//        if (img != null) {
//            System.out.println(img);
//            ImageWindow imageWindow = new ImageWindow(img);
//            imageWindow.displayImage();
//        }


    }
    private static final String USAGE = "Gimme args: <file_to_open> <[bands_to_download]> <[bands_to_show]>";
}