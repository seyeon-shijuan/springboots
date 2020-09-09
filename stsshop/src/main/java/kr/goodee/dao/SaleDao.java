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

import kr.goodee.dao.mapper.SaleMapper;
import kr.goodee.logic.Sale;

@Repository //@Component + db관련 클래스
public class SaleDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	public int getMaxSaleid() {
		return template.getMapper(SaleMapper.class).getMaxSaleid();
	}
	public void insert(Sale sale) {
		template.getMapper(SaleMapper.class).insert(sale);
	}
	public List<Sale> list(String id) {
		param.clear();
		param.put("userid", id);
		return template.getMapper(SaleMapper.class).select(param);
	}	
}