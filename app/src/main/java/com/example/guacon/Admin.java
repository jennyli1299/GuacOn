package com.example.guacon;

public class Admin extends User {
    private boolean admin = true;

    public Admin(){
        super();
//        admin = true;
    }

    public Admin(String fN, String lN) {
        super(fN, lN);
//        admin = true;
    }

}
