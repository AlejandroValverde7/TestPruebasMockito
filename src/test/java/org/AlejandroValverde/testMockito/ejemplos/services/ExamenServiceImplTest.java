package org.AlejandroValverde.testMockito.ejemplos.services;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;
import org.AlejandroValverde.testMockito.ejemplos.repositories.ExamenRepository;
import org.AlejandroValverde.testMockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class) //Con esto tambien podemos usar anotaciones y tener la dependencia de mock junit
class ExamenServiceImplTest {

    //Gracias a las anotaciones mock no necesitamos usar new ni nada asi
    @Mock
    ExamenRepository repository;
    @Mock
    PreguntaRepository preguntaRepository;

    //Crea la instanacia y ademas inyecta los objetos necesarios
    //ademas tenemos q llamar a su clase impl
    @InjectMocks
    ExamenServiceImpl service;

    //Mocks
    @BeforeEach
    void setUp() {
        //Habilitar el uso de anotaciones
        MockitoAnnotations.openMocks(this);


//        repository = mock(ExamenRepository.class); //Simulamos una instancia con los metodos
//        preguntaRepository = mock(PreguntaRepository.class);
//        service = new ExamenServiceImpl(repository,preguntaRepository);
    }

    //importamos de forma static la clase mockito para usarla con mas facilidad
    @Test
    void findExamenPorNombre() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cuando llamemos al findAll de repository se le dara la lista de prueba
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");


        //Comprobamos que existe el examen y que el id coincide
        assertTrue(examen.isPresent());
        assertEquals(5L,examen.orElseThrow().getId());
        assertEquals("Matematicas",examen.orElseThrow().getNombre());
    }


    //Como funcionaria si viene vacia la lista
    @Test
    void findExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos); //Cuando llamemos al findAll de repository se le dara la lista de prueba
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");


        //Comprobamos que existe el examen y que el id coincide
        assertFalse(examen.isPresent());
    }

    //Test para probar el total de preguntas en el examen
    @Test
    void TestPreguntasExamen() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
    }

    //Test para verificar que se utilizo el metodo que queremos
    @Test
    void TestPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
        verify(repository).findAll(); //Verificamos que se llama el findAll
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    @Disabled
    void TestNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas2");
        assertNull(examen);
        verify(repository).findAll(); //Verificamos que se llama el findAll
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testGuardarExamen() {
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //implemetnamos un metodo de respuesta para el when y simulamos el objeto
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });
        Examen examen = service.guardarExamen(newExamen);
        assertNotNull(examen.getId());
        assertEquals(8,examen.getId());
        assertEquals("Fisica",examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        assertEquals(IllegalArgumentException.class,exception.getClass());
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(null);
    }
    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(ArgumentMatchers.argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findPreguntasPorExamenId(ArgumentMatchers.argThat(arg -> arg != null && arg>=5L));
    }

}