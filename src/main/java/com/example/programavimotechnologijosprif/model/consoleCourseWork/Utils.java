package com.example.programavimotechnologijosprif.model.consoleCourseWork;

import com.example.programavimotechnologijosprif.model.AppUser;

import java.io.*;

public class Utils {
    public static void writeAppUserToFile(AppUser appUser) {
        ObjectOutputStream out = null;
        try(var file = new FileOutputStream("o.txt")) {
            out = new ObjectOutputStream(new BufferedOutputStream(file));
            out.writeObject(appUser);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException is caught");
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
//       ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(newFileInputStream("o.txt")));
//        Object o2 = in.readObject();
//        in.close();
    }

    public static void writeWoltToFile(Wolt wolt) {
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt")));
            out.writeObject(wolt);
        } catch(IOException e) {
            e.printStackTrace();
        } finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Wolt readWoltFromFile() {
        ObjectInputStream in = null;
        Wolt wolt = null;
        try{
             in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt")));
             wolt = (Wolt)in.readObject();
        } catch(EOFException e){
            // This is fine
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        return wolt;
    }




}
