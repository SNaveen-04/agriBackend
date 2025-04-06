package com.agri.backend.DTO;

import com.agri.backend.entity.User;

public class DTOMapper {
	public static UserDTO convertUserToUserDTO(User user) {
		UserDTO userDto = new UserDTO();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setNumber(user.getNumber());
		userDto.setUserName(user.getUserName());
		userDto.setUserType(user.getUserType());
		return userDto;
	}
}
