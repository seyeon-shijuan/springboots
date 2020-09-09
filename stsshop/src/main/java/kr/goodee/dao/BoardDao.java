package kr.goodee.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import kr.goodee.dao.mapper.BoardMapper;
import kr.goodee.logic.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	
	public int maxnum() {
		return template.getMapper(BoardMapper.class).maxnum();
	}
	public void insert(Board board) {
		template.getMapper(BoardMapper.class).insert(board);
	}
	public int count(String searchtype,String searchcontent) {
		param.clear();
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		return template.getMapper(BoardMapper.class).count(param);
	}
	public List<Board> list(Integer pageNum, int limit,
			String searchtype,String searchcontent) {
		param.clear();
		param.put("searchtype", searchtype);
		param.put("searchcontent", searchcontent);
		param.put("startrow", (pageNum - 1) * limit);
		param.put("limit",  limit);		
		return template.getMapper(BoardMapper.class).select(param);
	}
	public void readcntadd(Integer num) {
		template.getMapper(BoardMapper.class).readcntadd(num);
	}
	public Board selectOne(Integer num) {
		param.clear();
		param.put("num", num);
		List<Board> list = template.getMapper(BoardMapper.class).
		 select(param);
		if (list == null || list.size() == 0) return null;
		else return list.get(0);
	}
	public void updateGrpStep(Board board) {
		param.clear();
		param.put("grp", board.getGrp());
		param.put("grpstep", board.getGrpstep());
		template.getMapper(BoardMapper.class).updateGrpStep(param);
	}
	public void update(Board board) {
		template.getMapper(BoardMapper.class).update(board);
	}
	public void delete(int num) {
		template.getMapper(BoardMapper.class).delete(num);
	}
	public List<Map<String, Object>> graph1() {
		return template.getMapper(BoardMapper.class).graph1();
	}
	public List<Map<String, Object>> graph2() {
		return template.getMapper(BoardMapper.class).graph2();
	}
}
