package bean;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dao.UsuarioDao;
import vo.Ambiente;
import vo.CronogramaVo;
import vo.InformeVo;
import vo.Usuario;

@ManagedBean
@SessionScoped
public class IngresoBean {

	private boolean instructor=false;
	private String nombreAmbiente;
	private List<Ambiente> listaAmbiente;
	private List<String> ambientes;
	private boolean notificaciones= false;
	private boolean observacion=false;
	private boolean administrador;
	private boolean visible= false;
	private String correo;
	private String contrasena;
	private String nuevaContra;
	private String respuesta;
	private Usuario miUsuario;
	private Ambiente miAmbiente;
	private String res;
	UsuarioDao miUsuarioDao=new UsuarioDao();
	private ArrayList<InformeVo> observaciones;
	private ArrayList<InformeVo> notificacion;
	
	public IngresoBean() {
		miUsuario = new Usuario();
		miAmbiente = new Ambiente();
	}
	
	public String Ingreso(){
		miUsuario=miUsuarioDao.ingreso(correo,contrasena);
		listaAmbiente=miUsuarioDao.listaAmbiente();
		ambientes=new ArrayList<>();
		for (int i = 0; i < listaAmbiente.size(); i++) {
			ambientes.add(listaAmbiente.get(i).getNombre());
		}
		setObservaciones(miUsuarioDao.verNotificaciones(2));
		setNotificacion(miUsuarioDao.verNotificaciones(1));
		res="";
		if (miUsuario != null) {
		if(miUsuario.getRol().equals("Administrador")) {
			System.out.println("Administrador");
			res = "homeAdmin.jsf";
			administrador=true;
			mesaAyuda=false;
			instructor=false;
		}else{
			if (miUsuario.getRol().equals("Mesa de ayuda")) {
				System.out.println("Mesa de ayuda");
				res="homeInstru.jsf";
				mesaAyuda=true;
				administrador=false;
				instructor=false;
			} else {
				if (miUsuario.getRol().equals("Instructor")) {
					res = "homeInstru.jsf?faces-redirect=true";
					System.out.println("Instructor");
					instructor=true;
					mesaAyuda=false;
					administrador=false;
				} else {
					if (miUsuario.getRol().equals("0")) {
						System.out.println("Usuario no registrado 1");
						res = "index.jsf";
					}
				}
			}
		}
		} else {
			res = "index.jsf";
		}
		
		return res;
	}

	public String enlacePagina(int a) {
		switch (a) {
		case 1:
			res = Ingreso();
			break;
		case 2:
			res = "actualizarDatos.jsf?faces-redirect=true";
			break;
		}
		return res;
	}
	
	public String observaciones() {
		System.out.println(miAmbiente.getAdministradorAmb());
		miUsuarioDao.agregarObservacion(miUsuario, miAmbiente);
		miAmbiente = new Ambiente();
		return enlacePagina(1);
	}
	
	public String cambiarContra() {
		if (nuevaContra.equals(contrasena)) {
			if (miUsuario.getContrasena().equals(miUsuario.getRepcontrasena())) {
				miUsuarioDao.cambiarContra(miUsuario);
				contrasena=miUsuario.getContrasena();
				res=enlacePagina(2);
			} else {
				System.out.println("Deben Concidir");
				res="actualizarDatos.jsf";
			}
		} else {
			System.out.println("La contraseña no es la actual");
			res="actualizarDatos.jsf";
		}
		nuevaContra=null;
		return res;
	}
	
	public String actualizarDatos() {
		miUsuarioDao.editarPersona(miUsuario);
		return enlacePagina(2);
	}
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public Usuario getMiUsuario() {
		return miUsuario;
	}

	public void setMiUsuario(Usuario miUsuario) {
		this.miUsuario = miUsuario;
	}
	
	public Ambiente getMiAmbiente() {
		return miAmbiente;
	}

	public void setMiAmbiente(Ambiente miAmbiente) {
		this.miAmbiente = miAmbiente;
	}
	
	public String getNuevaContra() {
		return nuevaContra;
	}

	public void setNuevaContra(String nuevaContra) {
		this.nuevaContra = nuevaContra;
	}
	
	public ArrayList<InformeVo> getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(ArrayList<InformeVo> observaciones) {
		this.observaciones = observaciones;
	}

	public ArrayList<InformeVo> getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(ArrayList<InformeVo> notificacion) {
		this.notificacion = notificacion;
	}
	
	// **************************************************************************

		private boolean mesaAyuda=false;
		private boolean detalles=false;
		private String fecha;
		private String horaEntrada;
		private String horaSalida;
		private String motivo;
		private int ambiente;
		private String mensaje;
		String cad1;
		String cad2;
		boolean bien = true;
		private ArrayList<CronogramaVo> cronograma;

		public void activarNotificaciones(){
			if(notificaciones==false){
				notificaciones=true;
			}else{
				notificaciones=false;
			}
			
		}
		
		public void activarObservaciones(){
			if(observacion==false){
				observacion=true;
			}else{
				observacion=false;
			}
			
		}
		
		public String cambiarHora(String hora) {
			switch (hora) {
			case "06:00AM":
				hora = "06:00";
				bien = true;
				break;
			case "07:00AM":
				hora = "07:00";
				bien = true;
				break;
			case "08:00AM":
				hora = "08:00";
				bien = true;
				break;
			case "09:00AM":
				hora = "09:00";
				bien = true;
				break;
			case "10:00AM":
				hora = "10:00";
				bien = true;
				break;
			case "11:00AM":
				hora = "11:00";
				bien = true;
				break;
			case "12:00AM":
				hora = "12:00";
				bien = true;
				break;
			case "01:00PM":
				hora = "13:00";
				bien = true;
				break;
			case "02:00PM":
				hora = "14:00";
				bien = true;
				break;
			case "03:00PM":
				hora = "15:00";
				bien = true;
				break;
			case "04:00PM":
				hora = "16:00";
				bien = true;
				break;
			case "05:00PM":
				hora = "17:00";
				bien = true;
				break;
			case "06:00PM":
				hora = "18:00";
				bien = true;
				break;
			case "07:00PM":
				hora = "19:00";
				bien = true;
				break;
			case "08:00PM":
				hora = "20:00";
				bien = true;
				break;
			case "09:00PM":
				hora = "21:00";
				bien = true;
				break;
			case "10:00PM":
				hora = "22:00";
				bien = true;
				break;
			default:
				miUsuarioDao.setPos(3);
				if (!(miUsuarioDao.crearCadena(hora).equals("00"))) {
					mensaje = "La hora debe ser en punto";
				} else {
					mensaje = "La hora debe estar entre las 6:00AM y las 10:00PM";
				}
				bien = false;
				break;
			}
			return hora;
		}

		public void reservar() {
			horaEntrada = cambiarHora(horaEntrada);
			horaSalida = cambiarHora(horaSalida);
			if (bien == true) {
				if (horaEntrada.equals(horaSalida)) {
					mensaje = "Por favor ingrese una hora diferente a la hora de entrada";
				} else {
					int hora1 = Integer.parseInt(miUsuarioDao.darDato(horaEntrada));
					int hora2 = Integer.parseInt(miUsuarioDao.darDato(horaSalida));
					if (hora1 < hora2) {
						if (miUsuarioDao.consultar(fecha, horaSalida, horaEntrada, ambiente)==true) {
							miUsuarioDao.reservar(horaEntrada, horaSalida, motivo, ambiente, fecha);
							cronograma=miUsuarioDao.verCronograma(fecha, ambiente);
							String rol = (miUsuarioDao.verificarUsuario());
							horaEntrada="";
							horaSalida="";
							motivo="";
							if (rol.equalsIgnoreCase("Instructor")) {
								mensaje="Solicitud enviada \n Cuando sea respondida le enviaremos una notificacion con la respuesta";
							}else{
								mensaje="Reserva exitosa";
							}
						} else {
								mensaje = miUsuarioDao.mensajeVerificarOtro();
							System.out.print(mensaje);
						}
					} else {
						mensaje = "La hora de salida no puede ser menor a la hora de entrada. Por favor cambiela.";
					}

				}
				
			}
		}

		public void accionar(){
			if(visible==false){
				visible= true;
			}else{
				visible= false;
			}
		}
		@SuppressWarnings("deprecation")
		public String hacerCronograma(){
			if(mesaAyuda==false){
				for (int j = 0; j < listaAmbiente.size(); j++) {
					if(nombreAmbiente.equals(listaAmbiente.get(j).getNombre())){
						ambiente=listaAmbiente.get(j).getCodAmbiente();
				}
				
			}
			}else{
				for (int i = 0; i < listaAmbiente.size(); i++) {
					if(listaAmbiente.get(i).getNombre().equalsIgnoreCase("Audiovisual 1")){
						ambiente=listaAmbiente.get(i).getCodAmbiente();
					}
				}
			}
			int resp=0;
			String direccion="";
			if(mesaAyuda==true){
				resp=miUsuarioDao.validarFecha(fecha, 0);
			}else{
				resp=miUsuarioDao.validarFecha(fecha,3);
			}
			if(resp==1){
				cronograma= new ArrayList<>();
				cronograma=miUsuarioDao.verCronograma(fecha, ambiente);
				direccion="index2.jsf";
			}else{
				mensaje="La fecha es incorrecta, recuerde que la reserva debe de hacerse con tres días de anticipación";
				direccion="";
			}
			return direccion;
			
		}
		
		public String accionCronogramaEliminarYReservar(String hora){
			System.out.println("Entra");
			horaEntrada=cambiarHora(hora);
			System.out.println("Hora: "+hora);
			return "mesaAyuda.jsf";
		}
		
		public void accionCronogramaReserva(String hora){
				horaEntrada=hora;
		}
		
		public void accionCronogramaDetalles(int reserva, String descripcion){
			if(descripcion.equals("Reservado")){
				mensaje=miUsuarioDao.detalles(reserva, "solicitudReserva");
			}else{
				mensaje=miUsuarioDao.detalles(reserva, "reservaAmb");
			}
			
		}
		
		public void aceptarSolicitud(int reserva, int informe){
			//FacesContext facesContext = FacesContext.getCurrentInstance();
			//Map params= facesContext.getExternalContext().getRequestParameterMap();
			System.out.println(reserva);
			System.out.println(informe);
			//int reserva= Integer.parseInt((String) params.get("reserva"));
			//int informe= Integer.parseInt((String) params.get("informe"));
			miUsuarioDao.RespuestaSolicitud(reserva, "aceptada", informe);
		}
		
		public void rechazarSolicitud(){
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Map params= facesContext.getExternalContext().getRequestParameterMap();
			
			int reserva= Integer.parseInt((String) params.get("reserva"));
			int informe= Integer.parseInt((String) params.get("informe"));
			miUsuarioDao.RespuestaSolicitud(reserva, "rechazada", informe);
		}
		
		public void eliminarYReservar(){
			horaSalida=cambiarHora(horaSalida);
			System.out.println("Hora Salida" + horaSalida);
			miUsuarioDao.reservarVideoconferencia(horaEntrada, horaSalida, fecha);
		}
		
		public String getFecha() {
			return fecha;
		}

		public void setFecha(String fecha) {
			this.fecha = fecha;
		}

		public String getHoraEntrada() {
			return horaEntrada;
		}

		public void setHoraEntrada(String horaEntrada) {
			this.horaEntrada = horaEntrada;
		}

		public String getHoraSalida() {
			return horaSalida;
		}

		public void setHoraSalida(String horaSalida) {
			this.horaSalida = horaSalida;
		}

		public String getMotivo() {
			return motivo;
		}

		public void setMotivo(String motivo) {
			this.motivo = motivo;
		}

		public int getAmbiente() {
			return ambiente;
		}

		public void setAmbiente(int ambiente) {
			this.ambiente = ambiente;
		}

		public String getMensaje() {
			return mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}

		public ArrayList<CronogramaVo> getCronograma() {
			return cronograma;
		}

		public void setCronograma(ArrayList<CronogramaVo> cronograma) {
			this.cronograma = cronograma;
		}

		public boolean isMesaAyuda() {
			return mesaAyuda;
		}

		public void setMesaAyuda(boolean mesaAyuda) {
			this.mesaAyuda = mesaAyuda;
		}

		public boolean isDetalles() {
			return detalles;
		}

		public void setDetalles(boolean detalles) {
			this.detalles = detalles;
		}

		public boolean isAdministrador() {
			return administrador;
		}

		public void setAdministrador(boolean administrador) {
			this.administrador = administrador;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public boolean isNotificaciones() {
			return notificaciones;
		}

		public void setNotificaciones(boolean notificaciones) {
			this.notificaciones = notificaciones;
		}

		public boolean isObservacion() {
			return observacion;
		}

		public void setObservacion(boolean observacion) {
			this.observacion = observacion;
		}

		public List<Ambiente> getListaAmbiente() {
			return listaAmbiente;
		}

		public void setListaAmbiente(List<Ambiente> listaAmbiente) {
			this.listaAmbiente = listaAmbiente;
		}

		public List<String> getAmbientes() {
			return ambientes;
		}

		public void setAmbientes(List<String> ambientes) {
			this.ambientes = ambientes;
		}

		public String getNombreAmbiente() {
			return nombreAmbiente;
		}

		public void setNombreAmbiente(String nombreAmbiente) {
			this.nombreAmbiente = nombreAmbiente;
		}

		public boolean isInstructor() {
			return instructor;
		}

		public void setInstructor(boolean instructor) {
			this.instructor = instructor;
		}

}
