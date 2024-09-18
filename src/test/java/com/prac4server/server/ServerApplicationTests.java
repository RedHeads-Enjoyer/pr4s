package com.prac4server.server;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.util.MimeTypeUtils;
import static org.junit.jupiter.api.Assertions.*;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;


@SpringBootTest
class ServerApplicationTests {

	private CarRepository carRepository;

	private RSocketRequester requester;

	@BeforeEach
	public void setup() {
		requester = RSocketRequester.builder()
				.rsocketStrategies(builder -> builder.decoder(new
						Jackson2JsonDecoder()))
				.rsocketStrategies(builder -> builder.encoder(new
						Jackson2JsonEncoder()))
				.rsocketConnector(connector -> connector
						.payloadDecoder(PayloadDecoder.ZERO_COPY)
						.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
				.dataMimeType(MimeTypeUtils.APPLICATION_JSON)
				.tcp("localhost", 5200);
	}
	@AfterEach
	public void cleanup() {
		requester.dispose();
	}



	@Test
	void testGetCar() {
		Car car = new Car();
		car.setColor("red");
		car.setModel("Mazda");
		car.setPrice(10000f);

		Car savedCar = carRepository.save(car);

		Mono<Car> result = requester.route("getCar")
				.data(savedCar.getId())
				.retrieveMono(Car.class);
		assertNotNull(result.block());
	}

}
