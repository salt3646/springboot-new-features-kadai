package com.example.samuraitravel.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{id}")
public class ReviewController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService; 
	
	public ReviewController(HouseRepository houseRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}
	
	@GetMapping("/reviewlist")
	 public String reviewList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			 				  @PathVariable(name = "id") Integer id,
			 				  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
			 				 ) {
		 House house = houseRepository.getReferenceById(id);
		 Page<Review> reviewPage = reviewRepository.findAllByHouseOrderByCreatedAtDesc(house, pageable);
		 
		 if (userDetailsImpl != null) {
	        	User user = userDetailsImpl.getUser();
	        	 model.addAttribute("user", user);
         } else {
        	     model.addAttribute("user", null);
         }
		 
		 model.addAttribute("house", house);
		 model.addAttribute("reviewPage", reviewPage);
		 
		 return "review/list";
	}
	
	
	 @GetMapping("/reviewpost")
	 public String reviewPost(@PathVariable(name = "id") Integer id,
             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                          
             Model model) 
	 {
		 House house = houseRepository.getReferenceById(id);
         User user = userDetailsImpl.getUser(); 
         
         
         model.addAttribute("house", house);
         model.addAttribute("user", user);
         model.addAttribute("reviewPostForm", new ReviewPostForm());
         
         return "review/post";
	 }
	 
	 @GetMapping("/reviewedit/{reviewId}")
	 public String reviewEdit(@PathVariable(name = "id") Integer id,
			 @PathVariable(name = "reviewId") Integer reviewId,
             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,                          
             Model model) 
	 {
		 House house = houseRepository.getReferenceById(id);
         User user = userDetailsImpl.getUser();
         Review review = reviewRepository.getReferenceById(reviewId);
         
         ReviewEditForm reviewEditForm = new ReviewEditForm(review.getReview_Point(), review.getUser_Comment());
         
         model.addAttribute("house", house);
         model.addAttribute("user", user);
         model.addAttribute("newReview", review);
         model.addAttribute("reviewEditForm", reviewEditForm);
         
         return "review/edit";
	 }

	 @PostMapping("/reviewpost/create")
	 public String createPost(@PathVariable(name = "id") Integer id, Model model,
			 				  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			 				  @ModelAttribute @Validated ReviewPostForm reviewPostForm,
			 				  BindingResult bindingResult,
			 				  RedirectAttributes redirectAttributes)
	 {
		 
		 User user = null;
		 
		 if (userDetailsImpl != null) {
			 user = userDetailsImpl.getUser();
        	 model.addAttribute("user", user);
         } else {
        	 model.addAttribute("user", null);
         }
		 
		 if (bindingResult.hasErrors()) {
			 House house = houseRepository.getReferenceById(id);
	         List<Review> newReviews = reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
	        
	         boolean reviewNull =CollectionUtils.isEmpty(newReviews);
	         model.addAttribute("house", house); 
	         model.addAttribute("reservationInputForm", new ReservationInputForm());
	         model.addAttribute("newReviews", newReviews);
	         model.addAttribute("reviewNull", reviewNull);
	         model.addAttribute("errorMessage", "投稿内容に不備があります。"); 
	        
	        return "houses/show";
         }
		 
		 reviewService.createPost(id, user.getId(), reviewPostForm);
		 
		 redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		 
		 return "redirect:/houses/"+id;
	 }
	 
	 @PostMapping("/reviewedit/{reviewId}/create")
	 public String createEdit(@PathVariable(name = "id") Integer id,
			 				  @PathVariable(name = "reviewId") Integer reviewId,Model model,
							  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
							  @ModelAttribute @Validated ReviewEditForm reviewEditForm,
							  BindingResult bindingResult,
							  RedirectAttributes redirectAttributes)
		{
		
		User user = null;
		
		if (userDetailsImpl != null) {
			user = userDetailsImpl.getUser();
			model.addAttribute("user", user);
			} else {
			model.addAttribute("user", null);
		}
		
		if (bindingResult.hasErrors()) {
			House house = houseRepository.getReferenceById(id);
			List<Review> newReviews = reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
			
			boolean reviewNull =CollectionUtils.isEmpty(newReviews);
			model.addAttribute("house", house); 
			model.addAttribute("reservationInputForm", new ReservationInputForm());
			model.addAttribute("newReviews", newReviews);
			model.addAttribute("reviewNull", reviewNull);
			model.addAttribute("errorMessage", "投稿内容に不備があります。"); 
			
			return "houses/show";
		}
		
		reviewService.createEdit(id, user.getId(), reviewId, reviewEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/"+id;
	 }
	 
	 @PostMapping("/reviewdelete/{reviewId}")
     public String delete(@PathVariable(name = "id") Integer id,
    		 @PathVariable(name = "reviewId") Integer reviewId, 
    		 RedirectAttributes redirectAttributes) 
	 {        
         reviewRepository.deleteById(reviewId);
                 
         redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
         
         return "redirect:/houses/"+id;
     }    
}
