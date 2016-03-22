package beans.pattern;

import beans.trans.Item;
import weka.core.Instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Pattern {

    private static Map<Integer, Pattern> patternMap = new HashMap<>();


    private List<Item> entryList;
    private String patternName;
    private String patternId; // TODO: 如何确定??

    private Set<Instance> coverSet;


    public Pattern() {

    }

    public String getPatternId() {
        // TODO
        return null;
    }

    public void addEntry(Item pItem) {
        this.entryList.add(pItem);
    }

    public List<Item> getEntryList() {
        return this.entryList;
    }

    public boolean fit(Instance instance) {
        //TODO
        return false;
    }

    public Set<Instance> getCoverSet() {
        return coverSet;
    }

    public void setCoverSet(Set<Instance> coverSet) {
        this.coverSet = coverSet;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Pattern)) {
            return false;
        }
        Pattern p = (Pattern)o;
        return p.getPatternId().equals(this.patternId);
    }

    @Override
    public int hashCode() {
        return this.patternId.hashCode();
    }
}
