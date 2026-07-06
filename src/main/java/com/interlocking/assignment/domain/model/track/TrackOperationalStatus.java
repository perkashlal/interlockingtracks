package com.interlocking.assignment.domain.model.track;

/** Current operational state used when deciding whether a track may be assigned. */
public enum TrackOperationalStatus {
    OPEN,
    CLOSED,
    UNDER_MAINTENANCE,
    UNKNOWN
}
