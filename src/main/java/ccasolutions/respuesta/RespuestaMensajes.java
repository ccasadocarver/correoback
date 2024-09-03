package ccasolutions.respuesta;

import java.util.List;

import ccasolutions.modelos.Mensaje;

public class RespuestaMensajes 
{
	private String respuesta;
	private List <Mensaje> mensajes;
	
	
	public String getRespuesta() 
	{
		return respuesta;
	}
	public void setRespuesta(String respuesta) 
	{
		this.respuesta = respuesta;
	}
	public List<Mensaje> getMensajes() 
	{
		return mensajes;
	}
	public void setMensajes(List<Mensaje> mensajes) 
	{
		this.mensajes = mensajes;
	}
	

}
