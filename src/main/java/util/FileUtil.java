package util;

import java.io.*;

/**
 * Created by paranoidq on 16/3/12.
 */
public class FileUtil {


    public static BufferedReader readFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(path), Constants.UTF8));
        return br;
    }
}