package com.kh.spring.board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Reply {

	private int replyNo;
	private String replyContent;
	private int refBno;
	private String replyWriter;
	private String createDate; // 조회해올 때 타입 변경 가능! 
	private String status;
	
}
