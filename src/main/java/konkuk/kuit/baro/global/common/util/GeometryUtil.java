package konkuk.kuit.baro.global.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point createPoint(Double latitude, Double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
    public static Boolean validateLocation(Double latitude, Double longitude) {
        if (latitude > 90 || latitude < -90) {
            return false;
        }

        if (longitude > 180 || longitude < -180) {
            return false;
        }

        return true;
    }
}
