package pdok.featured;

import static pdok.featured.HandlerMaps.readers;
import static pdok.featured.HandlerMaps.writers;

import com.cognitect.transit.ReadHandler;
import com.cognitect.transit.Reader;
import com.cognitect.transit.TransitFactory;
import com.cognitect.transit.WriteHandler;
import com.cognitect.transit.Writer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Serializer {

    public static <T> String toJson(T obj) {
        return toJson(obj, writers);
    }

    public static <T> String toJson(T obj, Map<Class, WriteHandler<?, ?>> handlers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Writer<T> writer = TransitFactory.writer(TransitFactory.Format.JSON, out, handlers);
        writer.write(obj);
        return out.toString();
    }

    public static Object fromJson(String json) {
        return fromJson(json, readers);
    }

    public static Object fromJson(String json, Map<String, ReadHandler<?, ?>> handlers) {
        ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
        Reader reader = TransitFactory.reader(TransitFactory.Format.JSON, in, handlers);
        return reader.read();
    }
}
