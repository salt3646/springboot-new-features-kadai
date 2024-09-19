package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;  
    private final UserRepository userRepository;
	
	public ReviewService(ReviewRepository reviewRepositoty, HouseRepository houseRepository, UserRepository userRepository) {
		this.reviewRepository = reviewRepositoty;
		this.houseRepository = houseRepository;  
        this.userRepository = userRepository; 
	}
	
	@Transactional
	public void createPost(int houseId, int userId, ReviewPostForm reviewPostForm) {
		 Review review = new Review();
		 House house = houseRepository.getReferenceById(houseId);
		 User user = userRepository.getReferenceById(userId);
		 
         /*
		 switch(reviewPostForm.getReview_Point()) {
		 case "&#9733;&#9734;&#9734;&#9734;&#9734;" -> review.setReview_Point(1);
		 case "&#9733;&#9733;&#9734;&#9734;&#9734;" -> review.setReview_Point(2);
		 case "&#9733;&#9733;&#9733;&#9734;&#9734;" -> review.setReview_Point(3);
		 case "&#9733;&#9733;&#9733;&#9733;&#9734;" -> review.setReview_Point(4);
		 case "&#9733;&#9733;&#9733;&#9733;&#9733;" -> review.setReview_Point(5);
		 }
		 */
		review.setHouse(house);
		review.setUser(user);
		review.setReview_Point(reviewPostForm.getReview_Point());
		review.setUser_Comment(reviewPostForm.getUser_Comment());
	
		reviewRepository.save(review);
	}
	
	@Transactional
	public void createEdit(int houseId, int userId, int reviewId, ReviewEditForm reviewEditForm) {
		 Review review = reviewRepository.getReferenceById(reviewId);
		 House house = houseRepository.getReferenceById(houseId);
		 User user = userRepository.getReferenceById(userId);
		 
		review.setHouse(house);
		review.setUser(user);
		review.setReview_Point(reviewEditForm.getReview_Point());
		review.setUser_Comment(reviewEditForm.getUser_Comment());
	
		reviewRepository.save(review);
	}
}
