package com.example.inua.data

import com.google.android.gms.maps.model.LatLng

data class MapLocation(
    val latLng: LatLng,
    val title: String
)

val location1 = MapLocation(LatLng(-1.2884, 36.8233), "Nairobi CBD")
val location2 = MapLocation(LatLng(-1.319453, 36.928089), "Karen")
val location3 = MapLocation(LatLng(-1.180889, 36.972058), "Ruaka")
val location4 = MapLocation(LatLng(-1.180889, 36.972058), "Ruaka")
val location5 = MapLocation(LatLng(-1.180889, 36.972058), "Ruaka")
val location6 = MapLocation(LatLng(-1.180889, 36.972058), "Ruaka")
