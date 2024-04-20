package com.coker.springboot.dto;

import jakarta.validation.constraints.NotBlank;

public class SearchDTO {
    @NotBlank(message = "Can not be blanked")
    private String keyword;
    private Integer size = 10;
    private Integer currentPage = 0;
    private String sortedByColumn = "id";

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getSortedByColumn() {
        return sortedByColumn;
    }

    public void setSortedByColumn(String sortedByColumn) {
        this.sortedByColumn = sortedByColumn;
    }
}
