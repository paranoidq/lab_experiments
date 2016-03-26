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

    public static BufferedWriter writeFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), Constants.UTF8));
        return bw;
    }

    public static BufferedWriter writeFileAppendly(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.getParentFile().mkdirs();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path, true), Constants.UTF8));
        return bw;
    }
}
