package com.example.tp;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class system implements Serializable{

    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public system() {
        users = new HashSet<>();
    }

    public void addUser(User user) {
        users.add(user);
    }


    public boolean authenticate(String username, String password) {
        for (User user : users) {
            System.out.println(user.getPassward());
            if (user.getPseudo().equals(username) ) {
                System.out.println(user.getPseudo());
                if (BCrypt.checkpw(password, user.getPassward())) {
                    return true;
                }
            }
        }
        return false;
    }
    public void SaveListUsers() throws IOException {

        FileOutputStream fileout = new FileOutputStream(System.getProperty("user.home")+"\\MyDesktopPlanner\\Systeme"+"\\Systeme-info.bin");
        ObjectOutput out = new ObjectOutputStream(fileout);
        out.writeObject(this);
        out.close();
        fileout.close();
    }
    public void LoadListUsers() throws IOException, ClassNotFoundException {
        system s;
        FileInputStream filein = new FileInputStream(System.getProperty("user.home")+"\\MyDesktopPlanner\\Systeme"+"\\Systeme-info.bin");
        ObjectInput in = new ObjectInputStream(filein);
        s = (system) in.readObject();
        this.users=s.getUsers();
        filein.close();
        in.close();
    }
}
