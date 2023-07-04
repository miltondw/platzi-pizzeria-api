package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.pizza.persistence.repository.PizzaPageSortRepository;
import com.platzi.pizza.persistence.repository.PizzaRepository;
import com.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class PizzaService {
/*
    //Usando jdbcTemplate

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PizzaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PizzaEntity> getAll(){
        return this.jdbcTemplate.query("SELECT * FROM pizza",new BeanPropertyRowMapper<>(PizzaEntity.class));
    }*/
    public final PizzaRepository pizzaRepository;
    public final PizzaPageSortRepository pizzaPageSortRepository;

    public PizzaService(PizzaRepository pizzaRepository, PizzaPageSortRepository pizzaPageSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPageSortRepository = pizzaPageSortRepository;
    }
    //Gets
    public Page<PizzaEntity> getAll(int page, int elements){
        return pizzaPageSortRepository.findAll(PageRequest.of(page,elements));
    }
    public Page<PizzaEntity> getAvailable(int page, int elements,String sortBy, String sortDirection){
        System.out.println(this.pizzaRepository.countByVeganTrue());
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortBy);
        return pizzaPageSortRepository.findAllByAvailableTrue(PageRequest.of(page,elements,sort));
    }
    public Optional<PizzaEntity> getByName(String name){
        return Optional.ofNullable(pizzaRepository
                .findFirstByAvailableTrueAndNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("pizza does not exist")));
    }
    public List<PizzaEntity> getByIngredient(String description){
        return pizzaRepository
                .findAllByAvailableTrueAndDescriptionContainingIgnoreCase(description);
    }
    public List<PizzaEntity> getByNotIngredient(String description){
        return pizzaRepository
                .findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(description);
    }
    public List<PizzaEntity> getCheapest(double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity get(int idPizza){
        return pizzaRepository.findById(idPizza).orElse(null);
    }
    //Post
    public PizzaEntity save(PizzaEntity pizza){
        return pizzaRepository.save(pizza);
    }
    //Delete
    public void delete(int idPizza){
        pizzaRepository.deleteById(idPizza);
    }
    //Put
    @Transactional
    public void updatePrice(UpdatePizzaPriceDto dto) {
        this.pizzaRepository.updatePrice(dto);
    }
    //Util
    public boolean exists(int idPizza){
        return pizzaRepository.existsById(idPizza);
    }


}
