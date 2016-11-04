package pdok.featured;

import com.cognitect.transit.ArrayReader;
import com.cognitect.transit.ReadHandler;
import com.cognitect.transit.impl.AbstractWriteHandler;
import com.cognitect.transit.ArrayReadHandler;
import com.cognitect.transit.TransitFactory;
import org.joda.time.*;

import java.util.*;

/**
 * Created by raymond on 2-8-16.
 */
public class TransitHandlers {

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
        public GeometryAttribute fromRep(List<Object> list){
            return new GeometryAttribute((String) list.get(0), list.get(1), (Integer) list.get(2));
        }
    }

    public static class GeometryAttributeWriteHandler extends AbstractWriteHandler<GeometryAttribute, Object>{

        @Override
        public String tag(GeometryAttribute geometryAttribute) { return "ga"; }

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
            Chronology UTC = DateTimeUtils.getChronology(null).withUTC();
            return new LocalDateTime(s, UTC);
        }
    }

    public static class JodaLocalDateTimeWriteHandler extends AbstractWriteHandler<LocalDateTime, Long> {

        @Override
        public String tag(LocalDateTime o) {
            return "lm";
        }

        @Override
        public Long rep(LocalDateTime o) {
            return (new Date(o.toDateTime(DateTimeZone.UTC).getMillis())).getTime();
        }
    }


    public static class JodaLocalDateReadHandler implements ReadHandler<LocalDate, Long> {

        @Override
        public LocalDate fromRep(Long s) {
            return new DateTime(s, DateTimeZone.getDefault()).toLocalDate();
        }
    }

    public static class JodaLocalDateWriteHandler extends AbstractWriteHandler<LocalDate, Long> {

        @Override
        public String tag(LocalDate o) {
            return "ld";
        }

        @Override
        public Long rep(LocalDate o) {
            return (new Date(o.toDateTime(LocalTime.MIDNIGHT).getMillis())).getTime();
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
