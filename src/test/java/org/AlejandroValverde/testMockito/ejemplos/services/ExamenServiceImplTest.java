package org.AlejandroValverde.testMockito.ejemplos.services;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;
import org.AlejandroValverde.testMockito.ejemplos.repositories.ExamenRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    //importamos de forma static la clase mockito para usarla con mas facilidad
    @Test
    void findExamenPorNombre() {
        ExamenRepository repository = mock(ExamenRepository.class); //Simulamos una instancia con los metodos
        ExamenService service = new ExamenServiceImpl(repository);
        List<Examen> datos = Arrays.asList(new Examen(5L,"Matematicas"),
                new Examen(6L,"Lengua"),
                new Examen(7L,"Historia"),
                new Examen(8L,"Fisica"));

        when(repository.findAll()).thenReturn(datos); //Cuando llamemos al findAll de repository se le dara la lista de prueba
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");


        //Comprobamos que existe el examen y que el id coincide
        assertTrue(examen.isPresent());
        assertEquals(5L,examen.orElseThrow().getId());
        assertEquals("Matematicas",examen.orElseThrow().getNombre());
    }


    //Como funcionaria si viene vacia la lista
    @Test
    @Disabled
    void findExamenPorNombreListaVacia() {
        ExamenRepository repository = mock(ExamenRepository.class); //Simulamos una instancia con los metodos
        ExamenService service = new ExamenServiceImpl(repository);
        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos); //Cuando llamemos al findAll de repository se le dara la lista de prueba
        Optional<Examen> examen = service.findExamenPorNombre("Matematicas");


        //Comprobamos que existe el examen y que el id coincide
        assertTrue(examen.isPresent());
        assertEquals(5L,examen.orElseThrow().getId());
        assertEquals("Matematicas",examen.orElseThrow().getNombre());
    }
}