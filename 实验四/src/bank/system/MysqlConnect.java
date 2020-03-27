package bank.system;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import bank.util.*;

public class MysqlConnect {
	
	// JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/bank_sql?serverTimezone=GMT%2B8";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "ttn991122";
  
    public static void main(String[] args) {
    	getClientInformation();
    	System.out.println("管理员信息：");
    	getManagerInformation();
    	System.out.println("账单信息：");
    	getToDoListInformation();
    	System.out.println("插入用户信息");
    	Client client = new Client();
    	client.setUserName("lalala");
    	client.setUserPassword("lalala");
    	client.setDeposit(0);
    	insertClient(client);
    	System.out.println("插入管理员信息");
    	Administrator administrator = new Administrator();
    	administrator.setUserName("heihei");
    	administrator.setUserPassword("heihei");
    	insertManager(administrator);
    }
    
    // 查询用户名对应的密码
    public static void getClientInformation() {
    	Information.clients = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, name, password, money FROM user";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                long id  = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                int money = rs.getInt("money");
                	
                Client client = new Client();
                client.setDeposit(money);
                client.setId(id);
                client.setUserName(name);
                client.setUserPassword(password);
                // 存储数据
                Information.clients.add(client);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    
    // 查询管理员用户对应的密码
    public static void getManagerInformation() {
    	Information.administrators = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, name, password FROM bankowner";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                long id  = rs.getLong("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
    
                Administrator administrator = new Administrator();
                administrator.setId(id);
                administrator.setUserName(name);
                administrator.setUserPassword(password);
                // 存储数据
                Information.administrators.add(administrator);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    // 查询管理员用户对应的密码
    public static void getChiefManagerInformation() {
      Information.chiefadministrators = new ArrayList<>();
      Connection conn = null;
      Statement stmt = null;
      try{
          // 注册 JDBC 驱动
          Class.forName(JDBC_DRIVER);
          
          // 打开链接
          System.out.println("连接数据库...");
          conn = DriverManager.getConnection(DB_URL,USER,PASS);
      
          // 执行查询
          stmt = conn.createStatement();
          String sql;
          sql = "SELECT id, name, password FROM chiefbankowner";
          ResultSet rs = stmt.executeQuery(sql);
          // 展开结果集数据库
          while(rs.next()){
              // 通过字段检索
              long id  = rs.getLong("id");
              String name = rs.getString("name");
              String password = rs.getString("password");
  
              ChiefAdministrator chiefadministrator = new ChiefAdministrator();
              chiefadministrator.setId(id);
              chiefadministrator.setUserName(name);
              chiefadministrator.setUserPassword(password);
              // 存储数据
              Information.chiefadministrators.add(chiefadministrator);
          }
          // 完成后关闭
          rs.close();
          stmt.close();
          conn.close();
      }catch(SQLException se){
          // 处理 JDBC 错误
          se.printStackTrace();
      }catch(Exception e){
          // 处理 Class.forName 错误
          e.printStackTrace();
      }finally{
          // 关闭资源
          try{
              if(stmt!=null) {
                stmt.close();
              }
          }catch(SQLException se2){
          }// 什么都不做
          try{
              if(conn!=null) {
                conn.close();
              }
          }catch(SQLException se){
              se.printStackTrace();
          }
      }
    }
    
    // 查询未处理的账单信息
    public static void getToDoListInformation() {
    	Information.Todos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id,user_id, user_name, money, statue FROM to_do_list";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                String name = rs.getString("user_name");
                int money  = rs.getInt("money");
                int statue = rs.getInt("statue");
    
                Todo todo = new Todo();
                todo.setId(id);
                todo.setUser_id(user_id);
                todo.setName(name);
                todo.setMoney(money);
                todo.setStatue(statue);
                // 存储数据
                Information.Todos.add(todo);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    
    // 更新操作账单状态
    public static void updateToDo(String update_Todo_id) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "update to_do_list set statue = 1 where id = " + update_Todo_id;
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
				System.out.println("更改状态成功!!!");
			} else {
				System.out.println("更改状态失败!!!");
			}
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    
    // 删除操作账单
    public static void deleteToDo(String delete_Todo_id) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "delete FROM to_do_list where id = " + delete_Todo_id;
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
                System.out.println("删除成功!!!");
            } else {
                System.out.println("删除失败!!!");
            }
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    
    // 增加账单信息
    public static int insertToDoList(Todo todo) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            Random rand = new Random();
            int id = rand.nextInt(100000)+ 1; 
            sql = "insert into to_do_list(id, user_id, user_name, money, statue) values(" + 
            		"'" + id + "', '" + todo.getUser_id() + "', '" + todo.getName() +"', '" + todo.getMoney() + "', '" + todo.getStatue() + "')";
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
				System.out.println("插入数据成功!!!");
				stmt.close();
	            conn.close();
				return id;
			} else {
				System.out.println("插入数据失败!!!");
			}
            // 完成后关闭
            stmt.close();
            conn.close();
            return 0;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return 0;
    }
    
    // 更改数据库的存款信息
    public static void updateDeposit(long client_id, long deposit) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "update user set money = '" + deposit + "' where id = '" + client_id + "'";
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
				System.out.println("更新存款成功!!!");
			} else {
				System.out.println("更新存款失败!!!");
			}
            // 完成后关闭
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
    
    // 增加用户信息
    public static int insertClient(Client client) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            Random rand = new Random();
            int id = rand.nextInt(100000)+ 1; 
            sql = "insert into user(id, name, password, money) values(" + 
            	"'" + id + "', '" + client.getUserName() + "', '" + client.getUserPassword() +"', '" + client.getDeposit() + "')";
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
				System.out.println("插入数据成功!!!");
				stmt.close();
	            conn.close();
				return id;
			} else {
				System.out.println("插入数据失败!!!");
			}
            // 完成后关闭
            stmt.close();
            conn.close();
            return 0;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return 0;
    }
    
    // 增加管理员信息
    public static int insertManager(Administrator administrator) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            Random rand = new Random();
            int id = rand.nextInt(100000)+ 1; 
            sql = "insert into bankowner(id, name, password) values(" + 
            	"'" + id + "', '" + administrator.getUserName() + "', '" + administrator.getUserPassword() + "')";
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
				System.out.println("插入数据成功!!!");
				stmt.close();
	            conn.close();
				return id;
			} else {
				System.out.println("插入数据失败!!!");
			}
            // 完成后关闭
            stmt.close();
            conn.close();
            return 0;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return 0;
    }
    
    // 增加上级管理员信息
    public static int insertChiefManager(ChiefAdministrator chiefadministrator) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);
            
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        
            // 执行查询
            stmt = conn.createStatement();
            String sql;
            Random rand = new Random();
            int id = rand.nextInt(100000)+ 1; 
            sql = "insert into chiefbankowner(id, name, password) values(" + 
                "'" + id + "', '" + chiefadministrator.getUserName() + "', '" + chiefadministrator.getUserPassword() + "')";
            int rs = stmt.executeUpdate(sql);
            if (rs > 0) {
                System.out.println("插入数据成功!!!");
                stmt.close();
                conn.close();
                return id;
            } else {
                System.out.println("插入数据失败!!!");
            }
            // 完成后关闭
            stmt.close();
            conn.close();
            return 0;
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) {
                  stmt.close();
                }
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) {
                  conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return 0;
    }
}
