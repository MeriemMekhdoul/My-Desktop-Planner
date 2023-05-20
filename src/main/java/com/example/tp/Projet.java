package com.example.tp;

import com.example.tp.Taches;

import java.util.List;

public class Projet {
    public List<Taches> tache;

    public List<Taches> getTache() {
        return tache;
    }

    public void setTache(List<Taches> tache) {
        this.tache = tache;
    }
    public  void addTache(Taches newTache){
        tache.add(newTache);
    }
    public void SuppTahce(Taches suppTache){
        tache.remove(suppTache);
    }
}
