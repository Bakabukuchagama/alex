package ru.test.alex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.alex.entity.Worker;
import ru.test.alex.repository.WorkerRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    @PostConstruct
    protected void init() {
        workerRepository.save(Worker.builder()
                .cash(100)
                .name("Чувак")
                .build());
        workerRepository.save(Worker.builder()
                .cash(0)
                .name("Саня")
                .build());
        workerRepository.save(Worker.builder()
                .cash(35)
                .name("Николай")
                .build());
        workerRepository.save(Worker.builder()
                .cash(1000)
                .name("Контора")
                .build());
    }

    @Transactional
    public Worker updateCash(String name, Integer cash) {
        Optional<Worker> worker = workerRepository.findByName(name);
        if (worker.isEmpty()) {
            return null;
        }
        worker.get().setCash(worker.get().getCash() + cash);
        return worker.get();
    }

    public List<Worker> getAll() {
        return workerRepository.findAll();
    }
}
