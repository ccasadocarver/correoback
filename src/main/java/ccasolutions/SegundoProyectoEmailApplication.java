package ccasolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SegundoProyectoEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SegundoProyectoEmailApplication.class, args);
	}

}

/*

**************** PEQUEÑA GUIA PARA STS **********************

Seguramente falten cosas, pero el esqueleto basico es este:


	1 - Application.java

		Es esta clase, imprescindible para que la aplicacion se ejecute, no tiene mas, solo el SpringApplication.run
		
		1.1 - ApplicationProperties
		
			Archivo muy importante para la conexion a la base de datos, se encuentra en src/main/resources, como minimo debe tener:
			
			1.1.1 - URL de la base de datos:
			
				spring.datasource.url=jdbc:mysql://localhost/nombreBaseDeDatos
				
			1.1.2 - Nombre de usuario de la base de datos
			
			 	spring.datasource.username=root
			 	
			1.1.3 - Contraseña de la base de datos
				
				spring.datasource.password=1234
				
			1.1.4 - Driver JDBC 
			
				spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
				
			Algunas cosas secundarias son, por ejemplo:
			
			1.1.5 - Establecer el puerto en el que el servidor embebido escucha las solicitudes HTTP. 
			
				server.port=8080
				
				Por defecto es el 8080
 
 			1.1.6 - Configurar como JPA interactúa con la base de datos.
 			
 				spring.jpa.hibernate.ddl-auto=update
 				
 				Con update indica que Hibernate debe actualizar el esquema de la base de datos en cada inicio de la aplicacion.
 				
 			1.1.7 - Configurar el nivel de registro para Hibernate.
 			
 				logging.level.org.hibernate.SQL=debug
 				
 				Con debug hara que Hibernate registre todas las consultas SQL ejecutadas, lo cual es util para depuracion.
		
		
	2 - Modelos
	
		Los modelos son las clases de los elementos que van a tratarse, en este caso son Usuarios y Mensajes.		
		Ademas de los atributos y getters y setters, hay varias cosas importantes a tener en cuenta.
		
		2.1 - Anotaciones
		
			Los modelos deben tener:
			
				2.1.1 - @Entity
				
					Decora la clase para que sea definida como una entidad.
					Es decir, se prepara esta clase para interactuar de manera estructurada y eficiente con una base de datos relacional.
					Debe ser mapeada a una tabla en una base de datos relacional. Cada instancia de la clase corresponde a una fila en la tabla.
					Sin @Entity, Hibernate no reconocerá la clase como una entidad y no creará la tabla correspondiente en la base de datos.

				2.1.2 - @Table(name="nombreTabla")
				
					Con esto le decimos el nombre de la tabla que queremos que tenga esta entidad.					
			
			El campo id debe tener:
				
				2.1.3 - @Id
				
					Con esto se indica que el campo Id de la tabla sera el atributo inmediatamente inferior
					
				2.1.4 - @GeneratedValue(strategy = GenerationType.IDENTITY)
				
					Con esto se indica que el id se autogenere cada vez que se realice un nuevo registro
			
		2.2 - implements Serializable
		
			Los modelos deben implementar la interfaz Serializable para que sus datos puedan ser transportados.
			
		2.3 - serialVersionUID
			
			Como atributo, los modelos deben tener un:
			
			private static final long serialVersionUID = -466339413645963704L;
			
			Esto se pone cuando ya se ha implementado Serializable, aparecerá una pequeña bombillita a la izquierda en public class y el nombre de la clase subrayado.
			Click y se le da a Add generated serial version ID, listo.
			
		Ya con esto estaria el modelo configurado.
		
		2.4 - Lombok
				
			Para los metodos Getters y Setter se puede hacer manual
			O recurrir a la libreria Lombok que nos genera además algunos métodos interesantes
			Hay que importarla en el IDE y en el pom.xml
	
			Para importarlo en el IDE: Vamos a internet: https://projectlombok.org/
			Install, Spring Tool Suite, Via Eclipse Plugin Installer y ahi te dice como
			Que básicamente es pegando esta url: https://projectlombok.org/p2 en Help -> Install New Software aqui en el IDE
			Se selecciona, next y listo, hay que reiniciar el IDE
	
			Para importarlo en el POM: También, en https://projectlombok.org/
			Install -> Build Tools -> Maven
			Y ahi coger el XML para copiarlo en <dependencies> del archivo POM
			Pero del contenido, sustituir esto:
			
				<version>1.18.34</version>
				<scope>provided</scope>
			
			Por esto:
			
				<optional>true</optional>
			
			Y basicamente quedaria:
			
				<dependency>
				<groupId>org.projectlombok</groupId>
					<artifactId>lombok</artifactId>
				<optional>true</optional>
				</dependency>
				
			Una vez hecho esto, se decora la clase con @Data y ya estaría.	

		
	3 - Objeto respuesta y ResponseEntity<ObjetoRespuesta>
	
		El tema del objeto respuesta es para que cada metodo que sea ejecutado emita una respuesta que pueda ser interpretada.
		Es decir, un metodo puede:
		
			- Ser ejecutado y fin, es decir que no devuelva nada.
			- Ser ejecutado y devolver lo que sea, un objeto, un String con el resultado, por ejemplo: "Se ha guardado el mensaje con exito".
			- Ser ejecutado y dependiendo del resultado, un objeto o un String.
			- Ser ejecutado, devolver un objeto o un String, y además el HttpStatus. Esto es lo mas util
			
		Para el tercer caso, se usa el objeto Respuesta, que sera uno para cada modelo, es decir: RespuestaUsuarios, por ejemplo.
		Este objeto va a tener dos atributos:
		
				private String respuesta;
				private List <Usuario> usuarios;
	
		Cada metodo ejecutado seteara ambos atributos.
		
		Con el objeto respuesta parece que esta todo bien, es decir, se envia un String con la info que se quiera y una lista de objetos tipo el que se este requiriendo.
		Pero no se esta enviando en ningun momento el Http.Status, y siendo esto peticiones Http, es importante que quien llame a estos metodos obtenga el HttpStatus.
		Eso se hace con el RespondeEntity, que basicamente permite enviar estos estados:
	
			ResponseEntity<RespuestaUsuarios> RE = new ResponseEntity<RespuestaUsuarios>(respuesta, HttpStatus.OK)
	
		Basicamente es el objeto respuesta (RespuestaUsuarios en este caso) con el Http.Status.
		Con getBody() se obtiene el cuerpo, o sea el objeto.
		Y con getStatusCode() la respuesta http.
			
	
	4 - Capa de DAO (Data Acces Object)
	
		Esta capa consiste en una serie de interfaces que contienen los metodos para acceder a la base de datos y realizar los CRUDs.
	
		4.1 - Interfaz CrudRepository
		
			La interfaz CrudRepository de SpringFramework tiene todos los metodos para hacer CRUD a una base de datos.
		
		4.2 - Interfaz IXDao
		
			La X correspondera al modelo al que queramos referenciarnos, por ejemplo IUsuariosDao.
			Esta interfaz va a heredar de CrudRepository y como parametros hay que pasarle la Entidad con la que trabajará y el tipo de dato de la clave primaria:
			
			public interface IUsuarioDao extends CrudRepository<Usuario, Long>
			{
				//Aqui se añaden los metodos que no tiene CrudRepository
			}
			
			En la interfaz IXDao se pueden crear metodos que no existan en el CrudRepository, se hace de la siguiente manera:
			
				- Anotacion @Query("ConsulaSQL")
			
					@Query("SELECT m FROM Mensaje m WHERE m.emisorId = :emisorId")
					
				- Tipo de dato que devuelve, nombre del metodo, @Param("nombreDelParametro") tipoDeDatoDelParametro y nombreDelParametro
				
	 				List<Mensaje> findMessagesByEmisorId(@Param("emisorId") Long emisorId);
	 				
	 		Ojo, si la consulta NO es SELECT, hay que ponerle @Modifying:
	 		
	 			@Modifying
				@Query("UPDATE Mensaje m SET m.estaEnPapelera = false WHERE m.id = :mensajeId")
				int sacarDePapelera(@Param("mensajeId") Long mensajeId);
			
			
	5 - Capa de Servicios		
		
		Esta capa consiste en la definicion de los metodos que van a ser llamados externamente.
		De nuevo, es exclusiva para cada modelo, usaremos el modelo Usuarios como ejemplo.
		
		5.1 - IServicioUsuarios
		
			La interfaz donde se indican los metodos que deberan ser implementados.
		
		5.2 - ServicioUsuarios
		
			La clase donde se van a implementar los metodos indicados en la interfaz.
			Hay varias cosas a tener en cuenta, sobre todo sobre anotaciones:
						
			La clase de servicios debe tener la notacion:
				
				5.2.1 - @Service
					
					Permite identificar esta clase como una clase de Servicio.
						
			Dentro de la clase, se declara un objeto tipo interfaz IUsuariosDao para acceder a los metodos ahi descritos, y debe tener la anotacion:
				
				5.2.2 - @Autowired
					
					Se utiliza para realizar la inyección de dependencias automáticamente en los componentes de la aplicación.
				
			Cada metodo debe tener dos parametros:
				
				5.2.3 - @Override
					
					Que es basicamente indicar que es un metodo que se sobreescribe de la interfaz.
					
				5.2.4 - @Transactional
					
					Lo que simplifica la gestión de transacciones en aplicaciones Spring evitando que se hagan de forma manual.
						
					Si son metodos GET: @Transactional (readOnly=true)
					Si son metodos POST o DELETE: @Transactional					
					
		
	6 - Capa controladora
					
		Ultima capa, esta es la capa externa y expuesta a peticiones Http desde el exterior.
		Desde esta capa se llaman a los metodos de la capa de servicios, declarando un objeto tipo interfaz IServicioX.
		Cosas a tener en cuenta de cara a anotaciones:
		
		6.1 - Anotaciones
		
			La clase debe tener las siguientes anotaciones:
		
			6.1.1 - @CrossOrigin (origins= {"url"})
			
				Es para indicar el puerto que queremos que la aplicacion permita.
				Para Angular: http://localhost:4200:
				@CrossOrigin (origins= {"http://localhost:4200."})
				
			6.1.2 - @RestController
			
				Indica que va a ser un servicio Rest, es decir, responde solicitudes Http y devuelve datos tipo JSON
				
			6.1.3 - @RequestMapping("url")
			
				Especifica el prefijo de la URL base para todos los endpoints en esta clase.
				Normalmente: @RequestMapping("/api/v1")
			
			Dentro de la clase, se declara un objeto tipo interfaz IServicioX para acceder a los metodos ahi descritos, y debe tener la anotacion:
				
			6.1.4 - @Autowired
			
				Como la 5.2.2.
			
			Los metodos deben tener la anotacion:
			
			6.1.5 - @XMapping("url")
			
				Indica que es un metodo X (Get, Post, Delete...) y continua la url del @RequestMapping.
				
				Algunos metodos (como los que hacen algo segun ID) pueden extender su url, por ejemplo:
				
				@PostMapping("/usuarios")
				@DeleteMapping("/mensajes/{id}")
				
				Importante aqui, solo puede haber una url para cada tipo de metodo por controlador.
				Es decir, no puede haber dos metodos @PostMapping con la misma url.
				Pero si puede ser si uno es @GetMapping("/usuarios") y el otro @GetMapping("/usuarios/{id}").
				
			
	7 - Pruebas con PostMan
	
		Una vez tengamos todo lo anterior podemos ir a PostMan a comprobar que estan bien.
		
		Create new collection, dentro de esa new collection, add request
		
		Por ejemplo un metodo X seria:
		
		Post, con la url, que basicamente es: localhost:(puerto indicado en server.port=puerto)/(url de @RequestMapping("url"))/(url de @XMapping("url") del metodo)
		
		Por ejemplo: localhost:8080/api/v1/mensajes
		
		Body, raw y el json seria:
		
		{
    		"contenido": "Hola, este es un mensaje de prueba del usuario 3 al usuario 5",
    		"emisorId": "3",
    		"receptorId" : "5",
    		"fechaEnvio" : "2022-08-30T15:30:00",
    		"deAdmin" : false,
    		"estaEnPapelera" : false
		}
			

 */
