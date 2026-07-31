package com.ecommerce.backend.common;

import java.util.List;

public class PageResponse<T> {

    private final List<T> content;

    private final int currentPage;

    private final int pageSize;

    private final long totalElements;

    private final int totalPages;

    private final boolean first;

    private final boolean last;

    public PageResponse(
            List<T> content,
            int currentPage,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last
    ) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }
}