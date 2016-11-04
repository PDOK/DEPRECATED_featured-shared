package pdok.featured;

import java.util.*;

/**
 * Created by stroej on 23-9-2016.
 */
public class GeometryAttribute {

    private String type;
    private Object geometry;
    private Integer srid;
    private Collection<Integer> tiles;

    public GeometryAttribute(String type, Object geometry) {
        this(type, geometry, null);
    }

    public GeometryAttribute(String type, Object geometry, Integer srid) {
        this.type = type;
        this.geometry = geometry;
        this.srid = srid;
    }

    public String getType() {return type;
    }

    public Object getGeometry() {
        return geometry;
    }

    public Integer getSrid() { return srid;
    }

    public Collection<Integer> getTiles(){
        return tiles;
    }

    // tiles is will be computed outside of this class, so a set-method is necessary
    public void setTiles (final Collection<Integer> tiles){
        this.tiles = Collections.unmodifiableCollection(tiles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeometryAttribute that = (GeometryAttribute) o;

        if (!getType().equals(that.getType())) return false;
        if (!getGeometry().equals(that.getGeometry())) return false;
        return getSrid() != null ? getSrid().equals(that.getSrid()) : that.getSrid() == null;

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getGeometry().hashCode();
        result = 31 * result + (getSrid() != null ? getSrid().hashCode() : 0);
        return result;
    }
}
