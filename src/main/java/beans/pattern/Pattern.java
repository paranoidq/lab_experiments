package beans.pattern;

import beans.trans.PairItem;
import weka.core.Instance;

import java.util.List;
import java.util.Set;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Pattern {

    private List<PairItem> pairItemList;
    private String patternName;
    private String patternId;

    private Set<Instance> coverSet;


    public Pattern() {

    }

}
