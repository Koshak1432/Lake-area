package kosh;

import kosh.Kmeans.KMeans;
import org.silentsoft.arguments.parser.Arguments;
import org.silentsoft.arguments.parser.ArgumentsParser;
import org.silentsoft.arguments.parser.InvalidArgumentsException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println(USAGE);
        }
        String fileName = parseArgs(args);
        if (fileName == null) {
            System.err.println("Couldn't get file name to download");
            return;
        }

        File file = new File(fileName);

        GdalFormater formater = new GdalFormater();
        if (!formater.loadHeader(file)) {
            System.err.println("Couldn't load header");
            return;
        }
        Data data = formater.loadData(null);

        System.out.println("Available num of bands: " + Arrays.toString(formater.getBandsDescription()));
        boolean[] activeBands = new boolean[formater.getBandsNumber()];
        // спросить какие загружать???? они уже загружены лол
        // спросить какие отображать в качестве rgb

//        boolean[] activeBands = new boolean[]{true, false, false, true, false, false, true, false};
//        boolean[] activeBands = null;
//

//        int k = 5;
//
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

    private static String parseArgs(String[] args) {
        String fileNameOpt = "-f";
        Arguments arguments;
        try {
            arguments = ArgumentsParser.parse(args);
        } catch (InvalidArgumentsException e) {
            e.printStackTrace();
            System.err.println(USAGE);
            return null;
        }

        if (arguments.containsKey(fileNameOpt)) {
            return arguments.get(fileNameOpt).getValue();
        }
        return null;
    }


    private static final String USAGE = "Gimme args: <file_to_open> <[bands_to_download]> <[bands_to_show]>";
}