package ccasolutions.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="mensajes")
public class Mensaje implements Serializable
{

	private static final long serialVersionUID = 723196642099446163L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String contenido;
	private Long emisorId;
	private Long receptorId;
	private LocalDateTime fechaEnvio;
	private boolean deAdmin;
	private boolean estaEnPapelera;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public Long getEmisorId() {
		return emisorId;
	}
	public void setEmisorId(Long emisorId) {
		this.emisorId = emisorId;
	}
	public Long getReceptorId() {
		return receptorId;
	}
	public void setReceptorId(Long receptorId) {
		this.receptorId = receptorId;
	}
	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public boolean isDeAdmin() {
		return deAdmin;
	}
	public void setDeAdmin(boolean deAdmin) {
		this.deAdmin = deAdmin;
	}
	public boolean isEstaEnPapelera() {
		return estaEnPapelera;
	}
	public void setEstaEnPapelera(boolean estaEnPapelera) {
		this.estaEnPapelera = estaEnPapelera;
	}
	
	
	
	

}
