package beans.trans;



import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */

public class Item {

    private static Map<Integer, Item> items = new HashMap<>();

    public static Map<Integer, Item> getItems() {
        return items;
    }

    public static Item getItem(int id) {
        return items.get(id);
    }

    public static void addItem(int itemId, String name) {
        items.put(itemId, new Item(itemId, name));
    }

    private int id;
    private String name;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Item)) {
            return false;
        }
        Item item = (Item)o;
        return item.getId() == this.id;
    }

    @Override
    public String toString() {
        if (this.id == 42) {
            System.out.println(this.name);
        }
        return "" + this.id + "," + this.name;
    }

    public String repr() {
        return this.name;
    }

    public static Map<Integer, Integer> map2NewId() {
        Map<Integer, Integer> newID2IdMap = new HashMap<>();

        List<Map.Entry<Integer, Item>> list = new ArrayList<>(items.entrySet());
        Collections.sort(list,
                (o1, o2) -> Integer.compare(o1.getKey(), o2.getKey()));

        int newID = -1;
        for (Map.Entry<Integer, Item> entry : list) {
            newID2IdMap.put(++newID, entry.getKey());
        }
        return newID2IdMap;
    }

}
