package com.example.tp;

import java.io.File;

public class FileMyDestcktopPlanner {
    public  boolean CreerDossier(String LienCreation, String NomDossier) {

        // creer le dossier
        File folder = new File(LienCreation + "\\" + NomDossier);
        boolean result = folder.mkdir();
        return result;

    }

    public  void CreerDossierDescktopPlanner() {
        boolean result;
        result = CreerDossier(System.getProperty("user.home"), "MyDesktopPlanner");
        if(result){
            CreerDossier(System.getProperty("user.home")+"\\MyDesktopPlanner","UserInfo");
            CreerDossier(System.getProperty("user.home")+"\\MyDesktopPlanner","Systeme");
        }


    }

}
