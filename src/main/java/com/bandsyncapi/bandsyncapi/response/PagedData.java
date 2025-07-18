package com.bandsyncapi.bandsyncapi.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

/**
 * Object that represents paginated data to be sent to the client.
 * It contains the content of the page, page number, page size, total elements,
 * total pages, and flags for next and previous pages.
 *
 * @param <T> the type of content in the page
 */
@Data
public class PagedData<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean hasNext;
  private boolean hasPrevious;

  public PagedData(List<T> content, Page<?> pageInfo) {
    this.content = content;
    this.pageNumber = pageInfo.getNumber();
    this.pageSize = pageInfo.getSize();
    this.totalElements = pageInfo.getTotalElements();
    this.totalPages = pageInfo.getTotalPages();
    this.hasNext = pageInfo.hasNext();
    this.hasPrevious = pageInfo.hasPrevious();
  }
}
