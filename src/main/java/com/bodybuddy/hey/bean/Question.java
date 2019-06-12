package com.bodybuddy.hey.bean;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Alias("question")
@Setter @Getter
public class Question {
	private String ad_code;
	private String ad_name;
	private String ad_title;
	private String ad_content;
	private String ad_kind;
	private String ad_category;
	private String ad_status;
	private String ad_date;
	
	private String qa_adcode;	
	private String qa_writer;	
	private String qa_wcontent;	
	private String qa_wdate;	
	private String qa_acontent;	
	private String qa_adate;
	private String qa_answer;
	private String qa_num;
	/* 얍얍 */
}