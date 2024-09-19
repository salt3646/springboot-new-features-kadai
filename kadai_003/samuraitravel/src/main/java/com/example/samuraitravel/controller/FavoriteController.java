package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteController(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	@GetMapping("/favorites")
	 public String reviewList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,			 				
			 				  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl
			 				 ) {
		 
		 User user = userDetailsImpl.getUser();
		 Page<Favorite> favoritePage = favoriteRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
		 
		 
		 model.addAttribute("user", user);
		 model.addAttribute("favoritePage", favoritePage);
		 
		 return "favorite/list";
	}
	
	
}
