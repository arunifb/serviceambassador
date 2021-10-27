package technician.ifb.com.ifptecnician.utility;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;

public class Check_eighteen_digitno {

    String validNumber = "[0-9]+";
    public boolean checkNo(String scanres) {
        boolean suc;

        if (scanres.matches(validNumber)) {

            if (scanres.length() == 18) {

                suc = true;
            } else {
                suc = false;

            }


        } else {

            suc = false;
         }

        return suc;
    }
}
