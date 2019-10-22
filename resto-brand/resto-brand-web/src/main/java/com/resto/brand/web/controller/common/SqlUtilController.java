package com.resto.brand.web.controller.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.resto.brand.core.entity.Result;
import com.resto.brand.core.enums.RoleSign;
import com.resto.brand.core.util.ApplicationUtils;
import com.resto.brand.web.config.SessionKey;
import com.resto.brand.web.controller.GenericController;
import com.resto.brand.web.model.DatabaseConfig;
import com.resto.brand.web.service.DatabaseConfigService;

@RequiresRoles({RoleSign.SUPER_ADMIN})
@Controller
@RequestMapping("sqlutil")
public class SqlUtilController extends GenericController {

	@Resource
	DatabaseConfigService databaseconfigService;

	@RequestMapping("/list")
	public void list() {
	}

	@RequestMapping("/query")
	@ResponseBody
	public List<DatabaseConfig> query() {
		return databaseconfigService.selectList();
	}

	@RequestMapping("/run")
	@ResponseBody
	public Result runSql(String [] databaseIds,String sql,String pwd) throws ClassNotFoundException {
		if(!SessionKey.SECRET_KEY.equals(ApplicationUtils.pwd(pwd))){
			return new Result("迷之口令错误！",false);
		}
		Result result = new Result(true);
		String url = null;
		String driver = null;
		String username = null;
		String password = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int row = 0;
		StringBuffer msg = new StringBuffer();
		sql = sql.replaceAll("；", ";");
		String[] sqls = sql.toLowerCase().split(";");
		for(String currentSql : sqls){
			msg.append("SQL："+currentSql);
			for(String databaseId : databaseIds){
				try {
					//查询 选中的数据库信息
					DatabaseConfig databaseConfig = databaseconfigService.selectByPrimaryKey(databaseId);
					url = databaseConfig.getUrl();
					driver = databaseConfig.getDriverClassName();
					Class.forName(driver);
					username = databaseConfig.getUsername();
					password = databaseConfig.getPassword();
					con = DriverManager.getConnection(url, username, password);
					st = con.createStatement();// 创建sql执行对象
					if(currentSql.startsWith("select")){
						rs = st.executeQuery(currentSql);
					}else{
						row += st.executeUpdate(currentSql);
					}
				} catch (SQLException e) {
					result.setSuccess(false);
					msg.append("<p class='text-danger'>错误信息："+e.getMessage()+"</p>");
				}finally {
					close(con, st, rs);
				}
			}
			msg.append("<p class='text-info'>影响了 "+row+" 行</p><hr/>");
			row = 0;
		}
		result.setMessage(msg.toString());
		return result;
	}
	
	// 关闭相关的对象
	public void close(Connection con, Statement st, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
