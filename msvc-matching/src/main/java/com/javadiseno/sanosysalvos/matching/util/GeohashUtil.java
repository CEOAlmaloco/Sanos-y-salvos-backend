package com.javadiseno.sanosysalvos.matching.util;

public final class GeohashUtil {

    private GeohashUtil() {}

    private static final String BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz";
    private static final int DEFAULT_PRECISION = 5;

    public static String encode(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return "unknown";
        }
        return encode(latitude, longitude, DEFAULT_PRECISION);
    }

    public static String encode(double latitude, double longitude, int precision) {
        double[] latRange = { -90.0, 90.0 };
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
}
