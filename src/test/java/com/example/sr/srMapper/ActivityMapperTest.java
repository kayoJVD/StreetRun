package com.example.sr.srMapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityMapperTest {

    // Instanciamos a classe real gerada pelo MapStruct para testar o motor dela
    private final ActivityMapperImpl mapper = new ActivityMapperImpl();

    @Test
    void deveCalcularOPaceCorretamente() {
        // GIVEN (Cenário): Os exatos dados da corrida da Danielly
        Integer durationInSeconds = 1800; // 30 minutos
        Double distanceInKm = 5.5;

        // WHEN (Ação): Mandamos o motor calcular
        String resultado = mapper.calculatePace(durationInSeconds, distanceInKm);

        // THEN (Validação): O resultado OBRIGATORIAMENTE tem que ser "05:27"
        // Se a lógica matemática quebrar no futuro, esse teste vai explodir e avisar!
        assertEquals("05:27", resultado, "O Pace calculado está matematicamente incorreto!");
    }

    @Test
    void deveRetornarZeroQuandoHouverDivisaoPorZero() {
        // GIVEN/WHEN/THEN juntos: Testando a blindagem contra erros fatais do Java
        // Se a distância for 0, o sistema não pode dar erro "ArithmeticException", tem que devolver 00:00.
        assertEquals("00:00", mapper.calculatePace(1800, 0.0), "Falhou ao tratar distância zero");

        // E se o celular não mandar o tempo?
        assertEquals("00:00", mapper.calculatePace(null, 5.5), "Falhou ao tratar tempo nulo");

        // E se não mandar a distância?
        assertEquals("00:00", mapper.calculatePace(1800, null), "Falhou ao tratar distância nula");
    }
}
