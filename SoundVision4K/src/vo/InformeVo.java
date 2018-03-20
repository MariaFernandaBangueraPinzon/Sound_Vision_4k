package vo;

public class InformeVo {

	private int codigo;
	private int tipoInforme;
	private String destinatario;
	private String destino;
	private String descripcion;
	private int reserva;
	
	public InformeVo(int codigo, int tipoInforme, String destinatario, String destino, String descripcion, int reserva){
		this.codigo= codigo;
		this.tipoInforme=tipoInforme;
		this.destinatario=destinatario;
		this.destino=destino;
		this.descripcion=descripcion;
		this.reserva=reserva;
	}
	
	public InformeVo(){
		
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getTipoInforme() {
		return tipoInforme;
	}
	public void setTipoInforme(int tipoInforme) {
		this.tipoInforme = tipoInforme;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getReserva() {
		return reserva;
	}
	public void setReserva(int reserva) {
		this.reserva = reserva;
	}
}
