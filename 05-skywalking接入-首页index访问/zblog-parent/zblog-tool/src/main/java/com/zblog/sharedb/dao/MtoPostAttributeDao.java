package com.zblog.sharedb.dao;

import com.zblog.sharedb.entity.MtoPostAttribute;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface MtoPostAttributeDao {
    @Delete({
        "delete from mto_post_attribute",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into mto_post_attribute (id, editor, ",
        "content)",
        "values (#{id,jdbcType=BIGINT}, #{editor,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=LONGVARCHAR})"
    })
    int insert(MtoPostAttribute record);

    @Select({
        "select",
        "id, editor, content",
        "from mto_post_attribute",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR)
    })
    MtoPostAttribute selectByPrimaryKey(Long id);

    @Select({
        "select",
        "id, editor, content",
        "from mto_post_attribute"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="editor", property="editor", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<MtoPostAttribute> selectAll();

    @Update({
        "update mto_post_attribute",
        "set editor = #{editor,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MtoPostAttribute record);
}