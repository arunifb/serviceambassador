package technician.ifb.com.ifptecnician.troublesehoot.model;

public class TroubleShootModel {

    String Step,Check,Status,Action,Running;


    public String getStep() {
        return Step;
    }

    public String getCheck() {
        return Check;
    }

    public String getStatus() {
        return Status;
    }

    public String getAction() {
        return Action;
    }

    public void setStep(String step) {
        Step = step;
    }

    public void setCheck(String check) {
        Check = check;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getRunning() {
        return Running;
    }

    public void setRunning(String running) {
        Running = running;
    }
}



