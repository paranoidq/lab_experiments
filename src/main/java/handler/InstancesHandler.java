package handler;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by paranoidq on 16/3/23.
 */
public class InstancesHandler {

    static CSVLoader loader = new CSVLoader();
    static {
        loader.setNoHeaderRowPresent(true);
        loader.setNominalAttributes("first-last");
    }

    // TODO: randomize没有做
    public static Instances loadInstances(String path) throws IOException {
        loader.setSource(new File(path));
        Instances data = loader.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
    }
}
