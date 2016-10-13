package pdok.featured;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by raymond on 2-8-16.
 */
public class TransitHandlersTest {

    @Test
    public void LocalDateTimeTest() {
        LocalDateTime ldt = LocalDateTime.now();
        String serial = Serializer.toJson(ldt);

        LocalDateTime deserial = (LocalDateTime) Serializer.fromJson(serial);

        Assert.assertEquals(ldt, deserial);
    }

    //TODO enable test
//    @Test
//    public void LocalDateTimeZeroTest() {
//        LocalDateTime ldt = new LocalDateTime(1970,1,1,0,0,0);
//        String serial = "[\"~#lm\",0]";
//
//        LocalDateTime deserial = (LocalDateTime) Serializer.fromJson(serial);
//
//        Assert.assertEquals(ldt, deserial);
//    }

    @Test
    public void LocalDateTest() {
        LocalDate ld = LocalDate.now();
        String serial = Serializer.toJson(ld);

        LocalDate deserial = (LocalDate) Serializer.fromJson(serial);

        Assert.assertEquals(ld, deserial);
    }

    @Test
    public void IntegerTest() {
        Integer i = 1337;
        String serial = Serializer.toJson(i);

        Integer deserial = (Integer) Serializer.fromJson(serial);

        Assert.assertEquals(i, deserial);
    }

    @Test
    public void NilAttributeTest() throws ClassNotFoundException {
        NilAttribute na = NilAttribute.fromString("java.lang.String");
        String serial = Serializer.toJson(na);

        NilAttribute deserial = (NilAttribute) Serializer.fromJson(serial);
        Assert.assertEquals(na, deserial);
    }

}
