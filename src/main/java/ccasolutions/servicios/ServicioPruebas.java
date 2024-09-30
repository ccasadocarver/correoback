package ccasolutions.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;

@Service
public class ServicioPruebas implements IServicioPruebas
{
	String firma = "PermanentementePendiente";
	@Override
	public ResponseEntity<RespuestaUsuarios> devolverJWT(Usuario usuario) 
	{
		RespuestaUsuarios respuesta = new RespuestaUsuarios ();
		
		String token = "No se ha podido generar el token: ";
		
		try
		{
			token = JWT.create()
					.withSubject(usuario.getNombre())
					.withClaim("correo", usuario.getCorreo())
					.withClaim("contrasenya", usuario.getContrasenya())
					.withIssuedAt(new Date())
					.withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // Fecha de expiración (1 hora)
					.sign(Algorithm.HMAC256(firma));
			respuesta.setRespuesta(token);
			respuesta.setUsuarios(null);
		}
		catch (Exception e)
		{
			respuesta.setRespuesta(token + e);
			respuesta.setUsuarios(null);
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}

	
	
	
	@Override
	public ResponseEntity<RespuestaUsuarios> decodificarJWT(String token) 	
	{
		
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
	
		try
		{

			DecodedJWT tokenDescodificado = JWT.require(Algorithm.HMAC256(firma))
		            .build()
		            .verify(token);
			
				List<Usuario> usuario = new ArrayList<>();
				Usuario usuarioFromToken = new Usuario();
				
				usuarioFromToken.setNombre(tokenDescodificado.getSubject());
				usuarioFromToken.setCorreo(tokenDescodificado.getClaim("correo").asString());
				usuarioFromToken.setContrasenya(tokenDescodificado.getClaim("contrasenya").asString());
				
				usuario.add(usuarioFromToken);
				
				respuesta.setRespuesta("Usuario validado y decodificado correctamente");
				respuesta.setUsuarios(usuario);				
			
		}
		
		catch (JWTVerificationException e) 
		{
	        respuesta.setRespuesta("Token no validado: " + e.getMessage());
	        respuesta.setUsuarios(null);
	        return new ResponseEntity<RespuestaUsuarios>(respuesta, HttpStatus.UNAUTHORIZED);
		}
		
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al decodificar el token: " + e);
			respuesta.setUsuarios(null);
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}

	
	
	
}
