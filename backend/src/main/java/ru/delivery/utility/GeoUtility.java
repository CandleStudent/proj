package ru.delivery.utility;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;

@UtilityClass
public class GeoUtility {

  /**
   * Расчет дистанции между двумя точками на сфероиде
   * @param lat1 долгота первой точки
   * @param lon1 широта первой точки
   * @param lat2 долгота второй точки
   * @param lon2 широта второй точки
   * @return расстояние между точками, в метрах
   */
  public static Double getDistanceBetweenTwoPoints(
      double lat1, double lon1, double lat2, double lon2) {

    GeodesicData result = Geodesic.WGS84.Inverse(lat1, lon1, lat2, lon2);
    return result.s12;
  }

  /**
   * Расчет дистанции между двумя точками на сфероиде
   * @param lat1 долгота первой точки
   * @param lon1 широта первой точки
   * @param lat2 долгота второй точки
   * @param lon2 широта второй точки
   * @return расстояние между точками, в метрах
   */
  public static Double getDistanceBetweenTwoPoints(
      BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {

    return getDistanceBetweenTwoPoints(
        lat1.doubleValue(),
        lon1.doubleValue(),
        lat2.doubleValue(),
        lon2.doubleValue());
  }

}
