package com.example.tp;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class TacheDecomposee extends Taches  {


    private List<TacheSimple> Simple;

    public List<TacheSimple> getSimple() {
        return Simple;
    }

    public void setSimple(List<TacheSimple> simple) {
        Simple = simple;
    }
    public   List<Creneau>  DecmposerTache (Journee jour, Planning plan){
        User user = UserManager.getUser();
        Creneau creneau1= plan.FindCreneauTacheSimple(this);
        LocalDate date = jour.getDate() ;
        Duration duree = this.duree;
        List<Creneau> listeCreneau= new ArrayList<>();
        int i=1;
        if (creneau1== null) {
            while (!date.equals(plan.getDateFin())|| duree.isZero()){
                for(Creneau cr: jour.getCreneauxLibres() ){
                    Duration dureeCreneau= Duration.between(creneau.getHeureDebut(),creneau.getHeureFin());
                    Taches tache= new TacheSimple();
                    tache=this;
                    tache.setDuree(duree.minus(dureeCreneau));
                    tache.setName(this.getName()+i);
                    i++;
                    Simple.add((TacheSimple) tache);
                    jour.addCreneauPris(cr);
                    jour.suppCreneauLibre(cr);
                    listeCreneau.add(cr);
                    duree= duree.minus(dureeCreneau);
                    if (duree.isZero()){
                        return listeCreneau;
                    }
                }
            }
            System.out.println("la tache was not programmed ");
            user.getUnsheduledTaches().add(this);
            return null ;
        }else{
            listeCreneau.add(creneau1);
            System.out.println("la tache peut se programmer dans un seul creneau");
            return listeCreneau;
        }
    }
}
       /* @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NotNull
        @Override
        public Iterator<TacheSimple> iterator() {
            return null;
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return null;
        }

        @Override
        public boolean add(TacheSimple tacheSimple) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends TacheSimple> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NotNull Collection<? extends TacheSimple> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public TacheSimple get(int index) {
            return null;
        }

        @Override
        public TacheSimple set(int index, TacheSimple element) {
            return null;
        }

        @Override
        public void add(int index, TacheSimple element) {

        }

        @Override
        public TacheSimple remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NotNull
        @Override
        public ListIterator<TacheSimple> listIterator() {
            return null;
        }

        @NotNull
        @Override
        public ListIterator<TacheSimple> listIterator(int index) {
            return null;
        }

        @NotNull
        @Override
        public List<TacheSimple> subList(int fromIndex, int toIndex) {
            return null;
        }
    };*/