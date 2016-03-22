package handler;

import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.trans.Item;
import beans.trans.WrappedInstances;
import util.Constants;
import util.FileUtil;
import util.ParamConstants;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by paranoidq on 16/3/12.
 */
public class TransHandler {

    public static class Generator {
        private static Map<Integer, Item> items;
        static {
            items = Item.getItems();
        }

        public static void generateFPTrans(Instances train, int fold) {

            return;
        }

        public static void generatePUTrans(Instances train, int fold, ClassType ct) {
            return;
        }

        public static WrappedInstances generateAugTrans(Iterator<? extends Pattern> iterator, WrappedInstances instances) {
            //TODO
            return null;
        }

        public static WrappedInstances generateAugTrans4SIN(Iterator<Item> iterator, WrappedInstances instances) {
            // TODO
            return null;
        }

        public static void generateDataTrans() throws IOException {
            BufferedReader br = FileUtil.readFile(Constants._data_trans_path);
        }
    }


    public static class Loader {

        public static WrappedInstances loadSrc(String transPath) throws IOException {
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(transPath));
            loader.setNoHeaderRowPresent(true);
            loader.setNominalAttributes("first-last");
            Instances data = loader.getDataSet();
            data.setClassIndex(data.numAttributes()-1);

            return new WrappedInstances(data, ParamConstants.NUM_FOLDS);
        }
    }
}
