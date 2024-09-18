package com.prac4server.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CarService {
    private final CarRepository carRepository;
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car newCar(Car car) {
        carRepository.save(car);
        return car;
    }

    public Car getCarById(int id) {
        return carRepository.findById(id).orElse(null);
    }

    public Flux<Car> getCarAll() {
        return Flux.fromIterable(carRepository.findAll());
    }

    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }

    public Flux<Car> getChannel(Flux<Car> cars) {
        return cars.flatMap(car -> Mono.fromCallable(() -> carRepository.save(car)))
                .collectList()
                .flatMapMany(savedCats -> Flux.fromIterable(savedCats));
    }
}
