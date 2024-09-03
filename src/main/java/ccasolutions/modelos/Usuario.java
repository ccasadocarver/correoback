package ccasolutions.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario implements Serializable
{
	private static final long serialVersionUID = 8602339513118852346L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String nombre;
	private String contrasenya;
	private String correo;
	private LocalDateTime fechaCreacion;
	private boolean esAdmin;
	
	public long getId() 
	{
		return id;
	}
	public void setId(long id) 
	{
		this.id = id;
	}
	public String getNombre() 
	{
		return nombre;
	}
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	public String getContrasenya() 
	{
		return contrasenya;
	}
	public void setContrasenya(String contrasenya) 
	{
		this.contrasenya = contrasenya;
	}
	public String getCorreo() 
	{
		return correo;
	}
	public void setCorreo(String correo) 
	{
		this.correo = correo;
	}
	public LocalDateTime getFechaCreacion() 
	{
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDateTime fechaCreacion) 
	{
		this.fechaCreacion = fechaCreacion;
	}
	public boolean isEsAdmin() 
	{
		return esAdmin;
	}
	public void setEsAdmin(boolean esAdmin) 
	{
		this.esAdmin = esAdmin;
	}
	
	
	
	
	
}
