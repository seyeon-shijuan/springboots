package kr.goodee.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.goodee.logic.Sale;
@Mapper
public interface SaleMapper {

	@Select("select ifnull(max(saleid),0) from sale")
	int getMaxSaleid();

	@Insert("insert into sale (saleid,userid,saledate)"
			+" values (#{saleid}, #{userid}, now())")
	void insert(Sale sale);

	@Select("select * from sale where userid = #{userid}")
	List<Sale> select(Map<String, Object> param);

}
