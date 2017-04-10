package pdok.featured;

public class GeometryAttribute {

    private String type;
    private Object geometry;
    private Integer srid;

    public GeometryAttribute(String type, Object geometry) {
        this(type, geometry, null);
    }

    public GeometryAttribute(String type, Object geometry, Integer srid) {
        this.type = type;
        this.geometry = geometry;
        this.srid = srid;
    }

    public String getType() {
        return type;
    }

    public Object getGeometry() {
        return geometry;
    }

    public Integer getSrid() {
        return srid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeometryAttribute that = (GeometryAttribute) o;

        if (!getType().equals(that.getType())) {
            return false;
        }
        if (!(getGeometry() == null && that.getGeometry() == null) && !getGeometry().equals(that.getGeometry())) {
            return false;
        }
        
        return getSrid() != null ? getSrid().equals(that.getSrid()) : that.getSrid() == null;
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        if (getGeometry() != null) {
            result = 31 * result + getGeometry().hashCode();
        }

        result = 31 * result + (getSrid() != null ? getSrid().hashCode() : 0);
        return result;
    }
}
