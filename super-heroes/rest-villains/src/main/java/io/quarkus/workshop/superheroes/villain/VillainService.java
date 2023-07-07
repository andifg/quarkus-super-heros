package io.quarkus.workshop.superheroes.villain;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;


@ApplicationScoped
@Transactional(REQUIRED)
public class VillainService {

    @PostConstruct
    void onStartup() {
        System.out.println("VillainService starting...");
    }

    @Transactional(SUPPORTS)
    public List<Villain> findAllVillains() {
        return Villain.listAll();
    }

    @Transactional(SUPPORTS)
    public Villain findVillainById(Long id) {
        return Villain.findById(id);
    }

    @Transactional(SUPPORTS)
    public Villain findRandomVillain() {
        return Villain.findRandom();
    }

    public Villain persistVillain(Villain villain) {
        Villain.persist(villain);
        return villain;
    }

    public Villain updateVillain(@Valid Villain villain){

        Villain entity = Villain.findById(villain.id);
        entity.name = villain.name;
        entity.otherName = villain.otherName;
        entity.level = villain.level;
        entity.picture = villain.picture;
        entity.powers = villain.powers;
        return entity;

    }

    public void deleteVillain(Long id) {
        Villain villain = Villain.findById(id);
        villain.delete();
    }

}
