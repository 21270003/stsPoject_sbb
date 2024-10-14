package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	

	@SuppressWarnings("unused")
	private Specification<Question> search(String kw) {
					
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				String keyword = "%"+kw+"%";
				query.distinct(true); // 중복을 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), keyword ) ,// 제목
							cb.like(q.get("content"), keyword ),// 내용
							cb.like(u1.get("username"), keyword ), // 질문 작성자
							cb.like(a.get("content"), keyword ), // 답변 내용
							cb.like(u2.get("username"), keyword ) 
					   ); // 답변 작성자
			}
		};
		
	}
	
	public Page<Question> getQuestionList(int page, String keyword ){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Question> spec = search( keyword );
		return this.questionRepository.findAll( spec, pageable );
	}
	
	
	public Page<Question> getQuestionList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.questionRepository.findAll( pageable );
	}
	

	public List<Question> getQuestionList(){
		//List<Question> questionList =  this.questionRepository.findAll();
		//return questionList;
		return this.questionRepository.findAll();
	}

	
	public Question getOneQuestion( int id ){
		Optional<Question> oq = this.questionRepository.findById(id);
		if( oq.isPresent() ) {
			return oq.get();
		}else {
			throw new DataNotFoundException("question not found...");
		}
	}


	public void createQuestion(String subject, String content, SiteUser user) {
		Question question = new Question();
		question.setContent(content);
		question.setSubject(subject);
		question.setAuthor(user);
		question.setCreateDate(LocalDateTime.now());
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);		
	}

	public void modifyQuestion(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);	
	}


	public void deletQuestion(Question question) {
		this.questionRepository.delete(question);		
	}
	
	
	
	
	
	
}
