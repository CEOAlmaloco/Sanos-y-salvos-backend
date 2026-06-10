package com.javadiseno.sanosysalvos.analytics.config;

// SY-72 Utilidad para calcular geohash a partir de coordenadas
public final class GeohashUtil {

    private GeohashUtil() {}

    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static final int DEFAULT_PRECISION = 5;

    public static String encode(Double latitude, Double longitude) {
        return encode(latitude, longitude, DEFAULT_PRECISION);
    }

    /**
     * Calcula el geohash de una coordenada con precisión especifica.
     */
    public static String encode(double latitude, double longitude, int precision) {
        double[] latRange = { -90.0,  90.0 };
        double[] lonRange = { -180.0, 180.0 };

        StringBuilder geohash = new StringBuilder();
        boolean isLon = true;
        int bit = 0;
        int charIndex = 0;

        while (geohash.length() < precision) {
            if (isLon) {
                double mid = (lonRange[0] + lonRange[1]) / 2;
                if (longitude >= mid) {
                    charIndex = (charIndex << 1) | 1;
                    lonRange[0] = mid;
                } else {
                    charIndex = charIndex << 1;
                    lonRange[1] = mid;
                }
            } else {
                double mid = (latRange[0] + latRange[1]) / 2;
                if (latitude >= mid) {
                    charIndex = (charIndex << 1) | 1;
                    latRange[0] = mid;
                } else {
                    charIndex = charIndex << 1;
                    latRange[1] = mid;
                }
            }

            isLon = !isLon;
            if (++bit == 5) {
                geohash.append(BASE32.charAt(charIndex));
                bit = 0;
                charIndex = 0;
            }
        }

        return geohash.toString();
    }

    /**
     * Decodifica un geohash y devuelve el centro del área como [lat, lon].
     */
    public static double[] decode(String geohash) {
        double[] latRange = { -90.0,  90.0 };
        double[] lonRange = { -180.0, 180.0 };
        boolean isLon = true;

        for (char c : geohash.toCharArray()) {
            int charIndex = BASE32.indexOf(c);
            for (int bits = 4; bits >= 0; bits--) {
                int bit = (charIndex >> bits) & 1;
                if (isLon) {
                    double mid = (lonRange[0] + lonRange[1]) / 2;
                    if (bit == 1) lonRange[0] = mid;
                    else lonRange[1] = mid;
                } else {
                    double mid = (latRange[0] + latRange[1]) / 2;
                    if (bit == 1) latRange[0] = mid;
                    else latRange[1] = mid;
                }
                isLon = !isLon;
            }
        }

        double lat = (latRange[0] + latRange[1]) / 2;
        double lon = (lonRange[0] + lonRange[1]) / 2;
        return new double[]{ lat, lon };
    }
}
