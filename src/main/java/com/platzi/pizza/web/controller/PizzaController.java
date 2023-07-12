package com.platzi.pizza.web.controller;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.service.PizzaService;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {
    private final PizzaService pizzaService;
    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }
    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int elements
    ) {
        return ResponseEntity.ok(pizzaService.getAll(page,elements));
    }
    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int elements,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ){
        return ResponseEntity.ok(pizzaService.getAvailable(page,elements,sortBy,sortDirection));
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Optional<PizzaEntity>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(pizzaService.getByName(name));
    }
    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getByIngredient(@PathVariable String ingredient) {
        return ResponseEntity.ok(pizzaService.getByIngredient(ingredient));
    }
    @GetMapping("/with-not/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getByNotIngredient(@PathVariable String ingredient) {
        return ResponseEntity.ok(pizzaService.getByNotIngredient(ingredient));
    }
    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getCheapestPizzas(@PathVariable double price) {
        return ResponseEntity.ok(this.pizzaService.getCheapest(price));
    }
    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> get(@PathVariable int idPizza) {
        return pizzaService.exists(idPizza) || pizzaService.get(idPizza) == null
                ? ResponseEntity.ok(pizzaService.get(idPizza))
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PizzaEntity> add(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza) {
        if (pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza())) {
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }

        return ResponseEntity.badRequest().build();
    }
    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto) {
        if (this.pizzaService.exists(dto.getPizzaId())) {
            this.pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable int idPizza){
        if(pizzaService.exists(idPizza)){
            pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.notFound().build();
    }
}
