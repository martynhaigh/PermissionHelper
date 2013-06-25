package com.martynhaigh.sms.provider;


import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import com.martynhaigh.sms.lib.ISmsConsumer;
import com.martynhaigh.sms.lib.ISmsProvider;
import com.martynhaigh.sms.lib.SmsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martyn on 21/06/13.
 */
public class SmsProviderService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }


    private final ISmsProvider.Stub mBinder = new ISmsProvider.Stub() {

        @Override
        public void getSmsDetails(ISmsConsumer callback) throws RemoteException {
            List<SmsData> smsList = new ArrayList<SmsData>();

            Uri uri = Uri.parse("content://sms/inbox");
            Cursor c= getContentResolver().query(uri, null, null ,null,null);

            // Read the sms data and store it in the list
            if(c.moveToFirst()) {
                for(int i=0; i < c.getCount(); i++) {
                    SmsData sms = new SmsData();
                    sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
                    sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
                    smsList.add(sms);

                    c.moveToNext();
                }
            }
            c.close();

            callback.returnSmsDetails(smsList);
        }
    };


}
