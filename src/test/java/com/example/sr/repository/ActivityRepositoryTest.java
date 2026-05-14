package com.example.sr.repository;

import com.example.sr.domain.Activity;
import com.example.sr.domain.Sports;
import com.example.sr.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ActivityRepositoryTest {


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgis/postgis:15-3.3").asCompatibleSubstituteFor("postgres")
    );

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SportsRepository sportsRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setName("Kayo Atleta");
        user.setEmail("kayo.testcontainers@alves.com");
        user.setPassword("123456");
        user.setWeight(75.0);
        user.setHeight(1.80);
        savedUser = userRepository.save(user);

        Sports sport = new Sports();
        sport.setName("Corrida");
        sportsRepository.save(sport);


        Activity run1 = new Activity();
        run1.setUser(savedUser);
        run1.setSports(sport);
        run1.setDistance(5.5); // 5.5 km
        run1.setDuration(1500);
        run1.setDate(LocalDate.now());
        activityRepository.save(run1);

        Activity run2 = new Activity();
        run2.setUser(savedUser);
        run2.setSports(sport);
        run2.setDistance(10.0); // 10 km
        run2.setDuration(3000);
        run2.setDate(LocalDate.now());
        activityRepository.save(run2);
    }

    @Test
    @DisplayName("sumTotalDistanceByUserId should return the exact mathematical sum from PostgreSQL")
    void sumTotalDistanceByUserId_ReturnsExactSum() {

        Double totalDistance = activityRepository.sumTotalDistanceByUserId(savedUser.getId());

        Assertions.assertThat(totalDistance).isEqualTo(15.5);
    }

    @Test
    @DisplayName("findTopByUserIdOrderByDistanceDesc should return the 10km run")
    void findTopByUserIdOrderByDistanceDesc_ReturnsLongestRun() {

        var longestRunOptional = activityRepository.findTopByUserIdOrderByDistanceDesc(savedUser.getId());

        Assertions.assertThat(longestRunOptional).isPresent();
        Assertions.assertThat(longestRunOptional.get().getDistance()).isEqualTo(10.0);
    }

    @org.springframework.test.context.DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("It should return empty when the user has no registered rides.")
    void returnsEmptyOrNull_WhenUserHasNoActivities() {

        User lazyUser = new User();
        lazyUser.setName("Corredor pipoca");
        lazyUser.setEmail("pipoca@teste.com");
        lazyUser.setPassword("123456");
        lazyUser.setWeight(80.0);
        lazyUser.setHeight(1.75);
        User savedLazyUser = userRepository.save(lazyUser);


        var longestRunOptional = activityRepository.findTopByUserIdOrderByDistanceDesc(savedLazyUser.getId());
        Double totalDistance = activityRepository.sumTotalDistanceByUserId(savedLazyUser.getId());

        Assertions.assertThat(longestRunOptional).isEmpty();
        Assertions.assertThat(totalDistance).isNull();
    }
}
