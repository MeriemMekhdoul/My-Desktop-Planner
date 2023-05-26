package com.example.tp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Encouragement implements Serializable {

    private User user=UserManager.getUser();
    private  int BadgeGood=0 ;
    private  int BadgeVeryGood= 0 ;
    private  int BadgeExcellent=0 ;
    private int journeeConsecutive;



    public int countCompletedTasks(Journee journee) {

        int completedCount = 0;
        System.out.println("check the state"+journee.getTacheList().size());
        for (Taches tache : journee.getTacheList()) {
            if (tache.getEtat() == Etat.COMPLETED) {
                completedCount++;
            }
        }
        return completedCount;
    }
    public void updateBadges(Taches tache,Planning planning) {
        user=UserManager.getUser();
        user.getBadges().forEach((badge, value) -> {
            System.out.println("Badge:********** " + badge.name() + ", Value: " + value);
        });//elle est vide
        user.getBadges().clear();
        planning.getBadges().clear();
        int consecutiveGoodCount = 0;
        LocalDate dateF =tache.getCreneau().getDate();
        LocalDate dateD = planning.getDateDebut() ;
        int year = dateD.getYear();

        Calendrier cald= user.getCalendar(year);
        System.out.println("JE UPDATE LES BADGE"+dateD);
        while (!dateD.isAfter(dateF)) {
            Journee journee = cald.getJournee(dateD);
            int completedCount = countCompletedTasks(journee);
            System.out.println(completedCount);
            if (completedCount >= user.getMinTaskDaily()) {
                // Update badge for Good
                    System.out.println("GOOD"+completedCount);
                    user.addBadge(Badge.GOOD);
                    planning.addBadge(Badge.GOOD);
                    consecutiveGoodCount++;
                    System.out.println("consecutive count="+consecutiveGoodCount);
                    // Check if consecutive Good badges reach 3
                    if (consecutiveGoodCount == 5) {
                        System.out.println("VeryGood"+BadgeVeryGood);
                        user.addBadge(Badge.VERYGOOD);
                        planning.addBadge(Badge.VERYGOOD);
                        consecutiveGoodCount = 0;// prq??
                         BadgeVeryGood++;
                    }
                if ( BadgeVeryGood== 3) {
                    System.out.println("Excellent");
                    user.addBadge(Badge.EXCELLENT);
                    planning.addBadge(Badge.EXCELLENT);
                }
            } else {
                consecutiveGoodCount = 0;
                BadgeVeryGood = 0;
            }
            System.out.println("badge de la journee"+ BadgeVeryGood+"    "+completedCount);
            dateD = dateD.plusDays(1);
        }
        System.out.println("Je suis dans la fin de update badges");
    }

}
