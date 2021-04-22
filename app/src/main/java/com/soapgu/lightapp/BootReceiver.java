package com.soapgu.lightapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BootReceiver extends BroadcastReceiver {

    private Lightbar lightbar;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Logger.i( "----Receive ACTION_BOOT_COMPLETED----" );
            Toast toast = Toast.makeText(context, "-----BOOT_COMPLETED,Wait for Light----" , Toast.LENGTH_SHORT);
            toast.show();

            this.lightbar = LightFns.getInstance();
            boolean isOpen = lightbar.getOpened();
            if( !isOpen ) {
                try {
                    this.lightbar.open();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            disposables.add(Observable.intervalRange(1,2, 3,3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( t-> {
                        Logger.i( "interval parameter:%s",t );
                        if( t == 1 ) {
                            this.lightbar.echo();
                            Logger.i("-------Sent Red Light Echo---------");
                        }
                        else {
                            this.lightbar.green("HIGH");
                            Logger.i("-------Sent Red Light Complete---------");
                            Toast sendToast = Toast.makeText(context, "Red Light Send", Toast.LENGTH_SHORT);
                            sendToast.show();
                        }
                    },
                    e-> Logger.e(e, "Light error"),
                    ()->{
                        if( !isOpen ){
                            this.lightbar.close();
                        }
                        disposables.clear();
                    }));


        }
    }
}
