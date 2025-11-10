package com.wt.spring_project1.mapper;

//import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
//import org.apache.ibatis.annotations.Update;

import com.wt.spring_project1.entity.User;

//@Component와 비슷하게 스프링 컨테이너에 등록
// 자바와 mysql 사이 통역 역할
@Mapper
public interface UserMapper {
	// CRUD 중 CREATE
	@Insert("INSERT INTO backend_spring_project.user(username, password, writer, role)"
			+ "VALUES(#{username}, #{password}, #{writer}, #{role})")

	// void => DB에서 백엔드 영역(스프링 프레임워크)으로 데이터를 가져오는게 없기 때문에 void로 가져오는게 없다고 작성
	void insertUser(User user);

	// CRUD 중 READ
	@Select("SELECT username, password, writer, role FROM backend_spring_project.user WHERE username = #{username}")
	User findByUsername(String username);

	@Select("SELECT writer FROM backend_spring_project.user WHERE username = #{username}")
	String findWriter(String username);

//	// CRUD 중 UPDATE
//	@Update()
//	
//	// CRUD 중 DELETE
//	@Delete()

}
