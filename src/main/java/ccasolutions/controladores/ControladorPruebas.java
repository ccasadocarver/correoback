package ccasolutions.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;
import ccasolutions.servicios.IServicioPruebas;

@CrossOrigin (origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ControladorPruebas 
{
	@Autowired
	IServicioPruebas servicioPruebas;
	
	@PostMapping("/pruebas/")
	public ResponseEntity<RespuestaUsuarios> devolverJWT(@RequestBody Usuario usuario)
	{
		return servicioPruebas.devolverJWT(usuario);
	}
	
	@PostMapping("/pruebas/decodificar")
	public ResponseEntity<RespuestaUsuarios> decodificarJWT(@RequestBody String token)
	{
		return servicioPruebas.decodificarJWT(token);
	}
}
