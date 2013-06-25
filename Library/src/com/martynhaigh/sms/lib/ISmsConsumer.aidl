package com.martynhaigh.sms.lib;

import com.martynhaigh.sms.lib.SmsData;

interface ISmsConsumer {
    oneway void returnSmsDetails(in List<SmsData> data);
}
