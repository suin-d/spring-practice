package com.kh.spring.common.model.vo;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PageInfo {

	private int listCount;
	private int currentPage;
	private int startPage;
	private int endPage;
	private int maxPage;
	private int pageLimit;
	private int boardLimit;
	
}
