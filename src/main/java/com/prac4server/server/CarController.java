package com.prac4server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/car")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @MessageMapping("addCar")
    public Mono<Car> createCar(@RequestBody Car car) {
        return Mono.justOrEmpty(carService.newCar(car));
    }

    @MessageMapping("getCar")
    public Mono<Car> getCar(@PathVariable Integer id) {
        return Mono.justOrEmpty(carService.getCarById(id));
    }

    @MessageMapping("getCars")
    public Flux<Car> getAll() {
        return carService.getCarAll();
    }

    @MessageMapping("deleteCar")
    public Mono<Void> deleteCar(@PathVariable Integer id) {
        carService.deleteCarById(id);
        return Mono.empty();
    }

    @MessageMapping("carChannel")
    public Flux<Car> getChannel(Flux<Car> cars) {
        return carService.getChannel(cars);
    }
}
