package com.mysite.sbb.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/answer")
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;

	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{question_id}")
	public String createAnswer(@Valid AnswerForm answerForm, BindingResult bindingResult,  
			Model model, @PathVariable("question_id") int question_id , Principal principal ){
		
		Question question = this.questionService.getOneQuestion(question_id);
		if( bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		SiteUser user = this.userService.getUser(principal.getName());
		this.answerService.create(question, answerForm.getContent(), user);
		return String.format("redirect:/question/detail/%s#",question_id);
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{anser_id}")
	public String modifyQuestionOK(AnswerForm answerForm, 
			@PathVariable("anser_id") int anser_id, Principal principal) { 
		Answer answer = this.answerService.getOneAnswer( anser_id );
		
		if( !answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "수정 권한이 없습니다...");
		}
		answerForm.setContent( answer.getContent() );
		return "answer_form" ;
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{anser_id}")
	public String modifyQuestionOK(@Valid AnswerForm answerForm,
			BindingResult bindingResult, @PathVariable("anser_id") int anser_id, Principal principal) { 
		if( bindingResult.hasErrors() ) { 
			return "answer_form";
		}
		Answer answer = this.answerService.getOneAnswer( anser_id );
		if( !answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "수정 권한이 없습니다...");
		}
		this.answerService.modifyQuestion( answer, answerForm.getContent() );
		return String.format("redirect:/question/detail/%s#answer_%s",answer.getQuestion().getId(), answer.getId()) ;
	}

	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{anser_id}")
	public String deleteQuestionOK( @PathVariable("anser_id") int anser_id, Principal principal) { 
		Answer answer = this.answerService.getOneAnswer( anser_id );
		if( !answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "수정 권한이 없습니다...");
		}
		this.answerService.deleteAnswer( answer );
		return String.format("redirect:/question/detail/%s#answer_%s",answer.getQuestion().getId(), answer.getId()) ;
	}


	
	
}
