package com.example.organizzeclone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizzeclone.R;
import com.example.organizzeclone.database.FirebaseManager;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseManager.getFirebaseAuth().signOut();
        initSlides();
    }

    private void initSlides() {
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_intro_1)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_intro_4)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_intro_cadastro)
                .canGoForward(false)
                .build()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseManager.getFirebaseAuth().getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    public void startCadastroActivity(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void startLoginActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
