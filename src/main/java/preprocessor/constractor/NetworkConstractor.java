package preprocessor.constractor;

import beans.graph.Network;

import java.io.*;

/**
 * Created by paranoidq on 16/3/7.
 */
public class NetworkConstractor {

    public NetworkConstractor() {

    }

    public static Network constract(String srcPath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcPath), "UTF-8"));

        String line;
        while ( (line = br.readLine()) != null) {

        }


        return null;
    }

}
