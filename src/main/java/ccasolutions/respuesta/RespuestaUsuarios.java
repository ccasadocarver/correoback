package ccasolutions.respuesta;

import java.util.ArrayList;
import java.util.List;

import ccasolutions.modelos.Usuario;

public class RespuestaUsuarios 
{
	private String respuesta;
	private List <Usuario> usuarios = new ArrayList<>();;
	
	
	public String getRespuesta() 
	{
		return respuesta;
	}
	public void setRespuesta(String respuesta) 
	{
		this.respuesta = respuesta;
	}
	public List<Usuario> getUsuarios() 
	{
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) 
	{
		this.usuarios = usuarios;
	}
	

	
}
