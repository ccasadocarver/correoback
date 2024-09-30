package ccasolutions.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ccasolutions.dao.IUsuarioDao;

import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;

@Service
public class ServicioUsuarios implements IServicioUsuarios 

{
	@Autowired
	private IUsuarioDao usuarioDao;

	
	private String capitalize (String cadena)
	{
		return cadena.substring(0, 1).toUpperCase()+ cadena.substring(1).toLowerCase();
	}
	
	
	@Override
	@Transactional
	public String guardar(Usuario usuario) 
	{
		
		if (!existeCorreo(usuario))
		{
			try
			{
				usuario.setCorreo(usuario.getCorreo().toUpperCase());		
				usuario.setFechaCreacion(LocalDateTime.now());
				usuario.setNombre(capitalize(usuario.getNombre()));
				
				Usuario usuarioGuardado = usuarioDao.save(usuario);
				
				if (usuarioGuardado == null)
				{			
					
					return "Error al almacenar el usuario.";				
				}
				
			}
			catch (Exception e) 
			{
				return "Error al almacenar el usuario.";		
			}
		}
		else
		{
			return "El correo ya existe.";
		
		}		
		
		return "Usuario almacenado correctamente.";
	
	}


	
	
	private boolean existeCorreo(Usuario usuario)
	{
		boolean correoExiste = false;
		
		List <Usuario> lista = obtenerTodosLosUsuarios().getUsuarios();
		
		try
		{
			for (Usuario user : lista)
			{
					
				if(user.getCorreo().equals((usuario.getCorreo())))
				{
					correoExiste = true;
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
		return correoExiste;
	}
	
	/*
	 
	Objeto RespuestaUsuarios:
	
	Basicamente tendra dos atributos:
	
		1 - Un string que almacena el mensaje que se quiera
		2 - Una lista de objetos
	
	*/
	
	
	
	@Override
	@Transactional (readOnly=true)
	public RespuestaUsuarios obtenerTodosLosUsuarios() 
	{
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		List <Usuario> lista = new ArrayList<>();
		
		try
		{
			lista = (List<Usuario>) usuarioDao.findAll();
			
			if (lista != null)
			{
				respuesta.setUsuarios(lista);
				respuesta.setRespuesta("Usuarios obtenidos correctamente.");
			}
			else
			{
				respuesta.setRespuesta("Ha habido un error al obtener los usuarios.");
			}
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Ha habido un error al obtener los usuarios: " +e);
			return respuesta;
		}
		return respuesta;
	}
	
	@Override
	@Transactional (readOnly=true)
	public ResponseEntity<RespuestaUsuarios> buscarUnUsuarioPorId(long id) 
	{
		
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		List <Usuario> lista = new ArrayList<>();		
		
		try
		{
			/* Una tonteria, el findById devuelve un Optional			
			Un optional es una clase que puede o no contener un valor no nulo
			Hay que pasarlo al obtejo tipo Usuario con el metodo get()
			 */
			
			Optional <Usuario> usuarioOptional= usuarioDao.findById(id);
			
			if (usuarioOptional.isPresent())
			{
				Usuario usuarioObtenido = usuarioOptional.get();
				lista.add(usuarioObtenido);
				respuesta.setUsuarios(lista);
				respuesta.setRespuesta("Usuario encontrado.");
			}
			else
			{
				respuesta.setRespuesta("No se encontró el usuario.");
				return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);
			}
			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al buscar usuario: " +e);
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	
		return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}
	
	



	@Override
	@Transactional
	public ResponseEntity<RespuestaUsuarios> borrarUnUsuarioPorId(long id)
	{
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		
		//Comprobamos que ese usuario exista, evaluando si el Optional devuelto por el metodo findById esta presente
		 		 
		Optional <Usuario> usuarioOptional= usuarioDao.findById(id);
		
		if (usuarioOptional.isPresent())
		{	
			
			try
			{
				usuarioDao.deleteById(id);				
				respuesta.setRespuesta("Usuario eliminado.");				
			}
			catch (Exception e)
			{
				respuesta.setRespuesta("Error al intentar eliminar el usuario: " +e);
				return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);			
			}
		
			
		}
		else
		{
			respuesta.setRespuesta("No se encontró el usuario.");
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);
		}		
		
		return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}




	@Override
	@Transactional
	public ResponseEntity<RespuestaUsuarios> hacerDeshacerAdmin(long id) 
	{
		
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		
		List <Usuario> listaUsuarioId = buscarUnUsuarioPorId(id).getBody().getUsuarios();
		
		if (!listaUsuarioId.isEmpty()) 
		{
			try
			{
				if (listaUsuarioId.get(0).isEsAdmin())
				{
					int filasAfectadas = usuarioDao.deshacerAdmin(id);
					
					if (filasAfectadas > 0)
					{
						respuesta.setRespuesta("Usuario deshecho administrador.");
						respuesta.setUsuarios(listaUsuarioId);
					}
					else
					{
						respuesta.setRespuesta("No se ha podido deshacer administrador a este usuario.");
						respuesta.setUsuarios(listaUsuarioId);
					}
				}
				else
				{				
					int filasAfectadas = usuarioDao.hacerAdmin(id);
				
					if (filasAfectadas > 0)
					{
						respuesta.setRespuesta("Usuario hecho administrador.");
						respuesta.setUsuarios(listaUsuarioId);
					}
					else
					{
						respuesta.setRespuesta("No se ha podido hacer administrador a este usuario.");
						respuesta.setUsuarios(listaUsuarioId);
					}
				}			
				
			}	
			catch(Exception e)
			{
				respuesta.setRespuesta("No se encontró el usuario: " + e);
				return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}		
		else
		{
			respuesta.setRespuesta("No se encontró el usuario.");
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<RespuestaUsuarios>(respuesta, HttpStatus.OK);
	}




	@Override
	@Transactional
	public ResponseEntity<RespuestaUsuarios> comprobarLogin (Usuario usuario) 
	{
			RespuestaUsuarios respuesta = new RespuestaUsuarios();
			
			try
			{
				String correoDelUsuarioRecibido = usuarioDao.correoDeUsuario(usuario.getCorreo().toUpperCase());
				
				if (correoDelUsuarioRecibido == null)
				{
					respuesta.setRespuesta("El usuario no existe.");
					return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);
				}
				else
				{
					
					String contrasenyaRecibida = usuarioDao.contrasenyaDeUsuario(usuario.getCorreo().toUpperCase());
					
					if (contrasenyaRecibida != null)
					{
						if (contrasenyaRecibida.equals(usuario.getContrasenya()))
						{
							respuesta.setRespuesta("LogIn correcto.");
							List <Usuario> lista = new ArrayList<>();
							lista.add(usuarioDao.encontrarPorCorreo(correoDelUsuarioRecibido));
							respuesta.setUsuarios(lista);
						}
						else
						{
							respuesta.setRespuesta("LogIn incorrecto.");
						}
					}
					else
					{
						respuesta.setRespuesta("El usuario no tiene contraseña.");
						return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);
						
					}
				
				}
	
				
			}
			catch (Exception e)
			{
				respuesta.setRespuesta("No se encontró el usuario: " + e);
				return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			

		return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}


	
	@Override
	@Transactional
	public ResponseEntity<RespuestaUsuarios> usuarioPorCorreo (String correo) 
	{
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		List <Usuario> lista = new ArrayList<>();
		
		try
		{
			
			Usuario usuario = usuarioDao.encontrarPorCorreo(correo);
			
			if (usuario != null)
			{
				lista.add(usuario);				
				respuesta.setRespuesta("Usuario encontrado");
				respuesta.setUsuarios(lista);	

			}
			else
			{
				respuesta.setRespuesta("No existe ese correo.");
				return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.NOT_FOUND);			
			}
			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("No se encontró el usuario: " + e);
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<RespuestaUsuarios> formatearUsuarioPorId(long id) 
	{
		
		RespuestaUsuarios respuesta = new RespuestaUsuarios();
		List <Usuario> lista = new ArrayList<>();
		 
		
		try
		{
						
			RespuestaUsuarios objetoRespuestaObtenido = buscarUnUsuarioPorId(id).getBody();
			
			if (objetoRespuestaObtenido.getUsuarios() != null)
			{
				
				Usuario usuarioAFormatear = objetoRespuestaObtenido.getUsuarios().get(0);
				
				usuarioAFormatear.setNombre(capitalize(usuarioAFormatear.getNombre()));				
				usuarioAFormatear.setContrasenya(usuarioAFormatear.getContrasenya());				
				usuarioAFormatear.setCorreo(usuarioAFormatear.getCorreo().toUpperCase());					
				usuarioAFormatear.setFechaCreacion(usuarioAFormatear.getFechaCreacion());				
				usuarioAFormatear.setEsAdmin(usuarioAFormatear.isEsAdmin());			
				
				
				lista.add(usuarioAFormatear);
				respuesta.setRespuesta("Usuario formateado correctamente.");
				respuesta.setUsuarios(lista);
			}
			else
			{
				respuesta.setRespuesta("Usuario inexistente");
				return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.BAD_REQUEST);
			}
			
			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al buscar el usuario: " + e);
			return new ResponseEntity <RespuestaUsuarios> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		return new ResponseEntity<RespuestaUsuarios> (respuesta, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
}

























