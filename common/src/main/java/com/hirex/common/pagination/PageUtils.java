package com.hirex.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Utility helpers for building Spring Data {@link PageRequest} and {@link Sort} instances
 * in a consistent way across modules.
 */
public final class PageUtils {

    private PageUtils() {}

    /**
     * Builds a {@link PageRequest} using the provided page index, size and sort field.
     * If {@code sortField} is blank, an unsorted page request is returned.
     */
    public static PageRequest page(Integer pageIndex, Integer pageSize, String sortField) {
        return PageRequest.of(pageIndex, pageSize, sort(sortField));
    }

    /**
     * Builds a {@link Sort} using ASC direction for the provided sort field.
     * If {@code sortField} is blank or null, returns {@link Sort#unsorted()}.
     */
    public static Sort sort(String sortField) {
        if (isBlank(sortField)) {
            return Sort.unsorted();
        }
        return Sort.by(Sort.Direction.ASC, sortField);
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
