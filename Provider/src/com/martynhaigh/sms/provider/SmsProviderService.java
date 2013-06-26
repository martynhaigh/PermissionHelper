package com.martynhaigh.sms.provider;


import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
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

            // If not world readable, check the signature of the [first] package with the given
            // UID against the known-good official DashClock app signature.
            boolean verified = false;
            PackageManager pm = getPackageManager();
            String[] packages = pm.getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                try {
                    PackageInfo pi = pm.getPackageInfo(packages[0], PackageManager.GET_SIGNATURES);
                    if (pi.signatures != null
                            && pi.signatures.length == 1
                            && PERMISSION_HELPER_SIGNATURE.equals(pi.signatures[0])) {
                        verified = true;
                    }

                } catch (PackageManager.NameNotFoundException ignored) {
                }
            }

            if (!verified) {
                Log.e("MH", "Caller is not official permission consumer app");
                throw new SecurityException("Caller is not official permission consumer app.");
            }


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

    /**
     * The signature of the official permission extension app. Used to
     * compare caller.  This signature correlates to debug2.keystore.
     */
    private static final Signature PERMISSION_HELPER_SIGNATURE = new Signature("" +
        "3082030d308201f5a003020102020425a525ec300d06092a864886f70d01010b05003037310b300906035504061302555331103" +
        "00e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3133303432343038" +
        "343232375a170d3433303431373038343232375a3037310b30090603550406130255533110300e060355040a1307416e64726f6" +
        "964311630140603550403130d416e64726f696420446562756730820122300d06092a864886f70d01010105000382010f003082" +
        "010a02820101009798448a007fdb276032d930345b69f16208dcaaa97445088b78092463d4ea868e281dbcf0443beaf0c35548d" +
        "176fdcb50239618e655b272831c5c04620b69d6385c99b0037cf886f99382a9eab84a19a586581fe6f5890bfaaf8219656fb37e" +
        "4321c70682a51749fc7211ba61381bbd5b8e6059649a79a93da7b0a368242be480c57793f1583be9a035178c066e608215c33ba" +
        "a6ceb5d800d95c25968c5fb4c4dc0c9ad1f13c5f5b1f9c35435a4d5d02826dc09e4f195ffb4250d0fa2c7d8090b47fefb0add6b" +
        "9d7852ad6837e9b398542eaa18e08770b46af8eb9a903a876fbd8711fee418b55fbfb820579f4d552c00191fdb12e8f6b2ec114" +
        "dd513e74d690203010001a321301f301d0603551d0e04160414d5be7ddfc0158bb8c69aba36dd069784c036e0b4300d06092a86" +
        "4886f70d01010b050003820101003b36fd4299f60fa0e0522ac08ce239a5b72cccd9d675b68042031b54fc38c6d03d443e0783e" +
        "17ec26452c8478e30c69246033a2bde286476b2343bf31e9c63ad358e7b3c25a9d5aab2704848833db44f52d2815b0d6b7f5c76" +
        "80fe6757ebfaecddfeccbefeec51a8f12afe5bd352303bb550f2f771d7784d40215877414ca5f9a22111285991b9e0e1286b685" +
        "64e87c630a7ffe19122894934783645d09f74d3d7c996246c534da2ef377fad9f2b65df81f498f4474331c3820ad7af603186b2" +
        "b8b901a769e1ed78d2885aae7109e1569b15f330395862c1ecc64e0aae01d06c5445341197c09a520b89acdd97ee2b598c4c1c8" +
        "d5cde3146ecd5241c95e3beb6");



}
