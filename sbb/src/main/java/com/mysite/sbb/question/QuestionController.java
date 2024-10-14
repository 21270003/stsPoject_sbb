package com.mysite.sbb.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {
	
	private final QuestionService questionService;
	private final UserService userService;

	
	@GetMapping("/list")
	// @ResponseBody
	public String listQuestion( Model model, @RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="kw", defaultValue="") String keyword) {
		
		Page<Question> paging=this.questionService.getQuestionList( page, keyword );
		model.addAttribute("paging", paging );
		model.addAttribute("kw", keyword );
		return "question_list";
		
	}
	
	
	
	
	@GetMapping("/detail/{id}")
	public String goQuestionDetail( Model model, @PathVariable("id") int id, AnswerForm answerForm) {
		Question question = this.questionService.getOneQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
	
	@GetMapping("/create")
	public String insertQuestion( QuestionForm questionForm ) {
		return "question_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String insertQuestionOK(@Valid QuestionForm questionForm, 
			BindingResult bindingResult, Principal principal) { 
		if( bindingResult.hasErrors() ) { 
			return "question_form";
		}
		SiteUser user = this.userService.getUser(principal.getName());
		this.questionService.createQuestion( questionForm.getSubject(), questionForm.getContent(), user );
		return "redirect:/question/list";
	}

	@GetMapping("/modify/{question_id}")
	public String modifyQuestion( QuestionForm questionForm, 
			@PathVariable("question_id") int question_id, Principal principal ) {
		Question question = this.questionService.getOneQuestion(question_id);
		if( !question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "수정 권한이 없습니다...");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
		
	}


	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{question_id}")
	public String modifyQuestionOK(@Valid QuestionForm questionForm, 
			BindingResult bindingResult, @PathVariable("question_id") int question_id, Principal principal) { 
		if( bindingResult.hasErrors() ) { 
			return "question_form";
		}
		Question question = this.questionService.getOneQuestion(question_id);
		if( !question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "수정 권한이 없습니다...");
		}
		this.questionService.modifyQuestion( question, questionForm.getSubject(), questionForm.getContent() );
		return String.format("redirect:/question/detail/%s",question_id) ;
	}

	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{question_id}")
	public String deleteQuestionOK( @PathVariable("question_id") int question_id, Principal principal) { 
		Question question = this.questionService.getOneQuestion(question_id);
		if( !question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다...");
		}
		this.questionService.deletQuestion( question );
		return String.format("redirect:/question/list") ;
	}

	

}



























