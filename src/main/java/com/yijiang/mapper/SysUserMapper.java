package com.yijiang.mapper;

import com.yijiang.domain.SysUser;
import com.yijiang.domain.SysUserExample;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SysUserMapper {
    @SelectProvider(type=SysUserSqlProvider.class, method="countByExample")
    int countByExample(SysUserExample example);

    @DeleteProvider(type=SysUserSqlProvider.class, method="deleteByExample")
    int deleteByExample(SysUserExample example);

    @Delete({
        "delete from sys_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into sys_user (user_name)",
        "values (#{userName,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(SysUser record);

    @InsertProvider(type=SysUserSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(SysUser record);

    @SelectProvider(type=SysUserSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR)
    })
    List<SysUser> selectByExample(SysUserExample example);

    @Select({
        "select",
        "id, user_name",
        "from sys_user",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR)
    })
    SysUser selectByPrimaryKey(Integer id);

    @UpdateProvider(type=SysUserSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") SysUser record, @Param("example") SysUserExample example);

    @UpdateProvider(type=SysUserSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") SysUser record, @Param("example") SysUserExample example);

    @UpdateProvider(type=SysUserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SysUser record);

    @Update({
        "update sys_user",
        "set user_name = #{userName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(SysUser record);
}