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

import ccasolutions.dao.IMensajesDao;
import ccasolutions.dao.IUsuarioDao;
import ccasolutions.modelos.Mensaje;

import ccasolutions.respuesta.RespuestaMensajes;


@Service
public class ServicioMensajes implements IServicioMensajes
{

	@Autowired
	private IMensajesDao mensajesDao;

	@Autowired
	private IUsuarioDao usuarioDao;
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaMensajes> guardar(Mensaje mensaje) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		
		//Evaluamos si el usuario con el id del emisor del mensaje es administrador
		
		Boolean esAdminElEmisor = usuarioDao.esAdminElUsuario(mensaje.getEmisorId());
		
		if (esAdminElEmisor != null && esAdminElEmisor)
		{
			mensaje.setDeAdmin(true);
		}
		
		
		try
		{
		
			mensaje.setFechaEnvio(LocalDateTime.now());
			
			
			Mensaje mensajeGuardado = mensajesDao.save(mensaje);
			
			if (mensajeGuardado != null)
			{
				
				List<Mensaje> lista = new ArrayList<>();
				lista.add(mensajeGuardado);				
				respuesta.setMensajes(lista);
				respuesta.setRespuesta("Mensaje guardado correctamente.");
			}
			else
			{
				respuesta.setRespuesta("No se ha podido guardar el mensaje.");
				return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("No se ha podido guardar el mensaje: " + e);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}


	@Override
	@Transactional (readOnly=true)
	public ResponseEntity<RespuestaMensajes> buscarMensajePorReceptorId(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
				
		try
		{
			respuesta.setMensajes(mensajesDao.findMessagesByReceptorId(id));
			if (respuesta.getMensajes().isEmpty())
			{
				respuesta.setRespuesta("No hay mensajes para mostrar.");
			}
			else if(respuesta.getMensajes() == null)
			{
				respuesta.setRespuesta("Error. Lista nula.");
			}
			else
			{
				respuesta.setRespuesta("Mensajes obtenidos correctamente.");
			}
			
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al obtener los mensajes.");
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	
	
	@Override
	@Transactional (readOnly=true)
	public ResponseEntity<RespuestaMensajes> buscarMensajePorEmisorId(Long emisorId) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
				
		try
		{
			respuesta.setMensajes(mensajesDao.findMessagesByEmisorId(emisorId));
			if (respuesta.getMensajes().isEmpty())
			{
				respuesta.setRespuesta("No hay mensajes para mostrar.");
			}
			else if(respuesta.getMensajes() == null)
			{
				respuesta.setRespuesta("Error. Lista nula.");
			}
			else
			{
				respuesta.setRespuesta("Mensajes obtenidos correctamente.");
			}
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al obtener los mensajes.");
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	
	@Override
	@Transactional (readOnly=true)
	public ResponseEntity<RespuestaMensajes> buscarMensajePorEmisorIdYReceptorId(Long emisorId, Long receptorId) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
				
		try
		{
			respuesta.setMensajes(mensajesDao.findMessagesByEmisorIdAndReceptorId(emisorId, receptorId));
			if (respuesta.getMensajes().isEmpty())
			{
				respuesta.setRespuesta("No hay mensajes para mostrar.");
			}
			else if(respuesta.getMensajes() == null)
			{
				respuesta.setRespuesta("Error. Lista nula.");
			}
			else
			{
				respuesta.setRespuesta("Mensajes obtenidos correctamente.");
			}
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al obtener los mensajes.");
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}


	
	@Override
	public ResponseEntity<RespuestaMensajes> buscarMensajePorId(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		List <Mensaje> lista = new ArrayList<>();
		
		 try 
		 {
			Optional<Mensaje> mensajeOptional = mensajesDao.findById(id);
			
			 if (mensajeOptional.isPresent()) 
			 {		        
		          lista.add(mensajeOptional.get());
		          respuesta.setRespuesta("Mensaje encontrado.");
		     } 
			 else 
			 {		           
		          respuesta.setRespuesta("Mensaje no encontrado.");
		     }		        
		      
		     respuesta.setMensajes(lista);
		 } 
		 catch (Exception e) 
		 {      
			 respuesta.setRespuesta("Error al buscar el mensaje: " +e);
			 return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		 }
		 
		 return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	
	
	@Override
	public ResponseEntity<RespuestaMensajes> borrarMensajePorId(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		
		//Comprobamos que ese usuario exista, evaluando si el Optional devuelto por el metodo findById esta presente
		 		 
		List <Mensaje> listaMensajeId = buscarMensajePorId(id).getBody().getMensajes();
		
		
		if (!listaMensajeId.isEmpty())
		{	
			if (listaMensajeId.get(0).isEstaEnPapelera())
			{
				try
				{				
					mensajesDao.deleteById(id);
												
					respuesta.setMensajes(listaMensajeId);
					respuesta.setRespuesta("Mensaje eliminado definitivamente.");				
				}
				catch (Exception e)
				{
					respuesta.setRespuesta("Error al intentar eliminar el mensaje: " +e);
					return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);			
				}	
			
			}
			else
			{
				respuesta.setRespuesta("El mensaje no puede ser eliminado, no esta en la papelera.");
				return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
			}
		}
		else
		{
			respuesta.setRespuesta("No se encontró el mensaje.");
			respuesta.setMensajes(listaMensajeId);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
		}		
		
		
		return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaMensajes> enviarAPapelera(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		List <Mensaje> listaMensajeId= buscarMensajePorId(id).getBody().getMensajes();
		
		if (!listaMensajeId.isEmpty())
		{	
			if (!listaMensajeId.get(0).isEstaEnPapelera())
			{
		
				try
				{
					int filasAfectadas = mensajesDao.enviarAPapelera(id);
			
					if (filasAfectadas > 0)
					{			
				
						respuesta.setMensajes(listaMensajeId);
						respuesta.setRespuesta("Mensaje enviado a la papelera.");
					}
					else
					{
						respuesta.setRespuesta("Error. No se ha podido mover a la papelera.");
					}
				}
				catch (Exception e)
				{
					respuesta.setRespuesta("Error al intentar mover el mensaje: " +e);
					return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);	
				}
			}
			else
			{
				respuesta.setRespuesta("El mensaje esta en la papelera.");
				return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
			}
		}
		else
		{
			respuesta.setRespuesta("No se encontró el mensaje." );
			respuesta.setMensajes(listaMensajeId);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<RespuestaMensajes> (respuesta, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public ResponseEntity<RespuestaMensajes> sacarDePapelera(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		List <Mensaje> listaMensajeId= buscarMensajePorId(id).getBody().getMensajes();	
				
		if (!listaMensajeId.isEmpty())
		{	
			if (listaMensajeId.get(0).isEstaEnPapelera())
			{
		
				try
				{
					int filasAfectadas = mensajesDao.sacarDePapelera(id);
			
					if (filasAfectadas > 0)
					{			
				
										
						respuesta.setMensajes(listaMensajeId);
						respuesta.setRespuesta("Mensaje devuelto a su bandeja correspondiente.");
					}
					else
					{
						respuesta.setRespuesta("Error. No se ha podido mover a su bandeja correspondiente.");
					}
			
				}
				catch (Exception e)
				{
					respuesta.setRespuesta("Error al intentar mover el mensaje: " +e);
					return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);	
				}
			}
			else
			{
				respuesta.setRespuesta("El mensaje no esta en la papelera.");
				return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
			}
		}
		else
		{
			respuesta.setRespuesta("No se encontró el mensaje.");
			respuesta.setMensajes(listaMensajeId);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<RespuestaMensajes> (respuesta, HttpStatus.OK);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public ResponseEntity<RespuestaMensajes> mostrarPapelera(Long id) 
	{
		RespuestaMensajes respuesta = new RespuestaMensajes();
		
		try
		{
			List<Mensaje> lista = mensajesDao.findMessagesByEmisorIdInPapelera(id);
			
			if (lista.isEmpty())
			{
				respuesta.setMensajes(lista);
				respuesta.setRespuesta("No hay elementos en la papelera.");
			}
			else
			{
				respuesta.setMensajes(lista);
				respuesta.setRespuesta("Mensajes de la papelera obtenidos.");
			}
		}
		catch (Exception e)
		{
			respuesta.setRespuesta("Error al acceder a la papelera: " +e);
			return new ResponseEntity <RespuestaMensajes> (respuesta, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		
		return new ResponseEntity<RespuestaMensajes> (respuesta, HttpStatus.OK);
	}



	
	
	

}
