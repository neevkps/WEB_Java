package com.wearetrying.space_cats_market.service.exception;

public record ErrorResponse(
        String type,
        String title,
        int status,
        String detail,
        String path
) {}