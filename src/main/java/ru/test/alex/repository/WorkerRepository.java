package ru.test.alex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.alex.entity.Worker;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {

    Optional<Worker> findByName(String name);

}
