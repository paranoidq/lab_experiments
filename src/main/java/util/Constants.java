package util;

import java.io.File;

/**
 * Created by paranoidq on 16/3/7.
 */
public interface Constants {

    String EQUAL = "=";
    String TAB = "\t";
    String NEWLINE = "\n";

    String TAG = "tag";


    // path
    String _prefix = "";
    String _item_prefix = ".item";
    String _pair_item_prefix = ".pitem";
    String _trans_prefix = ".trans";

    String _src = _prefix + File.separator + "sina.txt";
    String _item = _prefix + File.separator + "item" ;


}
