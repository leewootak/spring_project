package com.canesblack.spring_project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canesblack.spring_project1.entity.User;
import com.canesblack.spring_project1.mapper.UserMapper;

@Service
public class UserService {
	@Autowired // 의존성 주입
	private UserMapper userMapper;

	public void insertUser(User user) {
		userMapper.insertUser(user);
	}

	public String findWriter(String username) {
		return userMapper.findWriter(username);
	}
}
