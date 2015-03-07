package edu.xautjzd.activityrecognition.predict.util;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("attributeDao")
public class AttributeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public List<String> getActions() {
		String sql= "select distinct Action from attributes";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}
}
