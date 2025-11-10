package com.wt.spring_project1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wt.spring_project1.entity.Menu;

// @Component와 비슷 => @ComponentScan
// @Mapper => @MapperScan => 자동으로 스프링 컨테이너에 등록
@Mapper
public interface MenuRestMapper {
	// 첫번째 게시글 -> idx: 1
	// 두번째 게시글 -> idx: 2
	// 가장 최근 게시글이 공지사항 페이지 상단에 보여지도록 DESC
	// * 쓰지말고 idx, memID 등 요소 다 작성하기
	@Select("SELECT idx, memID, title, content, writer, indate, count FROM backend_spring_project.menu ORDER BY idx DESC")
	public List<Menu> getLists();

	@Insert("INSERT INTO backend_spring_project.menu(memID, title, content, writer, indate) VALUES(#{memID}, #{title}, #{content}, #{writer}, #{indate})")
	public void boardInsert(Menu menu);

	@Select("SELECT idx, memID, title, content, writer, indate, count FROM backend_spring_project.menu WHERE idx=#{idx}")
	public Menu boardContent(int idx);

	@Delete("DELETE FROM backend_spring_project.menu WHERE idx=#{idx}")
	public void boardDelete(int idx);

	@Update("UPDATE backend_spring_project.menu SET title=#{title}, content=#{content}, writer=#{writer} WHERE idx=#{idx}")
	public void boardUpdate(Menu menu);

	@Update("UPDATE backend_spring_project.menu SET count=count+1 WHERE idx=#{idx}")
	public void boardCount(int idx);
}