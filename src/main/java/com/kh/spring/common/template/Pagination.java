package com.kh.spring.common.template;

import com.kh.spring.common.model.vo.PageInfo;

public class Pagination {

	public static PageInfo getPageInfo(int listCount, int currentPage, int pageLimit, int boardLimit) {
		// * maxPage : listCount, boardLimit 영향을 받는다 
		int maxPage = (int)Math.ceil((double)listCount/boardLimit);
		// double형 강제형변환 하면 소숫점 아래까지 값이 존재 => 나머지를 올림처리한 후 다시 int형변환 
		
		//* startPage : currentPage, pageLimit 영향
		int startPage = (currentPage - 1)/pageLimit * pageLimit +1; 

		// * endPage: startPage, pageLimit, maxPage
		int endPage = startPage + pageLimit - 1; 
		
		if(endPage > maxPage) {
			endPage = maxPage;
		}
	
		return new PageInfo(listCount, currentPage, startPage, endPage, maxPage, pageLimit, boardLimit);
		// PageInfo에 설정해둔 순서대로 담기 
	}
	
	
}
