package ru.test.alex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "worker_t")
@AllArgsConstructor
@NoArgsConstructor
public class Worker {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private Integer cash;

}
