package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import vo.Ambiente;
import vo.CronogramaVo;
import vo.InformeVo;
import vo.ReservaVo;
import vo.Usuario;

public class UsuarioDao {

	String tablaHora;
	String tabla;
	String resp = "";
	ArrayList<ReservaVo> reservasEnRango;
	Conexion miConexion;
	Connection connection;
	int horaS;
	int horaE;
	Usuario miUsuario;
	PreparedStatement statement;
	private String cad2Entrada;
	private String cad1Entrada;
	private int pos = 0;
	private String cad1Salida;
	ArrayList<ArrayList<Integer>> reservas;
	boolean disponibilidadSolicitud = true;
	boolean disponibilidadReserva = true;

	public int agregarUsuario(Usuario miUsuario) {
		int resultado = 0;

		Connection connection = null;
		Conexion conexion = new Conexion();
		PreparedStatement preStatement = null;

		connection = conexion.getConnection();
		String consulta = "INSERT INTO `usuarioreser` (`nombreUsu`, `telUsu`, `cedulaUsu`, `rolUsu`, `emailUsu`, `contraseniaUsu`) "
				+ "VALUES (?,?,?,?,?,?);";

		try {
			preStatement = connection.prepareStatement(consulta);
			preStatement.setString(1, miUsuario.getNombre());
			preStatement.setString(2, miUsuario.getTelefono());
			preStatement.setString(3, miUsuario.getDocumento());
			preStatement.setString(4, miUsuario.getRol());
			preStatement.setString(5, miUsuario.getCorreo());
			preStatement.setString(6, miUsuario.getContrasena());
			preStatement.execute();

			resultado = 1;

		} catch (SQLException e) {
			System.out.println("No se pudo registrar la persona: " + e.getMessage());
			resultado = 2;
		} finally {
			conexion.desconectar();
		}
		return resultado;
	}

	public Usuario ingreso(String correo, String contrasena) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		miUsuario = new Usuario();
		if (miConexion.isMysql() == true) {
			connection = miConexion.getConnection();
			String consulta = "SELECT * FROM usuarioreser WHERE emailUsu =? AND contraseniaUsu=?;";

			try {
				if (connection != null) {
					statement = connection.prepareStatement(consulta);
					statement.setString(1, correo);
					statement.setString(2, contrasena);
					result = statement.executeQuery();

					if (result.next() == true) {
						miUsuario.setRol(result.getString("rolUsu"));
						miUsuario.setDocumento(result.getString("cedulaUsu"));
						miUsuario.setNombre(result.getString("nombreUsu"));
						miUsuario.setTelefono(result.getString("telUsu"));
						miUsuario.setContrasena(result.getString("contraseniaUsu"));
						miUsuario.setCorreo(result.getString("emailUsu"));
					} else {
						System.out.println("El usuario no esta registrado");
						miUsuario = null;
					}

				}
			} catch (Exception e) {
				System.out.println("Error al consultar el usuario");
			}
		} else {
			miUsuario = null;
		}
		return miUsuario;
	}

	public List<Ambiente> listaAmbiente() {

		prepararConexion();
		ResultSet result = null;
		Ambiente miAmbiente = new Ambiente();
		List<Ambiente> listaAmbientes = new ArrayList<>();
		String consulta = "SELECT * FROM ambienteesp";
		try {
			statement = connection.prepareStatement(consulta);
			result = statement.executeQuery();
			while (result.next()) {
				miAmbiente = new Ambiente();
				miAmbiente.setCodAmbiente(result.getInt("codigoAmbEsp"));
				miAmbiente.setNombre(result.getString("nombreAmbEsp"));
				listaAmbientes.add(miAmbiente);
			}
		} catch (Exception e) {

		}
		return listaAmbientes;
	}

	public ArrayList<Usuario> obtenerListaPersonas() {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		Usuario miUsuario = new Usuario();
		ArrayList<Usuario> listaPersonas = null;

		connection = miConexion.getConnection();

		String consulta = "select * from usuarioreser where rolUsu='Instructor'";

		try {
			if (connection != null) {
				listaPersonas = new ArrayList<>();
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				while (result.next() == true) {
					miUsuario = new Usuario();
					miUsuario.setNombre(result.getString("nombreUsu"));
					miUsuario.setTelefono(result.getString("telUsu"));
					miUsuario.setDocumento(result.getString("CedulaUsu"));
					miUsuario.setCorreo(result.getString("emailUsu"));
					if (result.getString("estado").equals("activo")) {
						miUsuario.setEstadoB(true);
					} else {
						miUsuario.setEstadoB(false);
					}
					listaPersonas.add(miUsuario);
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}
		return listaPersonas;
	}

	public String eliminarPersona(Usuario usuario) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();

		String resp = "";
		try {
			String delete = "UPDATE `usuarioreser` SET estado=? WHERE `cedulaUsu`=?;";

			PreparedStatement statement = connection.prepareStatement(delete);
			if (usuario.isEstadoB()==true) {
				usuario.setEstado("desactivo");
			} else {
				usuario.setEstado("activo");
			}
			statement.setString(1, usuario.getEstado());
			statement.setString(2, usuario.getDocumento());

			statement.executeUpdate();

			resp = "Se ha eliminado exitosamente";
			statement.close();
			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			resp = "No se pudo eliminar";
		}
		return resp;
	}

	public String editarPersona(Usuario usuario) {
		String resultado = "";
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();
		try {
			String consulta = "UPDATE usuarioreser SET nombreUsu=?, telUsu=?, emailUsu=? WHERE cedulaUsu=?;";
			PreparedStatement preStatement = connection.prepareStatement(consulta);

			preStatement.setString(1, usuario.getNombre());
			preStatement.setString(2, usuario.getTelefono());
			preStatement.setString(3, usuario.getCorreo());
			preStatement.setString(4, usuario.getDocumento());
			preStatement.executeUpdate();

			resultado = "Se ha Actualizado la persona satisfactoriamente";

			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e);
			resultado = "No se pudo actualizar la persona";
		}
		return resultado;
	}

	public Usuario consultaIndividual(String documento) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		Usuario miPersona = null;

		connection = miConexion.getConnection();

		String consulta = "SELECT * FROM usuario where documento = " + documento;

		try {
			if (connection != null) {
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				if (result.next() == true) {
					miPersona = new Usuario();
					miPersona.setDocumento(result.getString("documento"));
					miPersona.setNombre(result.getString("nombre"));
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}
		return miPersona;
	}

	public int contador() {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		int a = 0;
		connection = miConexion.getConnection();

		String consulta = "SELECT COUNT(nombreUsu) FROM usuarioreser WHERE rolUsu='Instructor'";

		try {
			if (connection != null) {
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				if (result.next() == true) {
					a = result.getInt("count(nombreUsu)");
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}

		return a;
	}

	public int contadorReservas() {
		prepararConexion();
		ResultSet result = null;
		int a = 0;
		connection = miConexion.getConnection();

		String consulta = "SELECT COUNT(*) FROM reservaAmb;";

		try {
			if (connection != null) {
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				if (result.next() == true) {
					a = result.getInt("count(*)");
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}

		return a;
	}

	public int contadorAmbientes() {
		prepararConexion();
		ResultSet result = null;
		int a = 0;
		connection = miConexion.getConnection();

		String consulta = "SELECT COUNT(*) FROM ambienteesp;";

		try {
			if (connection != null) {
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				if (result.next() == true) {
					a = result.getInt("count(*)");
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}

		return a;
	}

	public ArrayList<Ambiente> obtenerListaInventario() {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		Ambiente ambiente = new Ambiente();
		ArrayList<Ambiente> listaInventario = null;

		connection = miConexion.getConnection();

		String consulta = "select * from inventario";

		try {
			if (connection != null) {
				listaInventario = new ArrayList<>();
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				while (result.next() == true) {
					ambiente = new Ambiente();
					ambiente.setCodInventario(result.getInt("codInventario"));
					ambiente.setCantidad(result.getInt("cantidad"));
					ambiente.setNombre(result.getString("objeto"));
					ambiente.setCodAmbiente(result.getInt("ambiente"));
					listaInventario.add(ambiente);
				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}
		return listaInventario;
	}

	public ArrayList<Ambiente> obtenerListaAmbiente() {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		Ambiente ambiente = new Ambiente();
		ArrayList<Ambiente> listaAmbienteAgregar = null;

		connection = miConexion.getConnection();

		String consulta = "select * from ambienteEsp";

		try {
			if (connection != null) {
				listaAmbienteAgregar = new ArrayList<>();
				statement = connection.prepareStatement(consulta);

				result = statement.executeQuery();

				while (result.next() == true) {
					ambiente = new Ambiente();
					ambiente.setNomAmbiente(result.getString("nombreAmbEsp"));
					ambiente.setCodAmbiente(result.getInt("codigoAmbEsp"));
					ambiente.setAdministradorAmb(result.getString("administradorAmbEsp"));
					listaAmbienteAgregar.add(ambiente);

				}

			}
		} catch (SQLException e) {
			System.out.println("Error en la consulta del usuario: " + e.getMessage());
		} finally {
			miConexion.desconectar();
		}
		return listaAmbienteAgregar;

	}

	public String agregarInventario(Ambiente miAmbiente) {
		String resultado = "";

		Connection connection = null;
		Conexion conexion = new Conexion();
		PreparedStatement preStatement = null;

		connection = conexion.getConnection();
		String consulta = "INSERT INTO `inventario` (`objeto`, `cantidad`, `ambiente`) " + "VALUES (?,?,?);";

		try {
			preStatement = connection.prepareStatement(consulta);
			preStatement.setString(1, miAmbiente.getNombre());
			preStatement.setInt(2, miAmbiente.getCantidad());
			preStatement.setInt(3, miAmbiente.getCodAmbiente());
			preStatement.execute();

			System.out.println(miAmbiente + "Problema 1");
			resultado = "Registrado Con exito";
			System.out.println(miAmbiente + "Problema");
		} catch (SQLException e) {
			System.out.println("No se pudo registrar la persona: " + e.getMessage());
			resultado = "No se Pudo Registrar";
		} finally {
			conexion.desconectar();
		}
		return resultado;
	}

	public String AmbienteAgregar(Ambiente miAmbienteAgregar) {
		String resultado = "";

		Connection connection = null;
		Conexion conexion = new Conexion();
		PreparedStatement preStatement = null;

		connection = conexion.getConnection();
		String consulta = "insert  into `ambienteesp`(`nombreAmbEsp`,`administradorAmbEsp`)" + "values (?,?);";

		try {
			preStatement = connection.prepareStatement(consulta);
			preStatement.setString(1, miAmbienteAgregar.getNomAmbiente());
			preStatement.setString(2, miAmbienteAgregar.getAdministradorAmb());
			preStatement.execute();

			resultado = "Registrado Con exito";

		} catch (SQLException e) {
			System.out.println("No se pudo registrar la persona: " + e.getMessage());
			resultado = "No se Pudo Registrar";
		} finally {
			conexion.desconectar();
		}
		return resultado;
	}

	public String eliminarInventario(Ambiente ambiente) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();

		String resp = "";
		try {
			String delete = "DELETE FROM inventario WHERE codInventario= ? ";

			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setInt(1, ambiente.getCodInventario());

			statement.executeUpdate();

			resp = "Se ha eliminado exitosamente";
			statement.close();
			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			resp = "No se pudo eliminar";
		}
		return resp;
	}

	public String eliminarAmbiente(Ambiente ambiente) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();

		String resp = "";
		try {
			String delete = "DELETE FROM ambienteesp WHERE codigoAmbEsp= ? ";

			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setInt(1, ambiente.getCodAmbiente());

			statement.executeUpdate();

			resp = "Se ha eliminado exitosamente";
			statement.close();
			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			resp = "No se pudo eliminar";
		}
		return resp;
	}

	public String editarInventario(Ambiente ambiente) {
		String resultado = "";
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();
		try {
			String consulta = "UPDATE inventario SET objeto=?, cantidad=? WHERE codInventario=?;";
			PreparedStatement preStatement = connection.prepareStatement(consulta);

			preStatement.setString(1, ambiente.getNombre());
			preStatement.setInt(2, ambiente.getCantidad());
			preStatement.setInt(3, ambiente.getCodInventario());
			preStatement.executeUpdate();

			resultado = "Se ha Actualizado la persona satisfactoriamente";

			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e);
			resultado = "No se pudo actualizar la persona";
		}
		return resultado;
	}

	public String editarAmbientes(Ambiente ambiente) {
		String resultado = "";
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();
		try {
			String consulta = "UPDATE ambienteesp SET nombreAmbEsp=?, administradorAmbEsp=? WHERE codigoAmbEsp=?;";
			PreparedStatement preStatement = connection.prepareStatement(consulta);

			preStatement.setString(1, ambiente.getNomAmbiente());
			preStatement.setString(2, ambiente.getAdministradorAmb());
			preStatement.setInt(3, ambiente.getCodAmbiente());
			preStatement.executeUpdate();

			resultado = "Se ha Actualizado la persona satisfactoriamente";

			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e);
			resultado = "No se pudo actualizar la persona";
		}
		return resultado;
	}

	public List<String> obtenerListaRol(int a) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		PreparedStatement statement = null;
		ResultSet result = null;

		ArrayList<String> listaRol = null;

		connection = miConexion.getConnection();
		String consulta;
		switch (a) {
		case 1:
			consulta = "SELECT DISTINCT rolUsu FROM usuarioreser;";
			try {
				if (connection != null) {
					listaRol = new ArrayList<>();
					statement = connection.prepareStatement(consulta);

					result = statement.executeQuery();
					listaRol.add("Seleccion el rol");
					while (result.next() == true) {
						listaRol.add(result.getString("rolUsu"));
					}

				}
			} catch (SQLException e) {
				System.out.println("Error en la consulta del usuario: " + e.getMessage());
			} finally {
				miConexion.desconectar();
			}
			break;
		case 2:
			consulta = "SELECT nombreUsu FROM usuarioreser WHERE rolUsu='Administrador';";
			try {
				if (connection != null) {
					listaRol = new ArrayList<>();
					statement = connection.prepareStatement(consulta);

					result = statement.executeQuery();
					listaRol.add("Seleccion el Administrador");
					while (result.next() == true) {
						listaRol.add(result.getString("nombreUsu"));
					}

				}
			} catch (SQLException e) {
				System.out.println("Error en la consulta del usuario: " + e.getMessage());
			} finally {
				miConexion.desconectar();
			}
			break;
		}

		return listaRol;
	}

	public void agregarObservacion(Usuario miUsuario, Ambiente miAmbiente) {
		Connection connection = null;
		Conexion conexion = new Conexion();
		PreparedStatement preStatement = null;

		connection = conexion.getConnection();
		String consulta = "INSERT INTO `informe` (`tipoInforme`, `destinatario`, `destino`, `descripcion`, `reserva`) "
				+ "VALUES (?,?,?,?,?);";

		try {
			preStatement = connection.prepareStatement(consulta);
			preStatement.setInt(1, 1);
			preStatement.setString(2, miUsuario.getDocumento());
			preStatement.setString(3, miAmbiente.getAdministradorAmb());
			preStatement.setString(4, miAmbiente.getDescripcion());
			preStatement.setInt(5, 1);
			preStatement.execute();
			System.out.println("Bien");

		} catch (SQLException e) {
			System.out.println("No se pudo registrar la persona: " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
	}

	public void cambiarContra(Usuario miUsuario) {
		Connection connection = null;
		Conexion miConexion = new Conexion();
		connection = miConexion.getConnection();
		try {
			String consulta = "UPDATE `usuarioreser` SET `contraseniaUsu`=? WHERE `cedulaUsu`=?;";
			PreparedStatement preStatement = connection.prepareStatement(consulta);

			preStatement.setString(1, miUsuario.getContrasena());
			preStatement.setString(2, miUsuario.getDocumento());
			preStatement.executeUpdate();

			System.out.println("Se ha Actualizado la persona satisfactoriamente");

			miConexion.desconectar();

		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("No se pudo actualizar la persona");
		}
	}

	public ArrayList<InformeVo> verNotificaciones(int campo) {
		ArrayList<InformeVo> noti = new ArrayList<>();
		if (miConexion.isMysql() == true) {
			prepararConexion();
			String consulta = "SELECT DISTINCT * from informe, tipoInforme where destino =? and tipoInforme = ? group by nis;";
			ResultSet result = null;
			try {
				if (connection != null) {
					statement = connection.prepareStatement(consulta);
					statement.setString(1, miUsuario.getDocumento());
					statement.setInt(2, campo);
					result = statement.executeQuery();
					while (result.next() == true) {
						noti.add(new InformeVo(result.getInt("nis"), result.getInt("tipoInforme"),
								result.getString("destinatario"), result.getString("destino"),
								result.getString("descripcion"), result.getInt("reserva")));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return noti;
	}

	public String darDato(String hora) {
		pos = 0;
		String resp = crearCadena(hora);
		return resp;
	}

	public String crearCadena(String cadena) {
		String nuevaCad = "";
		System.out.println("Inicia pos :" + pos);
		for (int i = pos; i < cadena.length(); i++) {

			if (cadena.charAt(i) != ':') {
				nuevaCad += cadena.charAt(i);
			} else {
				pos = i + 1;
				System.out.println("Pos fin :" + pos);
				break;
			}

		}
		return nuevaCad;
	}

	public String reservar(String horaEntrada, String horaSalida, String motivo, int ambiente, String fecha) {
		String resultado = "";
		tabla = "reservaamb";
		tablaHora = "horarioreser";
		int codigoReser = 0;
		ResultSet result = null;
		System.out.println(miUsuario.getRol());
		if (miUsuario.getRol().equals("Instructor")) {
			tabla = "solicitudreserva";
			tablaHora = "horarioSolicitud";
		}
		prepararConexion();
		String consulta = "INSERT INTO " + tabla
				+ " (UsuarioReser, fechaReser, horaLlegadaReser, motivoReser, ambienteReser, horaSalidaReser) VALUES(?,?,?,?,?,?)";
		try {
			if (connection != null) {
				statement = connection.prepareStatement(consulta);
				statement.setString(1, miUsuario.getDocumento());
				statement.setString(2, fecha);
				statement.setString(3, horaEntrada);
				statement.setString(4, motivo);
				statement.setInt(5, ambiente);
				statement.setString(6, horaSalida);
				statement.execute();

				resultado = "ok";

				consulta = "select * from " + tabla + " where UsuarioReser=?" + " and fechaReser=?"
						+ " and horaLlegadaReser=?" + " and motivoReser=?" + " and ambienteReser=?"
						+ " and horaSalidaReser=?;";

				if (connection != null) {
					statement = connection.prepareStatement(consulta);
					statement.setString(1, miUsuario.getDocumento());
					statement.setString(2, fecha);
					statement.setString(3, horaEntrada);
					statement.setString(4, motivo);
					statement.setInt(5, ambiente);
					statement.setString(6, horaSalida);
					result = statement.executeQuery();
					pos = 0;
					cad1Entrada = crearCadena(horaEntrada);
					System.out.println(cad1Entrada);
					cad2Entrada = crearCadena(horaEntrada);
					pos = 0;
					cad1Salida = crearCadena(horaSalida);
					// cad2Entrada=crearCadena(horaSalida);

					horaS = Integer.parseInt(cad1Salida);
					System.out.println("horaSalida" + horaS);
					// int minutoS= Integer.parseInt(cad2Salida);
					horaE = Integer.parseInt(cad1Entrada);
					// int minutoE = Integer.parseInt(cad2Entrada);
					int diferencia = horaS - horaE;
					ArrayList<String> tiempos = new ArrayList<>();
					for (int i = 0; i < (diferencia + 1); i++) {
						tiempos.add((Integer.parseInt(cad1Entrada) + i) + ":");

					}

					if (result.next() == true) {
						codigoReser = result.getInt("codReser");

						consulta = "INSERT INTO " + tablaHora + " (reserva, hora) VALUES(?,?)";
						for (int i = 0; i < tiempos.size(); i++) {

							if (connection != null) {
								statement = connection.prepareStatement(consulta);
								statement.setInt(1, codigoReser);
								if (i == (tiempos.size() - 1)) {
									statement.setString(2, tiempos.get(i) + cad2Entrada);
								} else {
									statement.setString(2, tiempos.get(i) + cad2Entrada);
								}
								statement.execute();
								resultado = "ok";

							}

						}

					}
				}

				if (tabla.equals("solicitudreserva")) {

					String admi = "";
					String nombreAmbiente = "";

					consulta = "SELECT administradorAmbEsp, nombreAmbEsp from ambienteEsp where codigoAmbEsp = ? ";

					statement = connection.prepareStatement(consulta);
					statement.setInt(1, ambiente);
					result = statement.executeQuery();
					if (result.next() == true) {
						admi = result.getString("administradorAmbEsp");
						nombreAmbiente = result.getString("nombreAmbEsp");
					}

					consulta = "INSERT INTO informe (tipoInforme, destinatario, destino, descripcion, reserva)"
							+ "VALUES(?,?,?,?,?)";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, 1);
					statement.setString(2, miUsuario.getDocumento());
					statement.setString(3, admi);
					statement.setString(4,
							"El instructor " + miUsuario.getNombre()
									+ " hizo una solicitud de reserva para el ambiente " + nombreAmbiente
									+ ". Por favor, responda a su solicitud.");
					statement.setInt(5, codigoReser);
					statement.execute();

					resultado = "ok";
				}
			}
		} catch (Exception e) {
			System.out.println("No se pudo registrar el dato: " + e.getMessage());
			resultado = "error";
		}
		System.out.println(resultado);
		miConexion.desconectar();

		return resultado;
	}

	public int validarFecha(String fecha, int suma) {
		prepararConexion();
		ResultSet result = null;
		int resp = 0;
		String consulta = "SELECT DATE_ADD(CURDATE(), INTERVAL " + suma + " DAY)<=? AS respuesta;";
		try {
			statement = connection.prepareStatement(consulta);
			statement.setString(1, fecha);
			result = statement.executeQuery();
			if (result.next()) {
				resp = result.getInt("respuesta");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public void prepararConexion() {
		connection = null;
		miConexion = new Conexion();
		statement = null;

		connection = miConexion.getConnection();
	}

	public ArrayList<CronogramaVo> verCronograma(String fecha, int ambiente) {
		reservasEnRango = new ArrayList<>();
		ArrayList<CronogramaVo> disponibilidadCron = new ArrayList<>();
		for (int i = 0; i < 17; i++) {
			disponibilidadCron.add(disponibilidadCronograma((6 + i) + ":00", fecha, ambiente));
		}

		return disponibilidadCron;
	}

	public CronogramaVo disponibilidadCronograma(String hora, String fecha, int ambiente) {
		prepararConexion();
		int reserva = 0;
		String accion = "Detalles";
		String disponibilidad = "Disponible";
		CronogramaVo miCronograma = new CronogramaVo();
		ResultSet result = null;
		String consulta = "SELECT * FROM horariosolicitud, solicitudreserva WHERE hora=? AND reserva = codReser AND fechaReser=? AND ambienteReser = ?";
		try {
			statement = connection.prepareStatement(consulta);
			statement.setString(1, hora);
			statement.setString(2, fecha);
			statement.setInt(3, ambiente);
			result = statement.executeQuery();
			if (result.next()) {
				reservasEnRango.add(new ReservaVo(result.getInt("codReser"), result.getString("usuarioReser"),
						result.getString("fechaReser"), result.getString("HoraLlegadaReser"),
						result.getString("HoraSalidaReser"), result.getInt("ambienteReser"),
						result.getString("motivoReser")));

				disponibilidad = "Solicitado.";
				reserva = result.getInt("codReser");
			} else {
				consulta = "SELECT * FROM horarioReser, reservaAmb WHERE hora=? AND reserva = codReser AND fechaReser=?  AND ambienteReser = ?";
				statement = connection.prepareStatement(consulta);
				statement.setString(1, hora);
				statement.setString(2, fecha);
				statement.setInt(3, ambiente);
				result = statement.executeQuery();
				if (result.next()) {
					reservasEnRango.add(new ReservaVo(result.getInt("codReser"), result.getString("usuarioReser"),
							result.getString("fechaReser"), result.getString("HoraLlegadaReser"),
							result.getString("HoraSalidaReser"), result.getInt("ambienteReser"),
							result.getString("motivoReser")));
					if (result.getString("usuarioReser").equals(miUsuario.getDocumento())) {
						disponibilidad = "Reservado por mi";
						accion = "Eliminar mi reserva";
					} else {
						disponibilidad = "Reservado.";
					}

					reserva = result.getInt("codReser");
				} else {
					accion = "Reservar";
				}
			}
			for (int i = 0; i < reservasEnRango.size(); i++) {
				System.out.println("Codigo " + reservasEnRango.get(i).getCodigo());
				System.out.println("Usuario " + reservasEnRango.get(i).getCedulaUsuario());
				System.out.println("fecha " + reservasEnRango.get(i).getFecha());
				System.out.println("Hora entrada" + reservasEnRango.get(i).getHoraLlegada());
				System.out.println("Hora salida " + reservasEnRango.get(i).getHoraSalida());
				System.out.println("ambiente " + reservasEnRango.get(i).getAmbiente());
				System.out.println("Motivo " + reservasEnRango.get(i).getMotivo());
			}
			miCronograma.setHora(hora);
			miCronograma.setDisponibilidad(disponibilidad);
			miCronograma.setAccion(accion);
			miCronograma.setReserva(reserva);

		} catch (Exception e) {
			System.out.println("No se encontrar el dato: " + e.getMessage());
			e.printStackTrace();
		}
		return miCronograma;
	}

	@SuppressWarnings("resource")
	public void reservarVideoconferencia(String horaEntrada, String horaSalida, String fecha, int ambiente) {
		reservasEnHora = new ArrayList<>();
		System.out.println("1");
		if (!(verDiponibilidad(fecha, horaSalida, horaEntrada, 5, "horarioSolicitud") == true)
				|| !(verDiponibilidad(fecha, horaSalida, horaEntrada, 5, "horarioReser") == true)) {
			String consulta = "";
			String fechaC = "";
			String correo = "";
			String horaE = "";
			String horaS = "";
			ResultSet result = null;
			String usuario = "";
			try {
				System.out.println("2");
				for (int i = 0; i < reservasEnHora.size(); i++) {
					consulta = "select * from reservaAmb where codReser=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					result = statement.executeQuery();
					if (result.next()) {
						System.out.println("3");
						usuario = result.getString("usuarioReser");
						fechaC = result.getString("fechaReser");
						horaE = result.getString("horaLlegadaReser");
						horaS = result.getString("horaSalidaReser");
						consulta = "SELECT * FROM usuarioReser where cedulaUsu = ?";
						statement = connection.prepareStatement(consulta);
						statement.setString(1, usuario);
						result = statement.executeQuery();
						if (result.next()) {
							correo = result.getString("emailUsu");
						}
					} else {
						System.out.println("4");
						consulta = "select * from solicitudReserva where codReser=?";
						statement = connection.prepareStatement(consulta);
						statement.setInt(1, reservasEnHora.get(i).getCodigo());
						result = statement.executeQuery();
						if (result.next()) {
							usuario = result.getString("usuarioReser");
							fechaC = result.getString("fechaReser");
							horaE = result.getString("horaLlegadaReser");
							horaS = result.getString("horaSalidaReser");
						}
					}
					System.out.println("5");
					consulta = "delete from solicitudReserva where codReser=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					statement.execute();
					System.out.println("6");
					consulta = "delete from horarioSolicitud where reserva=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					statement.execute();
					System.out.println("7");
					consulta = "delete from reservaAmb where codReser=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					statement.execute();
					System.out.println("8");
					consulta = "delete from horarioReser where reserva=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					statement.execute();
					System.out.println("10");
					consulta = "delete from informe where reserva=?";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, reservasEnHora.get(i).getCodigo());
					statement.execute();
					System.out.println("10");
					consulta = "INSERT INTO informe (tipoInforme, destinatario, destino, descripcion, reserva)"
							+ "VALUES(?,?,?,?,?)";
					statement = connection.prepareStatement(consulta);
					statement.setInt(1, 1);
					statement.setString(2, miUsuario.getDocumento());
					statement.setString(3, usuario);
					statement.setString(4, "La reserva para el " + fechaC + " desde " + horaE + " hasta " + horaS
							+ " fue eliminada por motivo de una videoconferencia," + " disculpe la molestia");
					statement.setInt(5, reservasEnHora.get(i).getCodigo());
					statement.execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reservar(horaEntrada, horaSalida, "Videoconferencia", ambiente, fecha);
	}

	ArrayList<ReservaVo> reservasEnHora;

	public boolean verDiponibilidad(String fecha, String horaSalida, String horaEntrada, int ambiente, String tabla) {
		String hora = "";
		int horaEntradaR;
		int horaSalidaR;
		boolean disponibilidad = true;

		for (int j = 0; j < reservasEnRango.size(); j++) {
			pos = 0;
			hora = crearCadena(reservasEnRango.get(j).getHoraLlegada());
			System.out.println("hora en string" + hora);
			horaEntradaR = Integer.parseInt(hora);
			System.out.println("hora en int" + horaEntradaR);
			pos = 0;
			hora = crearCadena(reservasEnRango.get(j).getHoraSalida());
			horaSalidaR = Integer.parseInt(hora);
			pos = 0;
			cad1Entrada = crearCadena(horaEntrada);
			System.out.println(horaSalida);
			horaE = Integer.parseInt(cad1Entrada);

			pos = 0;
			cad1Salida = crearCadena(horaSalida);

			horaS = Integer.parseInt(cad1Salida);

			System.out.println("horaNum" + horaEntradaR);
			System.out.println("horaEntrada" + cad1Entrada);
			System.out.println("horaSalida" + cad1Salida);
			if (((horaEntradaR >= horaE) && (horaEntradaR <= horaS))
					|| (horaSalidaR >= horaE) && (horaSalidaR <= horaS)) {
				disponibilidad = false;
				reservasEnHora.add(reservasEnRango.get(j));

			}
		}

		return disponibilidad;

	}

	public boolean consultar(String fecha, String horaSalida, String horaEntrada, int ambiente) {
		prepararConexion();
		reservasEnHora = new ArrayList<>();
		boolean disponibilidad = true;
		disponibilidadReserva = verDiponibilidad(fecha, horaSalida, horaEntrada, ambiente, "horarioReser");
		disponibilidadSolicitud = verDiponibilidad(fecha, horaSalida, horaEntrada, ambiente, "horarioSolicitud");
		if (disponibilidadReserva == false) {
			disponibilidad = false;
		}

		if (disponibilidadSolicitud == false) {
			disponibilidad = false;
		}
		System.out.println(disponibilidadReserva);
		System.out.println(disponibilidadSolicitud);
		System.out.println(disponibilidad);

		return disponibilidad;
	}

	public String mensajeVerificarOtro() {
		String parteResp = "";
		if (reservasEnRango.size() == 1) {
			if (miUsuario.getRol() == "Instructor") {
				parteResp = " solicitud";
			} else {
				parteResp = " reserva";
			}
			resp = "En este horario hay una reserva. No es posible hacer la" + parteResp;
		} else {
			resp = "En este horario hay " + reservasEnRango.size() + " reservas. No es posible hacer la" + parteResp;
		}
		return resp;
	}

	public void aceptado(String usuario, String fecha, String horaEntrada, String horaSalida, String motivo,
			int ambiente) {
		String consulta = "INSERT INTO reservaAmb(UsuarioReser, fechaReser, horaLlegadaReser, motivoReser, ambienteReser, horaSalidaReser) VALUES(?,?,?,?,?,?)";
		ResultSet result = null;
		int codigoReser;
		try {
			statement = connection.prepareStatement(consulta);
			statement.setString(1, usuario);
			statement.setString(2, fecha);
			statement.setString(3, horaEntrada);
			statement.setString(4, motivo);
			statement.setInt(5, ambiente);
			statement.setString(6, horaSalida);
			statement.execute();
			consulta = "select * from reservaAmb where UsuarioReser=?" + " and fechaReser=?" + " and horaLlegadaReser=?"
					+ " and motivoReser=?" + " and ambienteReser=?" + " and horaSalidaReser=?;";
			statement = connection.prepareStatement(consulta);
			statement.setString(1, usuario);
			statement.setString(2, fecha);
			statement.setString(3, horaEntrada);
			statement.setString(4, motivo);
			statement.setInt(5, ambiente);
			statement.setString(6, horaSalida);
			result = statement.executeQuery();

			pos = 0;
			cad1Entrada = crearCadena(horaEntrada);
			System.out.println(cad1Entrada);
			cad2Entrada = crearCadena(horaEntrada);
			pos = 0;
			cad1Salida = crearCadena(horaSalida);
			// cad2Entrada=crearCadena(horaSalida);

			horaS = Integer.parseInt(cad1Salida);
			System.out.println("horaSalida" + horaS);
			// int minutoS= Integer.parseInt(cad2Salida);
			horaE = Integer.parseInt(cad1Entrada);
			// int minutoE = Integer.parseInt(cad2Entrada);
			int diferencia = horaS - horaE;
			ArrayList<String> tiempos = new ArrayList<>();
			for (int i = 0; i < (diferencia + 1); i++) {
				tiempos.add((Integer.parseInt(cad1Entrada) + i) + ":");

			}

			if (result.next() == true) {
				codigoReser = result.getInt("codReser");

				consulta = "INSERT INTO horarioReser (reserva, hora) VALUES(?,?)";
				for (int i = 0; i < tiempos.size(); i++) {

					if (connection != null) {
						statement = connection.prepareStatement(consulta);
						statement.setInt(1, codigoReser);
						if (i == (tiempos.size() - 1)) {
							statement.setString(2, tiempos.get(i) + cad2Entrada);
						} else {
							statement.setString(2, tiempos.get(i) + cad2Entrada);
						}
						statement.execute();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void RespuestaSolicitud(int reserva, String respuesta, int informe) {
		String usuario = "";
		String fecha = "";
		String horaEntrada = "";
		String horaSalida = "";
		String motivo = "";
		int ambiente = 0;
		String correo = "";
		String descripcion = "rechazada";
		prepararConexion();
		ResultSet result = null;
		String consulta = "SELECT * FROM solicitudReserva where codReser=?";
		try {
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, reserva);
			result = statement.executeQuery();
			if (result.next()) {

				usuario = result.getString("UsuarioReser");
				fecha = result.getString("fechaReser");
				horaEntrada = result.getString("horaLlegadaReser");
				motivo = result.getString("motivoReser");
				ambiente = result.getInt("ambienteReser");
				horaSalida = result.getString("horaSalidaReser");
			}
			consulta = "SELECT * FROM usuarioReser where cedulaUsu = ?";
			statement = connection.prepareStatement(consulta);
			statement.setString(1, usuario);
			result = statement.executeQuery();
			if (result.next()) {
				miUsuario.setCorreo(result.getString("emailUsu"));
			}
			if (respuesta.equals("aceptada")) {
				descripcion = "aceptada";
				aceptado(usuario, fecha, horaEntrada, horaSalida, motivo, ambiente);
			}
			consulta = "delete from solicitudReserva where codReser=?";
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, reserva);
			statement.execute();
			consulta = "delete from horarioSolicitud where reserva=?";
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, reserva);
			statement.execute();
			consulta = "delete from informe where nis=?";
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, informe);
			statement.execute();
			consulta = "INSERT INTO informe (tipoInforme, destinatario, destino, descripcion, reserva)"
					+ "VALUES(?,?,?,?,?)";
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, 1);
			statement.setString(2, miUsuario.getDocumento());
			statement.setString(3, usuario);
			statement.setString(4, "La solicitud de reserva para el " + fecha + " desde " + horaEntrada + " hasta "
					+ horaSalida + " fue " + descripcion);
			statement.setInt(5, reserva);
			statement.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public String detalles(int reserva, String tabla) {
		prepararConexion();
		ResultSet result = null;
		String mensaje = "";
		String consulta = "SELECT * FROM " + tabla + " where codReser=?";
		try {
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, reserva);
			result = statement.executeQuery();
			if (result.next()) {
				mensaje = "Motivo = " + result.getString("motivoReser") + "\nUsuario = "
						+ result.getString("usuarioReser");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mensaje;
	}

	public String verificarUsuario() {

		if (miUsuario.getRol() == "Mesa de ayuda") {
			resp = "Mesa de ayuda";
		} else {
			if (miUsuario.getRol() == "Instructor") {
				resp = "Mesa de ayuda";
			} else {
				resp = "Administrador";
			}
		}
		return resp;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public ArrayList<ReservaVo> verMisReservas() {
		prepararConexion();
		ArrayList<ReservaVo> misReservas = new ArrayList<>();
		ResultSet result = null;
		if (miConexion.isMysql() == true) {
			String consulta = "SELECT * FROM reservaamb where usuarioReser = ?";
			try {
				statement = connection.prepareStatement(consulta);
				statement.setString(1, miUsuario.getDocumento());
				result = statement.executeQuery();
				while (result.next()) {
					misReservas.add(new ReservaVo(result.getInt("codReser"), "", result.getString("fechaReser"),
							result.getString("horaLlegadaReser"), result.getString("horaSalidaReser"),
							result.getInt("ambienteReser"), result.getString("motivoReser")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return misReservas;
	}

	public void eliminarMiReserva(int codigo) {
		prepararConexion();
		String consulta = "delete FROM reservaamb where codReser = ?";
		try {
			statement = connection.prepareStatement(consulta);
			statement.setInt(1, codigo);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
