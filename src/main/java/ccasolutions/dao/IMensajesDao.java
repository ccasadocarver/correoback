package ccasolutions.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ccasolutions.modelos.Mensaje;

public interface IMensajesDao extends CrudRepository<Mensaje, Long>
{
	//Metodo para la bandeja de entrada completa
	@Query("SELECT m FROM Mensaje m WHERE m.receptorId = :receptorId AND m.estaEnPapelera = false ORDER BY fechaEnvio DESC")
    List<Mensaje> findMessagesByReceptorId(@Param("receptorId") Long receptorId);
	

	//Metodo para la bandeja de entrada completa
	@Query("SELECT m FROM Mensaje m WHERE m.emisorId = :emisorId AND m.estaEnPapelera = false ORDER BY fechaEnvio DESC")
	 List<Mensaje> findMessagesByEmisorId(@Param("emisorId") Long emisorId);
	
	//Metodo para la bandeja de entrada y salida, filtrando por receptor/emisor
	// Bandeja de entrada, mantener fijo el receptorId y elegir un emisorId
	// Bandeja de salida, mantener fijo el emisorId y elegir un receptorId
	@Query("SELECT m FROM Mensaje m WHERE m.emisorId = :emisorId AND m.receptorId = :receptorId AND m.estaEnPapelera = false ORDER BY fechaEnvio DESC")
    List<Mensaje> findMessagesByEmisorIdAndReceptorId(@Param("emisorId") Long emisorId, @Param("receptorId") Long receptorId);
	
	//Para enviar a la papelera
	@Modifying
	@Query("UPDATE Mensaje m SET m.estaEnPapelera = true WHERE m.id = :mensajeId")
	int enviarAPapelera(@Param("mensajeId") Long mensajeId);
	 
	//Para sacar de la papelera
	@Modifying
	@Query("UPDATE Mensaje m SET m.estaEnPapelera = false WHERE m.id = :mensajeId")
	int sacarDePapelera(@Param("mensajeId") Long mensajeId);
	
	
	 //Mostrar papelera	
	@Query("SELECT m FROM Mensaje m WHERE m.receptorId = :receptorId AND m.estaEnPapelera = true")
	List<Mensaje> findMessagesByEmisorIdInPapelera(@Param("receptorId") Long receptorId);

	
}
