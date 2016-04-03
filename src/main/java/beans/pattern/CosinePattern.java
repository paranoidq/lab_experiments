package beans.pattern;

import beans.trans.Item;
import org.apache.commons.lang3.StringUtils;
import util.Constants;

/**
 * Created by paranoidq on 16/3/27.
 */
public class CosinePattern extends Pattern {

    private double cosine;

    public void setCosine(double cosine) {
        this.cosine = cosine;
    }

    public double getCosine() {
        return this.cosine;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : super.getItems()) {
            sb.append(Integer.toString(itemId) + StringUtils.SPACE);
        }
        sb.replace(sb.length() - 1, sb.length(), Constants.COMMA);
        sb.append("suppL=" + getSuppL()).append(Constants.COMMA).append("suppD=" + getSuppD())
                .append(Constants.COMMA).append("Cosine=" + this.cosine)
                .append(Constants.COMMA).append("Dx=" + getDxValue())
                .append(Constants.COMMA).append("From=" + getClass4Pattern().toString());
        return sb.toString();
    }

    @Override
    public String repr() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : getItems()) {
            sb.append(Item.getItem(itemId).repr() + StringUtils.SPACE);
        }
//        sb.replace(sb.length() - 1, sb.length(), Constants.COMMA);
//        sb.append("suppL=" + getSuppL()).append(Constants.COMMA).append("suppD=" + getSuppD())
//                .append(Constants.COMMA).append("Cosine=" + this.cosine)
//                .append(Constants.COMMA).append("Dx=" + getDxValue())
//                .append(Constants.COMMA).append("From=" + getClass4Pattern().toString());
        return sb.toString();
    }
}
