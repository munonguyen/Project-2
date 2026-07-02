package com.devon.building.pagination;

import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class PaginationResult<E> {

    private int totalRecords;
    private int currentPage;
    private List<E> list;
    private int maxResult;
    private int totalPages;
    private int maxNavigationPage;
    private List<Integer> navigationPages;

    public PaginationResult(TypedQuery<E> query, TypedQuery<Long> countQuery, int page, int maxResult, int maxNavigationPage) {
        this(
                query.setFirstResult((Math.max(page, 1) - 1) * maxResult)
                        .setMaxResults(maxResult)
                        .getResultList(),
                countQuery.getSingleResult().intValue(),
                page,
                maxResult,
                maxNavigationPage
        );
    }

    public PaginationResult(List<E> list, int totalRecords, int page, int maxResult, int maxNavigationPage) {
        this.list = list;
        this.totalRecords = totalRecords;
        this.maxResult = maxResult;
        this.currentPage = Math.max(page, 1);
        this.totalPages = maxResult == 0 ? 0 : (int) Math.ceil((double) totalRecords / maxResult);
        this.maxNavigationPage = Math.min(maxNavigationPage, totalPages);
        calcNavigationPages();
    }

    private void calcNavigationPages() {
        navigationPages = new ArrayList<>();

        int current = Math.min(currentPage, totalPages);

        int begin = current - maxNavigationPage / 2;
        int end = current + maxNavigationPage / 2;

        navigationPages.add(1);

        if (begin > 2) navigationPages.add(-1);

        for (int i = begin; i <= end; i++) {
            if (i > 1 && i < totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < totalPages - 2) navigationPages.add(-1);

        if (totalPages > 1) navigationPages.add(totalPages);
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
}
