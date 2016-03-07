package beans.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by paranoidq on 16/3/7.
 */
public class User {

    private static Map<Long, User> allUserMap;
    static {
        allUserMap = new HashMap<Long, User>();
    }


    private long uid;
    private List<String> hashTags;


    public User(long uid) {
        this.uid = uid;
    }

    public static Map<Long, User> getUserMap() {
        return allUserMap;
    }

    public void setHashTags(List<String> tags) {
        this.hashTags = tags;
    }

    public List<String> getHashTags() {
        return this.hashTags;
    }

}
