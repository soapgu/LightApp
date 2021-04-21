package com.soapgu.lightapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BootReceiver extends BroadcastReceiver {

    private Lightbar lightbar;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Logger.i( "----Receive ACTION_BOOT_COMPLETED----" );
            Toast toast = Toast.makeText(context, "-----BOOT_COMPLETED,Wait for Light----" , Toast.LENGTH_SHORT);
            toast.show();


            disposables.add(Single.timer( 3, TimeUnit.SECONDS )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( t-> {
                        this.lightbar = LightFns.getInstance();
                        boolean isOpen = lightbar.getOpened();
                        if( !isOpen ) {
                            this.lightbar.open();
                        }
                        this.lightbar.green("HIGH");
                        Logger.i( "-------Sent Red Light Complete---------" );
                        Toast sendToast = Toast.makeText(context, "Red Light Send" , Toast.LENGTH_SHORT);
                        sendToast.show();
                        if( !isOpen ){
                            this.lightbar.close();
                        }
                        disposables.clear();
                    },
                    e->
                    {
                        Logger.e(e, "Light error");
                    }));


        }
    }
}
