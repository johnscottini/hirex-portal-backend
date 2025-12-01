package com.hirex.common.exception;

import java.time.Instant;

public record ApiError(
        String message,
        int status,
        Instant timestamp
) {}