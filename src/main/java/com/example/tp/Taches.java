package com.example.tp;

abstract class Taches {
    protected String name;
    protected int priorite ;
    //protected Categorie categorie ;
    protected Etat etat;
    protected String duree ;

    public void setName(String Name){
        this.name=Name ;
    }
    public String getName(){
        return  this.name;
    }
    public void setPriorite(int prio){
        this.priorite=prio ;
    }  public int getPriorite(){
        return this.priorite;

    }  public Etat getEtat(){
        return this.etat;
    }
    public void setEtat(Etat etat1){
        this.etat=etat1;
    }
    public void setDuree(String Duree){
        this.duree= Duree;
    }
    public String getDuree(){
        return this.duree;
    }



}
