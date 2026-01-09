package com.pocketpipo.pocketpipo.dto;

import java.time.Instant;

    public record ApiErrorDTO(
        String message,
        int status,
        String path,
        Instant timestamp
) {}
