package com.mobility.inclass07.utilities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {

    private static FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Auth(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }


    public static String getCurrentUserID(){
        return  getAuth().getCurrentUser().getUid();
    }

    public static boolean signOutUser(){
        try {
            getAuth().signOut();
            mAuth = null;
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
