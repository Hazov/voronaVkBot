package ru.voronavk.utils;

import ru.voronavk.entities.FormatPhoto;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintPhotoUtil {
    public static final Map<FormatPhoto, Map<String,Double>> standardPricesPapper = new HashMap<>();
    public static final FormatPhoto a3plus = new FormatPhoto(32.9, 48.3);
    public static final double maxWidth = a3plus.getWidth();
    public static final double maxHeigth = a3plus.getHeight();
    public static final FormatPhoto a3 = new FormatPhoto(29.7, 42);
    public static final FormatPhoto a4 = new FormatPhoto(21.0, 29.7);
    public static final FormatPhoto a5 = new FormatPhoto(14.8, 21.0);
    public static final FormatPhoto a6 = new FormatPhoto(10.0, 15.0);



    static {
        //10x15
        Map<String, Double> prices1015 = new HashMap<>();
        prices1015.put("satin", 15d);
        prices1015.put("glossy", 10d);
        prices1015.put("matte", 10d);
        standardPricesPapper.put(a6, prices1015);
        //15x21
        Map<String, Double> prices1521 = new HashMap<>();
        prices1521.put("satin", 50d);
        prices1521.put("glossy", 25d);
        prices1521.put("matte", 25d);
        standardPricesPapper.put(a5, prices1521);
        //A4
        Map<String, Double> pricesA4 = new HashMap<>();
        pricesA4.put("satin", 100d);
        pricesA4.put("glossy", 60d);
        pricesA4.put("matte", 60d);
        standardPricesPapper.put(a4, pricesA4);
        //A3
        Map<String, Double> pricesA3 = new HashMap<>();
        pricesA3.put("glossy", 120d);
        pricesA3.put("matte", 120d);
        standardPricesPapper.put(a3, pricesA3);
        //Полароид
        Map<String, Double> pricesPolaroid = new HashMap<>();
        pricesPolaroid.put("satin", 15d);
        pricesPolaroid.put("glossy", 10d);
        pricesPolaroid.put("matte", 10d);
        standardPricesPapper.put(a6, pricesPolaroid);
    }


    public static Double priceOfFormat(String format, String papper){
        format = format.toLowerCase();
        if(!format.startsWith("other:")){
            FormatPhoto formatPhoto = getFormatPhotoByString(format);
            return calculatePriceBySizeAndType(formatPhoto, papper);
        } else {
            Map<String, Double> stringDoubleMap = standardPricesPapper.get(format);
            if(stringDoubleMap != null){
                Double papperPrice = stringDoubleMap.get(papper);
                return papperPrice;
            }
        }
        return null;
    }



    public static Double calculatePriceBySizeAndType(FormatPhoto formatPhoto, String paper){
        try{
            paper = paper.toLowerCase();
            Map<String, Double> pricesMap = standardPricesPapper.get(a3);
            if(formatPhoto.compareTo(a3) < 1){
                pricesMap = standardPricesPapper.get(a4);
                if(formatPhoto.compareTo(a4) < 1){
                    pricesMap = standardPricesPapper.get(a5);
                    if(formatPhoto.compareTo(a5) < 1){
                        pricesMap = standardPricesPapper.get(a6);
                    }
                }
            }
            return pricesMap.get(paper);
        }catch (Exception e){
            return null;
        }
    }
    public static Integer calculatePriceBySizeAndTypeInt(FormatPhoto format, String paper){
        Double result = calculatePriceBySizeAndType(format, paper);
        if(result != null){
            return Integer.parseInt(String.valueOf(result).split("\\.")[0]);
        }
        return null;
    }
    public static boolean canPrintThisFormat(String format){
        FormatPhoto formatPhoto = getFormatPhotoByString(format);
        if(getFormatPhotoByString(format) == null) return false;
        if(formatPhoto.getHeight() < 0.5 && formatPhoto.getWidth() < 0.5){
            return false;
        }
        double heigth = Math.max(formatPhoto.getHeight(), formatPhoto.getWidth());
        double width =  Math.min(formatPhoto.getHeight(), formatPhoto.getWidth());
        if(heigth < maxHeigth || width < maxWidth){
            return false;
        }
        return true;
    }

    public static FormatPhoto getFormatPhotoByString(String format){
        if(format.equals("10x15")) return a6;
        if(format.equals("15x21")) return a5;
        if(format.equals("A4")) return a4;
        if(format.equals("A3")) return a3;
        if(format.toLowerCase().equals("polaroid")) return a6;
        try{
            format = format.replaceAll(",", ".");
            Pattern pattern = Pattern.compile("(?:(\\d+([.|,][\\d]+)?))");
            Matcher matcher = pattern.matcher(format);
            Double width = null;
            Double height = null;
            for (int i = 0; true; i++) {
                if(matcher.find()){
                    if(i == 0) width = Double.parseDouble(format.substring(matcher.start(), matcher.end()));
                    if(i == 1) {
                        height = Double.parseDouble(format.substring(matcher.start(), matcher.end()));
                        return new FormatPhoto(width, height);
                    }
                } else {
                    return null;
                }
            }
        } catch (Exception e){
            return null;
        }
    }
}
