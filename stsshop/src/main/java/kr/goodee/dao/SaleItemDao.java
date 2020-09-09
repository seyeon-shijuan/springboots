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

import kr.goodee.dao.mapper.SaleItemMapper;
import kr.goodee.logic.Sale;
import kr.goodee.logic.SaleItem;

@Repository
public class SaleItemDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	
	public void insert(SaleItem saleItem) {
		template.getMapper(SaleItemMapper.class).insert(saleItem);		
	}
	public List<SaleItem> list(int saleid) { //주문상품목록
		param.clear();
		param.put("saleid", saleid);
		return template.getMapper(SaleItemMapper.class).select(param);
	}	
}
