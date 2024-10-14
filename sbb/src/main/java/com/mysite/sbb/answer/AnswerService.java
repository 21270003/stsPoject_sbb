package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	private final QuestionService questionService;

	public void create( String content, int question_id ) {
		Question question = this.questionService.getOneQuestion(question_id);
		Answer answer = new Answer ();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setCreateDate(LocalDateTime.now());
		this.answerRepository.save( answer );
	}
	
	public void create(Question question, String content, SiteUser user) {
		Answer answer = new Answer ();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setAuthor(user);
		answer.setCreateDate(LocalDateTime.now());
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save( answer );
	}
	
	
	public Answer getOneAnswer( int answer_id ){
		Optional<Answer> oa = this.answerRepository.findById(answer_id);
		if( oa.isPresent() ) {
			return oa.get();
		}else {
			throw new DataNotFoundException("answer not found...");
		}
	}

	public void modifyQuestion(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate( LocalDateTime.now() );
		this.answerRepository.save( answer );		
	}

	public void deleteAnswer(Answer answer) {
		this.answerRepository.delete(answer);
	}

	

}
