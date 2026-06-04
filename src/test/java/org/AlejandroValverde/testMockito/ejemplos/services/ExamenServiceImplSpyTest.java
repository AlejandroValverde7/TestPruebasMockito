package org.AlejandroValverde.testMockito.ejemplos.services;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;
import org.AlejandroValverde.testMockito.ejemplos.repositories.ExamenRepository;
import org.AlejandroValverde.testMockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.AlejandroValverde.testMockito.ejemplos.repositories.PreguntaRepository;
import org.AlejandroValverde.testMockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class) //Con esto tambien podemos usar anotaciones y tener la dependencia de mock junit
class ExamenServiceImplSpyTest {

    //Gracias a las anotaciones mock no necesitamos usar new ni nada asi
    @Spy
    ExamenRepositoryImpl repository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;

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

    @Test
    void testArgumentMatchers2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    //creamos la clase personalizada de error la cual es una implementacion del matchers
    public static class MiArgsMatchers implements ArgumentMatcher<Long>{

        private  Long argument;

        @Override
        public boolean matches(Long argument){
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString(){
            return "es  para un mensaje personalizado de error " +
                    "que imprime mockito ewn caso de que falle el test"
                    + " debe ser un entero positivo";
        }

//        @Test
//        void testArgumentCaptor() {
//            when(repository.findAll()).thenReturn(Datos.EXAMENES);
//            service.findExamenPorNombreConPreguntas("Matematicas");
//
//            ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
//            verify(PreguntaRepository)
//        }

    }

    @Test
    void testDoThrow() {
        //Lo lanzamos cuando se invoca el metodo con anylist
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
       doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

       assertThrows(IllegalArgumentException.class, () -> {
           service.guardarExamen(examen);
       });
    }

    @Test
    void testDoAnswer(){
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(Invocation -> {
           Long id = Invocation.getArgument(0);
           return id == 5L? Datos.PREGUNTAS: null;
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L,examen.getId());
        assertEquals("Matematicas",examen.getNombre());
        assertEquals(5,examen.getPreguntas().size());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testDoAnswerGuardarExamen() {
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //implemetnamos un metodo de respuesta para el when y simulamos el objeto
        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        Examen examen = service.guardarExamen(newExamen);
        assertNotNull(examen.getId());
        assertEquals(8,examen.getId());
        assertEquals("Fisica",examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    //para usar el docallrealmethjor necesitamos tener implemantado el metodo que vamos a utilizar
    //si usamos la impl, usara el metodo real para probar el real method
    @Test
    void testDoCallRealMethod(){
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
    doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L,examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void testSpy(){
        //Indicamos la Impl para el spy ya el uso de estos es real
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("aritmetica");
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);

        //Usamos los metodos reales
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5,examen.getId());
        assertEquals("Matematicas",examen.getNombre());
        assertEquals(5,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepository.findAll());
        verify(preguntaRepository.findPreguntasPorExamenId(anyLong()));
    }
}


