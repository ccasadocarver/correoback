package ccasolutions.servicios;

import org.springframework.http.ResponseEntity;

import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;

public interface IServicioPruebas 
{
	public ResponseEntity<RespuestaUsuarios> devolverJWT (Usuario usuario);
	
	public ResponseEntity<RespuestaUsuarios> decodificarJWT (String token);
}
