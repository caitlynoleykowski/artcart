package com.coco0o0o.artcart.feature.saved.impl

import android.net.Uri
import kotlin.math.*

data class LatLngPoint(
    val lat: Double,
    val lng: Double
)

fun buildOptimizedGoogleMapsRouteUrl(points: List<LatLngPoint>): String {
    require(points.size >= 2) { "Need at least 2 points" }

    val bestRoute = points.indices
        .map { startIndex -> buildNearestNeighborRoute(points, startIndex) }
        .minBy { route -> totalRouteDistance(route) }

    val origin = bestRoute.first().toMapsParam()
    val destination = bestRoute.last().toMapsParam()
    val waypoints = bestRoute.drop(1).dropLast(1)

    return Uri.Builder()
        .scheme("https")
        .authority("www.google.com")
        .path("maps/dir/")
        .appendQueryParameter("api", "1")
        .appendQueryParameter("travelmode", "driving")
        .appendQueryParameter("origin", origin)
        .appendQueryParameter("destination", destination)
        .apply {
            if (waypoints.isNotEmpty()) {
                appendQueryParameter(
                    "waypoints",
                    waypoints.joinToString("|") { it.toMapsParam() }
                )
            }
        }
        .build()
        .toString()
}

private fun buildNearestNeighborRoute(
    points: List<LatLngPoint>,
    startIndex: Int
): List<LatLngPoint> {
    val remaining = points.toMutableList()
    val route = mutableListOf<LatLngPoint>()

    var current = remaining.removeAt(startIndex)
    route += current

    while (remaining.isNotEmpty()) {
        val next = remaining.minBy { haversineMiles(current, it) }
        remaining.remove(next)
        route += next
        current = next
    }

    return route
}

private fun totalRouteDistance(route: List<LatLngPoint>): Double =
    route.zipWithNext().sumOf { (a, b) -> haversineMiles(a, b) }

private fun haversineMiles(a: LatLngPoint, b: LatLngPoint): Double {
    val earthRadiusMiles = 3958.8

    val dLat = Math.toRadians(b.lat - a.lat)
    val dLng = Math.toRadians(b.lng - a.lng)

    val lat1 = Math.toRadians(a.lat)
    val lat2 = Math.toRadians(b.lat)

    val h =
        sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) * sin(dLng / 2).pow(2)

    return 2 * earthRadiusMiles * asin(sqrt(h))
}

private fun LatLngPoint.toMapsParam(): String =
    "$lat,$lng"