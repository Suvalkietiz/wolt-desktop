package com.example.programavimotechnologijosprif.model.consoleCourseWork;

import com.example.programavimotechnologijosprif.model.User;

import java.util.Iterator;
import java.util.Scanner;


public class MenuControl {
    public static void generateUserMenu(Scanner scanner, Wolt wolt) {
        var cmd = 0;
        while (cmd != 6) {
            System.out.println("""
                    Choose and option:
                    1 - create
                    2 - view all users
                    3 - view user
                    4 - update user
                    5 - delete user
                    6 - return to main menu
                    """);
            cmd = scanner.nextInt();
            scanner.nextLine();

            switch (cmd) {
                case 1:
                    System.out.println("Enter Client data (Client class):login;password;name;surname;phoneNum;address;");
                    var input = scanner.nextLine();
                    String[] info = input.split(";");
                    User newUser = new User(info[0], info[1], info[2], info[3], info[4]);
                    wolt.getAllSysUsers().add(newUser);
                    break;
                case 2:
                    System.out.println("Visi vartotojai:");
                    for(User u : wolt.getAllSysUsers()) {
                        System.out.println(u);
                    }
                    break;
                case 3:
                    System.out.println("enter login: ");
                    var login = scanner.nextLine();
                    for(User u : wolt.getAllSysUsers()) {
                        if(u.getLogin().equals(login)){
                            System.out.println("Jusu vartotojas: " + u);
                        }
                    }
                    break;
                case 4:
                    System.out.println("enter login: ");
                    var loginUpdate = scanner.nextLine();
                    for(User u : wolt.getAllSysUsers()) {
                        if(u.getLogin().equals(loginUpdate)){
                            System.out.println("Enter new data for " + u.getName()+ "\tname;surname");
                            String [] infoUpdate = scanner.nextLine().split(";");
                            u.setName(infoUpdate[0]);
                            u.setSurname(infoUpdate[1]);
                            System.out.println(u);

                        }
                    }
                    break;
                case 5:
                    System.out.println("enter login: ");
                    var loginDelete = scanner.nextLine();
                    Iterator<User> iterator = wolt.getAllSysUsers().iterator();
                    while(iterator.hasNext()){
                        User u = iterator.next();
                        if(u.getLogin().equals(loginDelete)){
                            iterator.remove();
                        }
                    }
                    break;
                default:
                    System.out.println();
            }
        }
    }
}
