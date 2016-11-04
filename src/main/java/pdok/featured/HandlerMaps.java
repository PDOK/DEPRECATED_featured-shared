package pdok.featured;

import com.cognitect.transit.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raymond on 2-8-16.
 */
public class HandlerMaps {

    public static Map<Class, WriteHandler<?,?>> writers;
    public static Map<String, ReadHandler<?,?>> readers;

    static {
        writers = new HashMap<>();
        writers.put(NilAttribute.class, new TransitHandlers.NilAttributeWriteHandler());
        writers.put(LocalDateTime.class, new TransitHandlers.JodaLocalDateTimeWriteHandler());
        writers.put(LocalDate.class, new TransitHandlers.JodaLocalDateWriteHandler());
        writers.put(Integer.class, new TransitHandlers.IntegerWriteHandler());
        writers.put(GeometryAttribute.class, new TransitHandlers.GeometryAttributeWriteHandler());

        readers = new HashMap<>();
        readers.put("x", new TransitHandlers.NilAttributeReadHandler());
        readers.put("lm", new TransitHandlers.JodaLocalDateTimeReadHandler());
        readers.put("ld", new TransitHandlers.JodaLocalDateReadHandler());
        readers.put("I", new TransitHandlers.IntegerReadHandler());
        readers.put("ga", new TransitHandlers.GeometryAttributeReadHandler());
    }

}
