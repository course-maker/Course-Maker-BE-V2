package net.coursemaker.backendv2.util;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseMakerPagination<E> {
	@Schema(description = "현재 페이지", example = "1")
	private Integer currentPage;
	@Schema(description = "전체 페이지", example = "10")
	private Integer totalPage;
	@Schema(description = "페이지당 데이터 갯수", example = "12")
	private Integer pagingSlice;
	@Schema(description = "전체 데이터 갯수", example = "120")
	private Long totalContents;
	@Schema(description = "데이터")
	private List<E> contents;

	private CourseMakerPagination(){}

	public CourseMakerPagination(Pageable pageable, Page<E> page, long totalElements){
		this.currentPage = pageable.getPageNumber() + 1;
		this.totalPage = page.getTotalPages();
		this.pagingSlice = pageable.getPageSize();
		this.contents = page.getContent();
		this.totalContents = totalElements;
	}

	public CourseMakerPagination(Pageable pageable, List<E> contentList){

		int start=(int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), contentList.size());
		Page<E> page = new PageImpl<>(contentList.subList(start, end), pageable, contentList.size());

		this.currentPage = pageable.getPageNumber() + 1;
		this.totalPage = page.getTotalPages();
		this.pagingSlice = pageable.getPageSize();
		this.contents = page.getContent();
		this.totalContents = (long)contentList.size();
	}
}
