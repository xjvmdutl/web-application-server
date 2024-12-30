package util;

public class UrlParseUtils {
    public static String parseUrl(String line) {
        String[] tokens = line.split(" ");
        return tokens[1];
    }

    public static String separateUrl(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

    public static String separateParam(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return "";
        }
        return url.substring(index + 1);
    }

    public static String parseMethod(String line) {
        String[] tokens = line.split(" ");
        return tokens[0];
    }
}
