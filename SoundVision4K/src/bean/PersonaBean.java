package bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.*;

import dao.UsuarioDao;
import vo.Ambiente;
import vo.Usuario;

@ManagedBean
@SessionScoped
public class PersonaBean {
	private String contrasena;
	private String repcontrasena;
	private String respuesta;
	private List<String>listaRol=new ArrayList<>();;
	private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
	private ArrayList<Ambiente> listaInventarios = new ArrayList<>();
	private ArrayList<Ambiente> listaAmbientes = new ArrayList<>();

	private Usuario miUsuario;
	UsuarioDao miUsuarioDao = new UsuarioDao();
	private Ambiente miAmbiente;

	public PersonaBean() {
		miUsuario = new Usuario();
		miAmbiente = new Ambiente();
		contador();
		listasRol();
	}

	private void listasRol() {
		listaRol.clear();
		listaRol=miUsuarioDao.obtenerListaRol(1);
	}

	public void listaAmbiente() {
		listaAmbientes.clear();
		listaAmbientes = miUsuarioDao.obtenerListaAmbiente();

	}

	public void listaUsuario() {
		listaUsuarios.clear();
		listaUsuarios = miUsuarioDao.obtenerListaPersonas();
	}

	public void listaInventario() {
		listaInventarios.clear();
		listaInventarios = miUsuarioDao.obtenerListaInventario();
	}

	public int contador() {
		int a = miUsuarioDao.contador();
		return a;
	}

	public int contadorReservas() {
		int a = miUsuarioDao.contadorReservas();
		return a;
	}

	public int contadorAmbientes() {
		int a = miUsuarioDao.contadorAmbientes();
		return a;
	}
	
	public String agregarUsuario() {

		miUsuario.setContrasena(contrasena);
		miUsuario.setRepcontrasena(repcontrasena);
		if (contrasena.equals(repcontrasena) == true) {
			switch (miUsuarioDao.agregarUsuario(miUsuario)) {
			case 1:
				setRespuesta("Registro Exitoso");
				break;
			case 2:
				setRespuesta("Registro  no Exitoso");
				break;
			}
			miUsuario = new Usuario();
		} else {
			setRespuesta("Las contraseñas deben coincidir");
		}
		return "registro.jsf?faces-redirect=true";
	}

	public String agregarInventario() {
		miUsuarioDao.agregarInventario(miAmbiente);
		miAmbiente = new Ambiente();
		listaInventario();
		return "ambiente.jsf?faces-redirect=true";

	}

	public String AmbienteAgregar() {
		miUsuarioDao.AmbienteAgregar(miAmbiente);
		miAmbiente = new Ambiente();
		listaAmbiente();
		return "Ambientes.jsf?faces-redirect=true";
	}

	public String eliminarPersona(Usuario usuario) {
		setRespuesta(miUsuarioDao.eliminarPersona(usuario));
		listaUsuarios.remove(usuario);
		return "controlUsuario.jsf?faces-redirect=true";
	}

	public String editarPersona(Usuario usuario) {
		usuario.setEditar(true);
		return "controlUsuario.jsf";
	}

	public void guardarPersona(Usuario usuario) {
		setRespuesta(miUsuarioDao.editarPersona(usuario));
		usuario.setEditar(false);
	}

	public String eliminarInventario(Ambiente ambiente) {
		setRespuesta(miUsuarioDao.eliminarInventario(ambiente));
		listaUsuarios.remove(ambiente);
		listaInventario();
		return "ambiente.jsf?faces-redirect=true";
	}
	
	public String eliminarAmbientes(Ambiente ambiente) {
		setRespuesta(miUsuarioDao.eliminarAmbiente(ambiente));
		listaUsuarios.remove(ambiente);
		listaAmbiente();
		return "ambientes.jsf?faces-redirect=true";
	}

	public String editarInventario(Ambiente ambiente) {
		ambiente.setEditar(true);
		return "ambiente.jsf";
	}

	public String editarAmbientes(Ambiente ambiente) {
		ambiente.setEditar(true);
		return "ambientes.jsf";
	}
	
	public void guardarInventario(Ambiente ambiente) {
		setRespuesta(miUsuarioDao.editarInventario(ambiente));
		ambiente.setEditar(false);
	}
	public void guardarAmbientes(Ambiente ambiente) {
		setRespuesta(miUsuarioDao.editarAmbientes(ambiente));
		ambiente.setEditar(false);
	}


	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Usuario getMiUsuario() {
		return miUsuario;
	}

	public void setMiUsuario(Usuario miUsuario) {
		this.miUsuario = miUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getRepcontrasena() {
		return repcontrasena;
	}

	public void setRepcontrasena(String repcontrasena) {
		this.repcontrasena = repcontrasena;
	}

	public ArrayList<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public Ambiente getMiAmbiente() {
		return miAmbiente;
	}

	public void setMiAmbiente(Ambiente miAmbiente) {
		this.miAmbiente = miAmbiente;
	}

	public ArrayList<Ambiente> getListaInventarios() {
		return listaInventarios;
	}

	public void setListaInventarios(ArrayList<Ambiente> listaInventarios) {
		this.listaInventarios = listaInventarios;
	}

	public ArrayList<Ambiente> getListaAmbientes() {
		return listaAmbientes;
	}

	public void setListaAmbientes(ArrayList<Ambiente> listaAmbientes) {
		this.listaAmbientes = listaAmbientes;
	}

	public List<String> getListaRol() {
		return listaRol;
	}

	public void setListaRol(List<String> listaRol) {
		this.listaRol = listaRol;
	}

}
