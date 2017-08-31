package pdok.featured;

import com.cognitect.transit.ReadHandler;
import com.cognitect.transit.impl.AbstractWriteHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class TransitHandlers {

    /* This determines how LocalDate(Time)s are interpreted. The milliseconds in the database are always in UTC, i.e.
     * LOCAL_TIME_ZONE = UTC -> LocalDateTime 1970-01-01 00:00 = timestamp 0
     * LOCAL_TIME_ZONE = Europe/Amsterdam -> LocalDateTime 1970-01-01 01:00 = timestamp 0 */
    private static final DateTimeZone LOCAL_TIME_ZONE = DateTimeZone.forID("Europe/Amsterdam");

    public static class NilAttributeReadHandler implements ReadHandler<NilAttribute, String> {

        @Override
        public NilAttribute fromRep(String s) {
            try {
                return NilAttribute.fromString(s);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class NilAttributeWriteHandler extends AbstractWriteHandler<NilAttribute, String> {

        @Override
        public String tag(NilAttribute nilAttribute) {
            return "x";
        }

        @Override
        public String rep(NilAttribute nilAttribute) {
            return nilAttribute.clazz.getName();
        }
    }

    public static class GeometryAttributeReadHandler implements ReadHandler<GeometryAttribute, List<Object>> {

        @Override
        public GeometryAttribute fromRep(List<Object> list) {
            String type = (String) list.get(0);
            Object geometry = list.get(1);
            Integer srid = (Integer) list.get(2);
            Set<Integer> tiles = (Set<Integer>) list.get(3);
            GeometryAttribute ga = new GeometryAttribute(type, geometry, srid);
            ga.setTiles(tiles);
            return ga;
        }
    }

    public static class GeometryAttributeWriteHandler extends AbstractWriteHandler<GeometryAttribute, Object> {

        @Override
        public String tag(GeometryAttribute geometryAttribute) {
            return "ga";
        }

        @Override
        public List<Object> rep(GeometryAttribute geometryAttribute) {
            ArrayList<Object> ga = new ArrayList<>();
            ga.add(geometryAttribute.getType());
            ga.add(geometryAttribute.getGeometry());
            ga.add(geometryAttribute.getSrid());
            ga.add(geometryAttribute.getTiles());
            return ga;
        }
    }

    public static class JodaLocalDateTimeReadHandler implements ReadHandler<LocalDateTime, Long> {

        @Override
        public LocalDateTime fromRep(Long s) {
            return new LocalDateTime(s, LOCAL_TIME_ZONE);
        }
    }

    public static class JodaLocalDateTimeWriteHandler extends AbstractWriteHandler<LocalDateTime, Long> {

        @Override
        public String tag(LocalDateTime o) {
            return "lm";
        }

        @Override
        public Long rep(LocalDateTime o) {
            return o.toDateTime(LOCAL_TIME_ZONE).getMillis();
        }
    }

    public static class JodaLocalDateReadHandler implements ReadHandler<LocalDate, Long> {

        @Override
        public LocalDate fromRep(Long s) {
            return new LocalDateTime(s, LOCAL_TIME_ZONE).toLocalDate();
        }
    }

    public static class JodaLocalDateWriteHandler extends AbstractWriteHandler<LocalDate, Long> {

        @Override
        public String tag(LocalDate o) {
            return "ld";
        }

        @Override
        public Long rep(LocalDate o) {
            return o.toDateTimeAtStartOfDay(LOCAL_TIME_ZONE).getMillis();
        }
    }

    public static class IntegerReadHandler implements ReadHandler<Integer, String> {

        @Override
        public Integer fromRep(String s) {
            return new Integer(s);
        }
    }

    public static class IntegerWriteHandler extends AbstractWriteHandler<Integer, String> {

        @Override
        public String tag(Integer o) {
            return "I";
        }

        @Override
        public String rep(Integer o) {
            return o.toString();
        }
    }
}
