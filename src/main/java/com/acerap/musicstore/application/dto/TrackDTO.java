package com.acerap.musicstore.application.dto;

import com.acerap.musicstore.domain.model.Track;

public record TrackDTO(
        int position,
        String title,
        String duration
) {
    public static TrackDTO fromDomain(Track track) {
        return new TrackDTO(
                track.getPosition(),
                track.getTitle(),
                track.getDuration().formatted()
        );
    }
}
