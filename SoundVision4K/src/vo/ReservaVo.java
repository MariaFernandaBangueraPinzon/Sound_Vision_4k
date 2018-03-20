package vo;

public class ReservaVo {

	public ReservaVo(int codigo, String cedulaUsuario, String fecha, String horaLlegada, String horaSalida,
			int ambiente, String motivo) {
		super();
		this.codigo = codigo;
		this.cedulaUsuario = cedulaUsuario;
		this.fecha = fecha;
		this.horaLlegada = horaLlegada;
		this.horaSalida = horaSalida;
		this.ambiente = ambiente;
		this.motivo = motivo;
	}
	
	public ReservaVo(){
		
	}
	private int codigo;
	private String cedulaUsuario;
	private String fecha;
	private String horaLlegada;
	private String horaSalida;
	private int ambiente;
	private String motivo;
	
	
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getCedulaUsuario() {
		return cedulaUsuario;
	}
	public void setCedulaUsuario(String cedulaUsuario) {
		this.cedulaUsuario = cedulaUsuario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHoraLlegada() {
		return horaLlegada;
	}
	public void setHoraLlegada(String horaLlegada) {
		this.horaLlegada = horaLlegada;
	}
	public String getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
	public int getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(int ambiente) {
		this.ambiente = ambiente;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}
