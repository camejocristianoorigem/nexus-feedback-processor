package nexus.feedback.processor.simulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FeedbackLoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8081")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    Supplier<Map<String, Object>> supplier = () -> Map.of(
        "title", "Feedback Automático Gatling " + UUID.randomUUID().toString().substring(0, 8),
        "description", "Performance stress testing continuous integration pipelines inside virtual threads environment.",
        "customerEmail", "stress_test@nexus.io"
    );

    ScenarioBuilder scn = scenario("Carga Concorrente Nexus Feedback")
        .feed(StreamFeeder.generate(supplier))
        .exec(
            http("POST Novo Feedback")
                .post("/api/v1/feedbacks")
                .body(StringBody("{\"title\":\"#{title}\",\"description\":\"#{description}\",\"customerEmail\":\"#{customerEmail}\"}"))
                .check(status().is(201))
        )
        .pause(1)
        .exec(
            http("GET Listar Todos")
                .get("/api/v1/feedbacks")
                .check(status().in(200, 202)) // Corrigido: Aceita tanto 200 quanto 202 nativamente
        );

    {
        setUp(
            scn.injectOpen(
                nothingFor(2),
                rampUsers(10).during(5),    // Aquecimento lento
                constantUsersPerSec(20).during(15) // Sustentação de carga contínua
            )
        ).protocols(httpProtocol);
    }
}

class StreamFeeder {
    public static <T> Iterator<T> generate(Supplier<T> supplier) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return true; }
            @Override
            public T next() { return supplier.get(); }
        };
    }
}
