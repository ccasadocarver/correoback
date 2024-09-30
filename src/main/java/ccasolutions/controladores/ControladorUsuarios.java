package ccasolutions.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ccasolutions.modelos.Usuario;

import ccasolutions.respuesta.RespuestaUsuarios;
import ccasolutions.servicios.IServicioUsuarios;

@CrossOrigin (origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ControladorUsuarios 
{
	@Autowired
	private IServicioUsuarios servicioUsuarios;
	
	
	
	
	@PostMapping("/usuarios")
	public String guardarUsuario(@RequestBody Usuario usuario)
	{
		return servicioUsuarios.guardar(usuario);
		
		/*
		 Para probarlo con el PostMan:
		 
		 Create new collection, dentro de esa new collection, add request
		 Post, con la url: localhost:8080/api/v1/usuarios
		 Body, raw y el json seria:
		 
		 {
    		"nombre": "Carlos",
    		"contrasenya": "soyCarlos",
    		"correo" : "carlos@casado.com",
    		"fechaCreacion" : "2024-07-30T15:30:00",
    		"esAdmin" : true
		 }
		 
		 Devolverá un JSON con los atributos del objeto RespuestaUsuarios:
		 
		 {
    		"respuesta": "Usuario almacenado correctamente.",
    		"usuarios": 
    		[
        		{
            		"id": 1,
            		"nombre": "Carlos",
    				"contrasenya": "soyCarlos",
    				"correo" : "carlos@casado.com",
    				"fechaCreacion" : "2024-07-30T15:30:00",
    				"esAdmin" : true
        		}
    		]
		}
		 
		 
		 */
	
	}
	
	
	@GetMapping("/usuarios")
	public RespuestaUsuarios obtenerTodosLosUsuarios()
	{
		return servicioUsuarios.obtenerTodosLosUsuarios();
	}
	
	
	@GetMapping("/usuarios/{id}")
	public ResponseEntity<RespuestaUsuarios> buscarUnUsuarioPorId(@PathVariable("id") Long id)
	{
		return servicioUsuarios.buscarUnUsuarioPorId(id);
	}
	
	@DeleteMapping("/usuarios/{id}")
	public ResponseEntity<RespuestaUsuarios> borrarUnUsuarioPorId(@PathVariable("id") Long id)
	{
		return servicioUsuarios.borrarUnUsuarioPorId(id);
	}
	
	@PutMapping("/usuarios/{id}")
	public ResponseEntity<RespuestaUsuarios> hacerDeshacerAdmin(@PathVariable("id") Long id)
	{
		return servicioUsuarios.hacerDeshacerAdmin(id);
	}
	
	@PostMapping("/usuarios/login")
	public ResponseEntity<RespuestaUsuarios> comprobarLogin(@RequestBody Usuario usuario)
	{
		return servicioUsuarios.comprobarLogin(usuario);
	}
	
	@GetMapping("/usuarioscorreo/{correo}")
	public ResponseEntity<RespuestaUsuarios> UsuarioPorCorreo(@PathVariable("correo") String correo)
	{
		return servicioUsuarios.usuarioPorCorreo(correo);
	}
	
	@PutMapping("/usuariosformatear/{id}")
	public ResponseEntity<RespuestaUsuarios> formatearUsuarioPorId(@PathVariable("id") Long id)
	{
		return servicioUsuarios.formatearUsuarioPorId(id);
	}
	
	
	
	
	
	
	
	
	
	
	
}
