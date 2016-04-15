package beans.pattern;

import beans.Edge;
import beans.trans.Item;
import beans.trans.Trans;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import util.Constants;

import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Pattern {

    // pattern id
    private int id;
    // pattern items
    private List<Integer> items;
    // pattern items (set)
    private Set<Integer> itemsSet;

    // pattern所属的类别
    private ClassType class4Pattern;

    // class支持度
    private int suppL;
    private List<Edge> coverEdges;

    // 根据OLAP计算的pattern强度
    private double strength;


    // 全局支持度
    private int suppD;
    // Dx值
    private double DxValue;


    public Pattern() {
        this.items = new LinkedList<>();
        this.itemsSet = new HashSet<>();
        coverEdges = new ArrayList<>();
    }

    /**
     * 判断pattern是否cover一个trans
     * @param trans
     * @return
     */
    public boolean cover(Trans trans) {
        // return CollectionUtils.isSubCollection(items, trans.getItemsAsList());
        return trans.getItemsAsSet().containsAll(itemsSet);
    }

    /**
     * 自增全局支持度
     */
    public void incrementSuppD() {
        this.suppD++;
    }

    public void addCoverEdge(Edge e) {
        this.coverEdges.add(e);
    }

    public List<Edge> getCoverEdges() {
        return this.coverEdges;
    }

    /**
     * 注：这里统计的方法分母直接用Cn(2)来表示所有的点对，这种方式对于文本的tag可行，但是对于属性-值对不行
     * 需要按照每个点的属性值来划分。但是由于tag是考虑直接相等，所以直接用Cn(2)表示即可
     */
    public void calStrength() {
        int cover = coverEdges.size();
        Set<Integer> nodesSet = new HashSet<>();
        for (Edge e : coverEdges) {
            nodesSet.add(e.getId1());
            nodesSet.add(e.getId2());
        }
        int nodes = nodesSet.size();
        int nodePairs = nodes * (nodes-1) / 2;
        this.strength = (double)cover / nodePairs;
    }


    public List<Integer> getItems() {
        return this.items;
    }

    public void addItem(Integer itemId) {
        this.items.add(itemId);
        this.itemsSet.add(itemId);
    }

    public void setClass4Pattern(ClassType class4Pattern) {
        this.class4Pattern = class4Pattern;
    }
    public ClassType getClass4Pattern() {
        return this.class4Pattern;
    }

    public double getDxValue() {
        return DxValue;
    }

    public void setDxValue(double dxValue) {
        DxValue = dxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Pattern)) {
            return false;
        }
        Pattern p = (Pattern)o;
        return p.id == this.id;
    }

    @Override
    public int hashCode() {
        return 19*id + 37;
    }

    public int getSuppL() {
        return this.suppL;
    }

    public void setSuppL(int supportL) {
        this.suppL = supportL;
    }

    public int getSuppD() {
        return this.suppD;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : items) {
            sb.append(Integer.toString(itemId) + StringUtils.SPACE);
        }
        sb.replace(sb.length() - 1, sb.length(), Constants.COMMA);
        sb.append("suppL=" + suppL).append(Constants.COMMA).append("suppD=" + suppD)
                .append(Constants.COMMA).append("Dx=" + DxValue)
                .append(Constants.COMMA).append("From=" + class4Pattern.toString());
        return sb.toString();
    }

    public String repr() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : items) {
            sb.append(Item.getItem(itemId).repr() + StringUtils.SPACE);
        }
//        sb.replace(sb.length() - 1, sb.length(), Constants.COMMA);
//        sb.append("suppL=" + suppL).append(Constants.COMMA).append("suppD=" + suppD)
//                .append(Constants.COMMA).append("Dx=" + DxValue)
//                .append(Constants.COMMA).append("From=" + class4Pattern.toString());
        return sb.toString();
    }
}
