package kr.goodee.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.goodee.logic.Item;
@Mapper
public interface ItemMapper {

	@Select({"<script>",
		"select * from item ",
		"<if test='id != null'> where id=#{id} </if>",
		"</script>"})
	List<Item> select(Map<String,Object> param);

	@Select("select ifnull(max(id),0) from item")
	int maxid();

	@Insert("insert into item "
  + "(id,name,price,description,pictureUrl ) "
  + " values (#{id}, #{name},#{price},#{description},#{pictureUrl})")
	void insert(Item item);

	@Update("update item "
		  + " set name=#{name}, price=#{price},"
		  + " description=#{description},pictureUrl=#{pictureUrl}"
		  + " where id=#{id}")
	void update(Item item);

	@Delete("delete from item where id=#{id}")
	void delete(Map<String, Object> param);
}