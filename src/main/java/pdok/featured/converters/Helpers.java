package pdok.featured.converters;

import com.vividsolutions.jts.geom.Coordinate;
import rdnaptrans.value.Cartesian;
import rdnaptrans.value.Geographic;

public class Helpers {

    public static Geographic toGeographic(Coordinate coord, boolean withZ) {
        if (withZ) {
            return new Geographic(coord.x, coord.y, coord.z);
        } else {
            return new Geographic(coord.x, coord.y);
        }
    }

    public static Cartesian toCartesian(Coordinate coord, boolean withZ) {
        if (withZ) {
            return new Cartesian(coord.x, coord.y, coord.z);
        } else {
            return new Cartesian(coord.x, coord.y);
        }
    }

    public static Coordinate toCoordinate(Geographic coord, boolean withZ) {
        if (withZ) {
            return new Coordinate(coord.lambda, coord.phi, coord.h);
        } else {
            return new Coordinate(coord.lambda, coord.phi);
        }
    }

    public static Coordinate toCoordinate(Cartesian coord, boolean withZ) {
        if (withZ) {
            return new Coordinate(coord.X, coord.Y, coord.Z);
        } else {
            return new Coordinate(coord.X, coord.Y);
        }
    }
}
