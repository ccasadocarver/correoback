package ccasolutions.servicios;

import org.springframework.http.ResponseEntity;

import ccasolutions.modelos.Mensaje;

import ccasolutions.respuesta.RespuestaMensajes;

public interface IServicioMensajes 
{
	public ResponseEntity<RespuestaMensajes> guardar(Mensaje mensaje) ;	
	
	public ResponseEntity <RespuestaMensajes> buscarMensajePorReceptorId(Long id);
	
	public ResponseEntity <RespuestaMensajes> buscarMensajePorEmisorId(Long id);
	
	public ResponseEntity <RespuestaMensajes> buscarMensajePorEmisorIdYReceptorId(Long emisorId, Long receptorId);
	
	public ResponseEntity <RespuestaMensajes> borrarMensajePorId(Long id);
	
	public ResponseEntity<RespuestaMensajes> enviarAPapelera(Long id);
	
	public ResponseEntity<RespuestaMensajes> sacarDePapelera(Long id);
	
	public ResponseEntity<RespuestaMensajes> mostrarPapelera(Long id);
	
	public ResponseEntity<RespuestaMensajes> buscarMensajePorId(Long id);
}
