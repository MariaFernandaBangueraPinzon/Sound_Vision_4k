package vo;

public class Ambiente {

	private String descripcion;
	private int codInventario;
	private int Cantidad;
	private String Nombre;
	private String nomAmbiente;
	private int codAmbiente;
	private boolean Editar;
	private String AdministradorAmb;

	public Ambiente() {

	}

	public String getNomAmbiente() {
		return nomAmbiente;
	}

	public void setNomAmbiente(String nomAmbiente) {
		this.nomAmbiente = nomAmbiente;
	}

	public boolean isEditar() {
		return Editar;
	}

	public void setEditar(boolean editar) {
		Editar = editar;
	}

	public int getCantidad() {
		return Cantidad;
	}

	public void setCantidad(int cantidad) {
		Cantidad = cantidad;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public int getCodAmbiente() {
		return codAmbiente;
	}

	public void setCodAmbiente(int codAmbiente) {
		this.codAmbiente = codAmbiente;
	}

	public int getCodInventario() {
		return codInventario;
	}

	public void setCodInventario(int codInventario) {
		this.codInventario = codInventario;
	}

	public String getAdministradorAmb() {
		return AdministradorAmb;
	}

	public void setAdministradorAmb(String administradorAmb) {
		AdministradorAmb = administradorAmb;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
