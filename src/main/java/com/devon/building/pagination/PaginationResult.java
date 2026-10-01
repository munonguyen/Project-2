package com.devon.building.pagination;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor
public class PaginationResult<E> {

    private int totalRecords;
    private int currentPage;
    private List<E> list;
    private int maxResult;
    private int totalPages;
    private int maxNavigationPage;
    private List<Integer> navigationPages;

    public PaginationResult(
            TypedQuery<E> query,
            TypedQuery<Long> countQuery,
            int page,
            int maxResult,
            int maxNavigationPage
    ) {
        initialize(page, maxResult, maxNavigationPage, countQuery.getSingleResult().intValue());
        this.list = query
                .setFirstResult((currentPage - 1) * maxResult)
                .setMaxResults(maxResult)
                .getResultList();
    }

    public PaginationResult(
            Query query,
            Class<E> resultClass,
            int totalRecords,
            int page,
            int maxResult,
            int maxNavigationPage
    ) {
        initialize(page, maxResult, maxNavigationPage, totalRecords);
        this.list = query
                .setFirstResult((currentPage - 1) * maxResult)
                .setMaxResults(maxResult)
                .getResultList()
                .stream()
                .map(resultClass::cast)
                .toList();
    }

    private void initialize(int page, int maxResult, int maxNavigationPage, int totalRecords) {
        if (maxResult <= 0) {
            throw new IllegalArgumentException("maxResult must be greater than zero");
        }
        if (maxNavigationPage <= 0) {
            throw new IllegalArgumentException("maxNavigationPage must be greater than zero");
        }

        this.maxResult = maxResult;
        this.currentPage = Math.max(page, 1);
        this.totalRecords = Math.max(totalRecords, 0);
        this.totalPages = (int) Math.ceil((double) this.totalRecords / maxResult);
        this.maxNavigationPage = Math.min(maxNavigationPage, Math.max(totalPages, 1));
        calcNavigationPages();
    }

    private void calcNavigationPages() {
        navigationPages = new ArrayList<>();
        if (totalPages <= 0) {
            return;
        }

        int current = Math.min(currentPage, totalPages);
        int begin = current - maxNavigationPage / 2;
        int end = current + maxNavigationPage / 2;

        navigationPages.add(1);

        if (begin > 2) {
            navigationPages.add(-1);
        }

        for (int i = begin; i <= end; i++) {
            if (i > 1 && i < totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < totalPages - 2) {
            navigationPages.add(-1);
        }

        if (totalPages > 1) {
            navigationPages.add(totalPages);
        }
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<E> getList() {
        return list;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public List<Integer> getNavigationPages() {
        return navigationPages;
    }

    public <T> PaginationResult<T> map(java.util.function.Function<E, T> mapper) {
        PaginationResult<T> result = new PaginationResult<>();
        result.setMaxResult(this.maxResult);
        result.setCurrentPage(this.currentPage);
        result.setTotalPages(this.totalPages);
        result.setTotalRecords(this.totalRecords);
        result.setMaxNavigationPage(this.maxNavigationPage);
        result.setNavigationPages(this.navigationPages);
        if (this.list != null) {
            result.setList(this.list.stream().map(mapper).toList());
        }
        return result;
    }
}
