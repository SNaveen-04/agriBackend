package com.agri.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.agri.backend.entity.Category;
import com.agri.backend.service.CategoryService;


@RestController
@RequestMapping("category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("")
    public ResponseEntity<?> getCategories(){
    	try {
    		List<Category> categories = categoryService.getCategory();
    		return ResponseEntity.status(HttpStatus.OK).body(categories);    		
    	}
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong"); 
    	}
    }
	
	@GetMapping("{id}")
	public ResponseEntity<?> getCategoryById(@RequestParam long id){
    	try {
    		Category category = categoryService.getCategoryById(id);
    		return ResponseEntity.status(HttpStatus.OK).body(category);    		
    	}
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong"); 
    	}
    }
	
	
	@PostMapping("")
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
    	try {
            Category savedCategory = categoryService.saveCategory(category); 
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
	
	@GetMapping("/latest-id")
    public ResponseEntity<?> getLatestCategoryId() {
        try {
            Long latestId = categoryService.getLatestCategoryId();
            return ResponseEntity.ok(latestId != null ? latestId : "No categories found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving latest category ID");
        }
    }
}
