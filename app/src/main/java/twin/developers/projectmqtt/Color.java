package twin.developers.projectmqtt;

public class Color {
    private String nombreColor;
    private String codigoColor;

    public Color (){
    }

    public Color(String nombreColor, String codigoColor) {
        this.nombreColor = nombreColor;
        this.codigoColor = codigoColor;
    }

    public void setNombreColor(String nombreColor) {
        this.nombreColor = nombreColor;
    }

    public void setCodigoColor(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    public String getNombreColor() {
        return nombreColor;
    }

    public String getCodigoColor() {
        return codigoColor;
    }
}