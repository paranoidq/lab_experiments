package beans.trans;

import beans.pattern.ClassType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */

public class Trans {

    private List<Integer> items;
    private ClassType ct;


    public Trans(ClassType ct) {
        this.ct = ct;
        this.items = new LinkedList<>();
    }

    public Trans copy() {
        Trans trans = new Trans(this.ct);
        this.items.forEach(trans::addItemId);
        return trans;
    }


    public void addItemId(int itemId){
        this.items.add(itemId);
    }

    public List<Integer> getItems(){
        return this.items;
    }

    public ClassType getCt() {
        return this.ct;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : items) {
            sb.append(itemId).append(",");
        }
//        if(!items.isEmpty()) {
//            sb.replace(sb.length()-1, sb.length(), "");
//        }
        sb.replace(sb.length()-1, sb.length(), "");
        return sb.toString();
    }


    /**
     * 作为list的实现,这种操作很低效
     * TODO
     * @param itemId
     * @return
     */
    public boolean contains(Integer itemId) {
        return items.contains(itemId);
    }

}
