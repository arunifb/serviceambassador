package technician.ifb.com.ifptecnician;

import org.json.JSONObject;

public interface VolleyRespondsListener {

    public void onSuccess(String result, String type);
    public void onFailureJson(int responseCode, String responseMessage);

}
