package handler;

import beans.trans.Trans;
import beans.trans.TransSet;
import util.Constants;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by paranoidq on 16/3/23.
 */
public class InstancesHandler {


    public static Instances loadInstances(String path) {
        try {
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File(path));
            Instances instances = loader.getDataSet();
            instances.setClassIndex(instances.numAttributes()-1);
            return instances;
        } catch (IOException e) {
            return null;
        }

    }

    public static void writeOriginInstances(TransSet set, int itemCount, Map<Integer, Integer> newID2id, BufferedWriter bw) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("@relation polblogs");
        sb.append(Constants.NEWLINE);
        for (int i = 0; i < itemCount; i++) {
            sb.append("@attribute item" + i + " {0, 1}");
            sb.append(Constants.NEWLINE);
        }
        sb.append("@attribute class {POSITIVE, NEGATIVE}");
        sb.append(Constants.NEWLINE);
        sb.append("@data");
        sb.append(Constants.NEWLINE);
        bw.write(sb.toString());
        for (Trans trans : set.getTransSet()) {
            sb = new StringBuilder();
            for (int i = 0; i < itemCount; i++) {
                if (trans.contains(newID2id.get(i))) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            sb.append(trans.getCt().toString());
            sb.append(Constants.NEWLINE);
            bw.write(sb.toString());
        }
        bw.flush();
    }


}
