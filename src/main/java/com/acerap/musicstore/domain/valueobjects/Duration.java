package com.acerap.musicstore.domain.valueobjects;

public record Duration(int minutes, int seconds) {
    public Duration {
        if (minutes < 0 || seconds < 0 || seconds >= 60) {
            throw new IllegalArgumentException("Invalid duration");
        }
    }

    public String formatted() {
        return String.format("%d:%02d", minutes, seconds);
    }
}
