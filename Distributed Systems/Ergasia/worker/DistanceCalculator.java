package worker;

public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    // Calculate distance between two points in latitude and longitude taking
    // into account height difference. If you are not interested in height
    // difference pass 0.0. Uses Haversine method as its base.
    // lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
    // el2 End altitude in meters
    // returns Distance in Kilometers
    public static double calculateDistance(double lat1, double lon1,
                                           double lat2, double lon2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c; // convert to kilometers

        // We don't consider elevation for this assignment.
        // double height = el1 - el2;
        // distance = Math.pow(distance, 2) + Math.pow(height, 2);
        // return Math.sqrt(distance);

        return distance;
    }
}