package ar.edu.unq.epersgeist.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private Driver neo4jDriver;

    @Override
    public void run(String... args) throws Exception {
        try (Session session = neo4jDriver.session()) {
            String query = "CREATE CONSTRAINT IF NOT EXISTS FOR (h:Habilidad) REQUIRE h.nombre IS UNIQUE";
            session.run(query);
        }
    }
}
