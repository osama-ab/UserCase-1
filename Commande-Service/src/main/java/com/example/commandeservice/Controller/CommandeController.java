package com.example.commandeservice.Controller;


import com.example.commandeservice.Configuration.ApplicationPropertiesConfiguration;
import com.example.commandeservice.Dao.CommandeDao;
import com.example.commandeservice.Model.Commande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CommandeController implements HealthIndicator {

            private final ApplicationPropertiesConfiguration appConfig ;
            private final CommandeDao commandeDao ;

    @Autowired
    public CommandeController(ApplicationPropertiesConfiguration appConfig, CommandeDao commandeDao) {
        this.appConfig = appConfig;
        this.commandeDao = commandeDao;
    }


 @GetMapping("/Commandes")

 public List<Commande>getLastCommande(){



     List<Commande> dernieresCommandes = commandeDao.findAll().stream()
             .sorted(Comparator.comparing(Commande::getDate).reversed())
             .limit(appConfig.getcommandeslast())
             .collect(Collectors.toList());
     System.out.println(appConfig.getcommandeslast());

     return dernieresCommandes;



 }

    @GetMapping("/Commandeslast")

    public List<Commande>last(){

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusDays(appConfig.getcommandeslast());


        List<Commande> commandesLast20Days = new ArrayList<>();
        List <Commande> allCommandes = commandeDao.findAll();

        for (Commande commande : allCommandes) {
            LocalDate dateCommande = commande.getDate();
            if (dateCommande.isAfter(startDate) || dateCommande.isEqual(startDate) &&
                    (dateCommande.isBefore(currentDate) || dateCommande.isEqual(currentDate))) {
                commandesLast20Days.add(commande);
            }
        }

return commandesLast20Days ;

    }





    @Override
    public Health health() {
        List<Commande> products = commandeDao.findAll();
        if (products.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
    }

