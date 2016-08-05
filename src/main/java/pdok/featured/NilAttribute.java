package pdok.featured;

/**
 * Created by raymond on 2-8-16.
 */
public class NilAttribute {

    public final Class clazz;

    public NilAttribute(Class clazz) {
        this.clazz = clazz;
    }

    public static NilAttribute fromString(String className) throws ClassNotFoundException {
        return new NilAttribute(Class.forName(className));
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NilAttribute that = (NilAttribute) o;

        return clazz != null ? clazz.equals(that.clazz) : that.clazz == null;
    }

    @Override
    public int hashCode() {
        return clazz != null ? clazz.hashCode() : 0;
    }
}
