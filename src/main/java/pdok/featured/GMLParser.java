package pdok.featured;

import com.vividsolutions.jts.geom.Geometry;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.deegree.cs.CRSCodeType;
import org.deegree.cs.coordinatesystems.ICRS;
import org.deegree.geometry.standard.AbstractDefaultGeometry;
import org.deegree.gml.GMLInputFactory;
import org.deegree.gml.GMLStreamReader;
import org.deegree.gml.GMLVersion;

public final class GMLParser {

    /**
     * Default SRID (28992: Amersfoort RD / New)
     */
    private static final int DEFAULT_SRID = 28992;

    private GMLParser() {
    }

    public static Geometry parse(String gml) throws Exception {
        try {
            // Reintroduce quirk from previous GML parser to support BGT. Remove as soon as possible.
            gml = gml.replace("srsName='urn:ogc:def:crs:EPSG:28992'", "srsName='urn:ogc:def:crs:EPSG::28992'")
                    .replace("srsName=\"urn:ogc:def:crs:EPSG:28992\"", "srsName=\"urn:ogc:def:crs:EPSG::28992\"");

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(new StringReader(gml));
            xmlStreamReader.nextTag();

            GMLVersion gmlVersion;
            if ("http://www.opengis.net/gml/3.2".equals(xmlStreamReader.getNamespaceURI())) {
                gmlVersion = GMLVersion.GML_32;
            } else {
                gmlVersion = GMLVersion.GML_31;
            }

            GMLStreamReader gmlStreamReader = GMLInputFactory.createGMLStreamReader(gmlVersion, xmlStreamReader);

            AbstractDefaultGeometry deegreeGeometry = (AbstractDefaultGeometry) gmlStreamReader.readGeometry();
            Geometry jtsGeometry = deegreeGeometry.getJTSGeometry();

            ICRS cs = deegreeGeometry.getCoordinateSystem();
            if (cs == null) {
                jtsGeometry.setSRID(DEFAULT_SRID);
            } else {
                for (CRSCodeType code : cs.getCodes()) {
                    if ("epsg".equals(code.getCodeSpace())) {
                        jtsGeometry.setSRID(Integer.parseInt(code.getCode()));
                        break;
                    }
                }
            }

            return jtsGeometry;
        } catch (Exception e) {
            throw new GMLParserException("Couldn't parse GML", e);
        }
    }
}
