package ccasolutions.servicios;



import org.springframework.http.ResponseEntity;

import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;


public interface IServicioUsuarios 
{
	
	//Vamos a ir poco a poco con los metodos
	
	
	//Este primero devolvera un String diciendo que se ha almacenado bien o mal, basicamente
	public String guardar(Usuario usuario);
	
	/*
	Pero claro, ¿que pasa si el metodo puede devolver dos cosas?
	Es decir, si sale bien, un objeto, pero si no un error
	Para eso se hace un objeto Respuesta que contenta esos parametros
	*/
	public RespuestaUsuarios obtenerTodosLosUsuarios();
	
	
	/*	
	Con el objeto respuesta parece que esta todo bien, es decir, se envia un String con la info que se quiera y una lista de objetos tipo el que se este requiriendo.
	Pero no se esta enviando en ningun momento el Http.Status, y siendo esto peticiones Http, es importante que quien llame a estos metodos obtenga el HttpStatus
	Eso se hace con el RespondeEntity, que basicamente permite enviar estos estados:
	
	ResponseEntity<RespuestaUsuarios> RE = new ResponseEntity<RespuestaUsuarios>(respuestaUsuarios, HttpStatus.OK)
	
	Sencillote, basicamente es el objeto Respuesta (RespuestaUsuarios en este caso) con el Http.Status
	getBody() para obtener el cuerpo, o sea el objeto
	getStatusCode() para obtener la respuesta http
	*/
	
	
	
	//Y nada, ya los demas
	
	public ResponseEntity<RespuestaUsuarios> buscarUnUsuarioPorId(long id);
	
	public ResponseEntity<RespuestaUsuarios> comprobarLogin(Usuario usuario);
	
	public ResponseEntity <RespuestaUsuarios> borrarUnUsuarioPorId (long id);
	
	public ResponseEntity <RespuestaUsuarios> hacerDeshacerAdmin (long id);
	
	public ResponseEntity<RespuestaUsuarios> usuarioPorCorreo (String correo);

}
