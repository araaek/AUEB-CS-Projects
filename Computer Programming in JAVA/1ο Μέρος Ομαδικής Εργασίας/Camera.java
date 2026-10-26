class Camera extends ElectronicsStore {
    private String typeCamera;
    private int pixelCamera;
    private int opticalzoomCamera;
    private int digitalzoomCamera;
    private double screensizeCamera;

    public Camera(String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel,
                  String typeCamera, int pixelCamera, int opticalzoomCamera, int digitalzoomCamera, double screensizeCamera) {
        super(codeModel, nameModel, yearModel, manufacturerModel, priceModel);
        this.typeCamera = typeCamera;
        this.pixelCamera = pixelCamera;
        this.opticalzoomCamera = opticalzoomCamera;
        this.digitalzoomCamera = digitalzoomCamera;
        this.screensizeCamera = screensizeCamera;
    }

    public String getTypeCamera() {
        return typeCamera;
    }

    public int getMegapixel() {
        return pixelCamera;
    }

    public int getOpticalZoom() {
        return opticalzoomCamera;
    }

    public int getDigitalZoom() {
        return digitalzoomCamera;
    }

    public double getScreenSize() {
        return screensizeCamera;
    }


    public String printInfo() {
        return ("Type: " + typeCamera + "\n Pixel's: " + pixelCamera + "\n Optical Zoom: "
                + opticalzoomCamera + "\n Digital Zoom: " + digitalzoomCamera + "\n Screen Size: " + screensizeCamera);
    }

    public String toString() {
        return super.toString();
    }
}