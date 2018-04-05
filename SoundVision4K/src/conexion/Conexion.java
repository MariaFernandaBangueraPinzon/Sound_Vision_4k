package conexion;

import java.sql.*;


public class Conexion {
	
	private String nombreBd="proyectoreserva";
	private String usuario="root";
	private String password="";
	private String url="jdbc:mysql://localhost:3306/"+nombreBd;
	private boolean mysql;
	Connection conn=null;
	
	public Conexion(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url,usuario,password);
			if (conn!=null) {
				System.out.println("Conexion Exitosa a la BD: "+ nombreBd);
				mysql=true;
			}else{
				System.out.println("********** No Se Pudo Conectar");
				mysql=false;
			}
		}catch(ClassNotFoundException e){
			System.out.println("Ocurre una ClassNotFoundException: "+e.getMessage());
			mysql=false;
		}catch (SQLException e) {
			System.out.println("Ocurre una SQLException : "+e.getMessage());
			System.out.println("Verifique si mysql esta encendido...: ");
			mysql=false;
		}
	}
	public Connection getConnection() {
		return conn;
	}
	
	public void desconectar(){
		conn=null;
	}
	public boolean isMysql() {
		return mysql;
	}
	public void setMysql(boolean mysql) {
		this.mysql = mysql;
	}
}
