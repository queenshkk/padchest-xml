package hepl.xml.laboxml;

public class Image {
    private String id;
    private String imageDir;
    private String projection;
    private String methodProjection;
    private String pediatric;

    private String reportID;
    private String report;
    private String methodLabel;
    private String labels;
    private String localizations;
    private String labelsLocalizations;
    private String labelCUIS;
    private String localizationsCUIS;

    public Image(String id, String imageDir, String projection,
                 String methodProjection, String pediatric,
                 String reportID, String report, String methodLabel,
                 String labels, String localizations,
                 String labelsLocalizations, String labelCUIS,
                 String localizationsCUIS){
        this.id = id;
        this.imageDir = imageDir;
        this.projection = projection;
        this.methodProjection = methodProjection;
        this.pediatric = pediatric;

        this.reportID = reportID;
        this.report = report;
        this.methodLabel = methodLabel;
        this.labels = labels;
        this.localizations = localizations;
        this.labelsLocalizations = labelsLocalizations;
        this.labelCUIS = labelCUIS;
        this.localizationsCUIS = localizationsCUIS;
    }

    public String getId() {
        return id;
    }

    public String getImageDir() {
        return imageDir;
    }

    public String getProjection() {
        return projection;
    }

    public String getMethodProjection() {
        return methodProjection;
    }
    public String getPediatric() {
        return pediatric;
    }

    public String getReportID() {
        return reportID;
    }

    public String getReport() {
        return report;
    }

    public String getMethodLabel() {
        return methodLabel;
    }

    public String getLabels() {
        return labels;
    }

    public String getLocalizations() {
        return localizations;
    }

    public String getLabelsLocalizations() {
        return labelsLocalizations;
    }

    public String getLabelCUIS() {
        return labelCUIS;
    }

    public String getLocalizationsCUIS() {
        return localizationsCUIS;
    }
}
