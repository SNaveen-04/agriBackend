package com.agri.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.backend.entity.Category;
import com.agri.backend.repository.CategoryRepo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	public List<Category> getCategory() {
		return categoryRepo.findAll();
	}
	
	public Category getCategoryById(long id) {
		return categoryRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Category not found"));
	}

	public Category saveCategory(Category category) {
		if (category.getId() != null && categoryRepo.existsById(category.getId())) {
			// Updating an existing category
			Category existingCategory = categoryRepo.findById(category.getId())
					.orElseThrow(() -> new RuntimeException("Category not found"));
			existingCategory.setName(category.getName());
			existingCategory.setImg(category.getImg());
			existingCategory.setDescription(category.getDescription());
			return categoryRepo.save(existingCategory);
		} else {
			// Saving a new category
			category.setId(null); // Ensure it's not mistakenly treated as an update
			return categoryRepo.save(category);
		}
	}

	public void deleteCategory(long id) {
		try {
			if (categoryRepo.existsById(id)) {
				categoryRepo.delete(
						categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found")));
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong");
		}
	}

	public Long getLatestCategoryId() {
        return categoryRepo.findLatestCategoryId();
    }
}
