package pdok.featured;

import org.junit.Assert;
import org.junit.Test;
import pdok.featured.tiles.TilesHelper;

import java.util.Set;

/**
 * Created by raymond on 2-8-16.
 */
public class TilesHelperTest {

    @Test
    public void testTiles() {

        GeometryAttribute ga = new GeometryAttribute("gml", "<gml:Point xmlns:gml=\"http://www.opengis.net/gml/3.2\" srsName=\"urn:ogc:def:crs:EPSG::28992\" gml:id=\"LOCAL_ID_1\"><gml:pos>131411.071 481481.517</gml:pos></gml:Point>");
        Set<Integer> tiles = (Set <Integer>) TilesHelper.nl(ga);
        Assert.assertEquals(1, tiles.size());
        Integer[] tilesArray = tiles.toArray(new Integer[0]);
        Assert.assertEquals((Integer) 38309, tilesArray[0]);
    }

}
