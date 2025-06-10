package ru.delivery.distribution;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.delivery.utility.GeoUtility;

@Data
@RequiredArgsConstructor
public class GeoPoint {

  private final double lat;
  private final double lon;

  public static GeoPoint centroid(List<GeoPoint> points) {
    double x = 0, y = 0, z = 0;
    for (GeoPoint point : points) {
      double latitude = Math.toRadians(point.lat);
      double longitude = Math.toRadians(point.lon);
      x += Math.cos(latitude) * Math.cos(longitude);
      y += Math.cos(latitude) * Math.sin(longitude);
      z += Math.sin(latitude);
    }
    int total = points.size();
    x /= total;
    y /= total;
    z /= total;

    double hyp = Math.sqrt(x * x + y * y);
    double lat = Math.toDegrees(Math.atan2(z, hyp));
    double lon = Math.toDegrees(Math.atan2(y, x));

    return new GeoPoint(lat, lon);
  }

  public double distanceTo(GeoPoint other) {
    return GeoUtility.getDistanceBetweenTwoPoints(this.lat, this.lon, other.lat, other.lon);
  }

}
