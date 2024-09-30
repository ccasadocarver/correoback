package ccasolutions.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import ccasolutions.dao.IUsuarioDao;
import ccasolutions.modelos.Usuario;
import ccasolutions.respuesta.RespuestaUsuarios;

import java.util.Optional;

public class ServicioUsuariosTest 
{

	@Mock
    private IUsuarioDao userRepository; // Asegúrate de reemplazar esto por tu repositorio real

    @InjectMocks
    private ServicioUsuarios servicioUsuarios; // La clase que quieres probar

    @BeforeEach
    void setUp() 
    {
        MockitoAnnotations.openMocks(this);
    }

    
    
    @Test
    void testBuscarUnUsuarioPorId_UsuarioEncontrado() 
    {
        // Datos de prueba
        long userId = 1L;
        Usuario mockUser = new Usuario(); // Reemplaza 'User' por tu clase real de usuario
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Ejecutar el método a probar
        ResponseEntity<RespuestaUsuarios> response = servicioUsuarios.buscarUnUsuarioPorId(userId);

        // Verificaciones
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    
    
    @Test
    void testBuscarUnUsuarioPorId_UsuarioNoEncontrado() 
    {
        // Datos de prueba
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Ejecutar el método a probar
        ResponseEntity<RespuestaUsuarios> response = servicioUsuarios.buscarUnUsuarioPorId(userId);

        // Verificaciones
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue()); // Comprueba que el estado es 404
        assertNotNull(response.getBody()); // Ahora esperamos que el cuerpo no sea null
        assertEquals("No se encontró el usuario.", response.getBody().getRespuesta()); // Comprueba que el mensaje es correcto
        assertTrue(response.getBody().getUsuarios().isEmpty()); // Asegúrate de que la lista de usuarios esté vacía
    }

}
