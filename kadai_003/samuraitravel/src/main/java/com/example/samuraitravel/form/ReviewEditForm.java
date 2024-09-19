package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
		/*
	 	@NotNull(message = "houseIdがありません")
		private Integer houseId;
	 	
	 	@NotNull(message = "userIdがありません")
		private Integer userId;
		*/
		@NotNull(message = "評価を選択してください")
		private Integer review_Point;
		
		@NotBlank(message = "コメントを入力してください。")
		private String user_Comment;
}
