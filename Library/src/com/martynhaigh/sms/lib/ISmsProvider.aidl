package com.martynhaigh.sms.lib;

import com.martynhaigh.sms.lib.ISmsConsumer;

interface ISmsProvider {
    oneway void getSmsDetails(in ISmsConsumer callback);
}
