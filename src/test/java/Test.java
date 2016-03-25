import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.TextDirectoryLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by paranoidq on 16/3/12.
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
//        TextDirectoryLoader tdl = new TextDirectoryLoader();
//        tdl.setDirectory(new File("/Users/paranoidq/weka-txt"));
//        tdl.
//        Instances inss = tdl.getDataSet();
//

//        CSVLoader loader = new CSVLoader();
//        loader.setSource(new File("/Users/paranoidq/weka-txt/nolink/2"));
//        loader.setNoHeaderRowPresent(true);
//        loader.setNominalAttributes("first-last");
//        Instances data = loader.getDataSet();
//        data.setClassIndex(data.numAttributes()-1);
//
//        System.out.println(data);

//        String cmd = "/Users/paranoidq/316-data/polblogs/fpgrowth -x -tm -m2 -s90 -v\"|%a\" /Users/paranoidq/316-data/polblogs/trans " +
//                "/Users/paranoidq/316-data/polblogs/patterns";
//        Process p = Runtime.getRuntime().exec(cmd);
//        p.waitFor();

        System.out.println(System.getProperty("os.name"));
    }
}
