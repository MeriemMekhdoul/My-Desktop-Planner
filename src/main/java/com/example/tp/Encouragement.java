package com.example.tp;

import java.io.Serializable;
import java.time.LocalDate;

public class Encouragement implements Serializable {

    private User user=UserManager.getUser();
    private  int BadgeGood=0 ;
    private  int BadgeVeryGood= 0 ;
    private  int BadgeExcellent=0 ;
    private int journeeConsecutive;



    public int countCompletedTasks(Journee journee) {
        int completedCount = 0;
        for (Taches tache : journee.getTacheList()) {
            if (tache.getEtat() == Etat.COMPLETED) {
                completedCount++;
            }
        }
        return completedCount;
    }
    public void updateBadges(Taches tache,Planning planning) {
        int consecutiveGoodCount = 0;
        LocalDate dateD =tache.creneau.getDate();
        int year = dateD.getYear();//Revoir apres
        user=UserManager.getUser();
        Calendrier cald= user.getCalendar(year);
        LocalDate dateFin=planning.getDateFin();
        while (!dateD.isAfter(dateFin)) {
            Journee journee = cald.getJournee(dateD);
            int completedCount = countCompletedTasks(journee);
            if (completedCount >= 3) {
                // Update badge for Good
                System.out.println("GOOD");
                    user.addBadge(Badge.GOOD);
                    consecutiveGoodCount++;
                    // Check if consecutive Good badges reach 3
                    if (consecutiveGoodCount == 5) {
                        System.out.println("VeryGood");
                        user.addBadge(Badge.VERYGOOD);
                        consecutiveGoodCount = 0;// prq??
                         BadgeVeryGood++;
                    }
                if ( BadgeVeryGood== 3) {
                    System.out.println("Excellent");
                    user.addBadge(Badge.EXCELLENT);
                }
            } else {
                consecutiveGoodCount = 0;
                BadgeVeryGood = 0;
            }
            dateD = dateD.plusDays(1);
        }
    }

}
