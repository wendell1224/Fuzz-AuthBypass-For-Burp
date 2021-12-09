package tools;

public class filterPath {
    public static String filter(String urlpath) {
        if(!urlpath.contains("?")) {
            if (urlpath.contains(".")) {
                String[] strArray = urlpath.split("/");
                String str = "";
                for (int i = 0; i < strArray.length - 1; i++) {
                    str += strArray[i] + "/";
                }
                String path = str.substring(0, str.length() - 1);
                return path;
            } else {
                String path = urlpath;
                return path;
            }
        }
        return urlpath;
    }
}
