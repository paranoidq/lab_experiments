package beans.trans;

import util.Constants;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paranoidq on 16/3/7.
 */
@Deprecated
public class Tag {

    private static Map<Integer, Tag> tags = new HashMap<>();
    static {
        try {
            TagHandler.loadTags();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Tag> getTags() {
        return tags;
    }

    private static int INCREMENT_ID = 0;

    private int id;
    private String tagName;


    public Tag(String tagName) {
        this.tagName = tagName;
        this.id = ++INCREMENT_ID;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.tagName;
    }



    /**
     *
     * TODO:
     *      改进:用序列化的方式保存tags
     */
    private static class TagHandler {

        /**
         * 由python代码完成
         * tags_path文件格式:
         *      tag_name,tag_occur_count
         */
        private static void genTags() {}

        public static void loadTags() throws IOException {
            if (tags.isEmpty()) {
                initTags();
            }
        }
        private static void initTags() throws IOException {
            String tags_path = Constants._tags_src_path;
            BufferedReader br = FileUtil.readFile(tags_path);
            String line;
            while ( (line = br.readLine()) != null) {
                int i = line.lastIndexOf(Constants.COMMA);
                String tagName = line.substring(0, i);
                Tag tag = new Tag(tagName);
                tags.put(tag.getId(), tag);
            }
        }
    }
}
