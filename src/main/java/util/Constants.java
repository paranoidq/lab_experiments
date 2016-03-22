package util;

import java.io.File;

/**
 * Created by paranoidq on 16/3/7.
 */
public interface Constants {

    String UTF8 = "UTF-8";

    String EQUAL = "=";
    String TAB = "\t";
    String NEWLINE = "\n";
    String UNDERLINE = "_";
    String COMMA = ",";

    String TAG = "tag";

    String PATTERN_FEQ_SPLIT = "\\|";
    String PATTERN_ENTRY_SPLIT = "\\s+";


    // path
    String _prefix = "";
    String _item_postfix = ".item";
    String _pat_postfix = ".pat";
    String _trans_postfix = ".trans";



    String _fp_trans4fold = _prefix + File.separator + "fp" + File.separator + "fold_";
    String _fp_pattern4fold = _prefix + File.separator + "fp" + File.separator + "fold_";

    String _pu_trans4fold_4c = _prefix + File.separator + "fp" + File.separator + "fold_";
    String _pu_pattern4fold_4c = _prefix + File.separator + "pu" + File.separator + "fold_";


    String _item_path = _prefix + File.separator + "words";
    String _data_trans_path = _prefix + "";
}
