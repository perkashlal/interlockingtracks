package com.interlocking.assignment.domain.rule.track;

import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;

/** Allows automatic assignment only when a track is explicitly open. */
public final class TrackOperationalStatusRule {

    public boolean isAssignable(TrackOperationalStatus status) {
        return status == TrackOperationalStatus.OPEN;
    }
}
