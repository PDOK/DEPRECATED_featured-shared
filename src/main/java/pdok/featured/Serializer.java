package pdok.featured;

import com.cognitect.transit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static pdok.featured.HandlerMaps.readers;
import static pdok.featured.HandlerMaps.writers;

/**
 * Created by raymond on 4-8-16.
 */
public class Serializer {
    public static <T> String toJson(T obj) {
        return toJson(obj, writers);
    }

    public static <T> String toJson(T obj, Map<Class, WriteHandler<?,?>> handlers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer<T> writer = TransitFactory.writer(TransitFactory.Format.JSON, out, handlers);
        writer.write(obj);
        return out.toString();
    }

    public static Object fromJson(String json) {
        return fromJson(json, readers);
    }

    public static Object fromJson(String json, Map<String, ReadHandler<?,?>> handlers) {
        ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
        Reader reader = TransitFactory.reader(TransitFactory.Format.JSON, in, handlers);
        return reader.read();
    }
}
