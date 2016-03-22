package beans.pattern;

/**
 * Created by paranoidq on 16/3/7.
 */
public enum ClassType {

    POSITIVE(1.0),
    NEGATIVE(0.0);

    //0.0 = NEGATIVE 1.0 = POSITIVE
    double cValue;

    ClassType(double cValue) {
        this.cValue = cValue;
    }


    @Override
    public String toString() {
        return name();
    }

    public static ClassType get(double cValue) {
        if (cValue == 1.0) {
            return POSITIVE;
        } else {
            return NEGATIVE;
        }
    }
}
