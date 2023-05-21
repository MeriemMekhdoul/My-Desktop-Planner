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
        LocalDate date =tache.creneau.getDate();
        int year = date.getYear();
        user=UserManager.getUser();
        Calendrier cald= user.getCalendar(year);
        Journee journee1=cald.getJournee(date);
        int startingDay = planning.getJourneeList().lastIndexOf(journee1);
        for (int i= startingDay ;i<planning.getJourneeList().size();i++ ) {
            Journee journee = planning.getJourneeList().get(i);
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
        }
    }

}
