package pdok.featured;

import com.cognitect.transit.ReadHandler;
import com.cognitect.transit.impl.AbstractWriteHandler;
import org.joda.time.*;

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

    public static class JodaLocalDateTimeReadHandler implements ReadHandler<LocalDateTime, Long> {

        @Override
        public LocalDateTime fromRep(Long s) {
            return new LocalDateTime(s, DateTimeZone.UTC);
        }
    }

    public static class JodaLocalDateTimeWriteHandler extends AbstractWriteHandler<LocalDateTime, Long> {

        @Override
        public String tag(LocalDateTime o) {
            return "lm";
        }

        @Override
        public Long rep(LocalDateTime o) {
            return o.toDateTime(DateTimeZone.UTC).getMillis();
        }
    }

    public static class JodaDateTimeWriteHandler extends AbstractWriteHandler<DateTime, Long> {

        @Override
        public String tag(DateTime o) {
            return "lm";
        }

        @Override
        public Long rep(DateTime o) {
            return o.toDateTime(DateTimeZone.getDefault()).withZoneRetainFields(DateTimeZone.UTC).getMillis();
        }
    }

    public static class JodaLocalDateReadHandler implements ReadHandler<LocalDate, Long> {

        @Override
        public LocalDate fromRep(Long s) {
            return new LocalDateTime(s, DateTimeZone.UTC).toLocalDate();
        }
    }

    public static class JodaLocalDateWriteHandler extends AbstractWriteHandler<LocalDate, Long> {

        @Override
        public String tag(LocalDate o) {
            return "ld";
        }

        @Override
        public Long rep(LocalDate o) {
            return o.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis();
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
