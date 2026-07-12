package com.interlocking.assignment.adapter.web;

import java.util.List;

public record AssignmentResponse(
        String outcome,
        String assignedTrackId,
        List<String> reasons) {
}
