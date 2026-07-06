package com.interlocking.assignment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArchitectureSmokeTest {

    @Test
    void applicationClassShouldBeAvailable() {
        assertThat(AutomaticTrainAssignmentApplication.class).isNotNull();
    }
}

