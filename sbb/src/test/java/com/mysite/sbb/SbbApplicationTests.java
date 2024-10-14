package com.mysite.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysite.sbb.question.QuestionService;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired
	private QuestionService questionService ;

	@Test
	void testJpa() {
		for(int i=0;i<900; i++) {
			String subject=(i+1)+"-SBB 테스트 질문입니다..";
			String content=(i+1)+"-SBB 테스트 질문의 구체적인 내용입니다..";
			this.questionService.createQuestion(subject, content, null);
		}
	}

}
