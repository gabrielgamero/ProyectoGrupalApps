package pucp.telecom.moviles.incidencias.entities;

public class DtoMessage {
    private String msg;
    private int status;

    public DtoMessage(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
