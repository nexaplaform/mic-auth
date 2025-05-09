package com.nexaplatform.api.services;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/users")
public interface BaseApi<I, O, K> {

    @PostMapping
    @Operation(
            summary = "Create record",
            description = "Creation of record"
    )
    default ResponseEntity<O> create(@RequestBody I dto) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping
    @Operation(
            summary = "Get paginated records",
            description = "Get a number of paginated records"
    )
    default ResponseEntity<List<O>> getPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Sort.Direction sort) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get record by id",
            description = "Search a record by id"
    )
    default ResponseEntity<O> getById(@PathVariable(value = "1") K id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update record",
            description = "Update a record by id"
    )
    default ResponseEntity<O> update(
            @PathVariable(value = "1") K id,
            @RequestBody I user) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete record",
            description = "Delete a record by id"
    )
    default ResponseEntity<Void> delete(@PathVariable(value = "1") K id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
