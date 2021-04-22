package com.soapgu.lightapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private Lightbar lightbar;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lightbar = LightFns.getInstance();

        this.findViewById(R.id.button_green ).setOnClickListener( v -> lightbar.green("HIGH"));

        this.findViewById(R.id.button_red ).setOnClickListener( v -> lightbar.red("HIGH"));

        this.findViewById(R.id.button_yellow ).setOnClickListener( v -> lightbar.yellow("HIGH"));
    }

    @Override
    protected void onResume() {

        try {
            this.lightbar.open();
            this.lightbar.echo();
            disposables.add(
                    Single.timer( 3 , TimeUnit.SECONDS )
                            .subscribe( t-> this.lightbar.echo() )
            );

        } catch (IOException e) {
            Logger.e( e, "Open Light SP error" );
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        this.disposables.clear();
        this.lightbar.close();
        super.onStop();
    }
}