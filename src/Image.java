public class Image extends File {
	private String resolution;
	private String extension;

	public Image(String name, int size, String resolution, String extension) {
		this.name = name;
		this.size = size;
		this.type = "img";
		this.resolution = resolution;
		this.extension = extension;
		this.containingDrive = Manager.getDriveLocation();
		this.containingFolder = Manager.getFolderLocation();

	}

	public Image(Image image, Folder folder, Drive drive) {
		this.name = image.name;
		this.size = image.size;
		this.type = image.type;
		this.resolution = image.resolution;
		this.extension = image.extension;
		this.containingDrive = drive;
		this.containingFolder = folder;
	}

	public String getResolution() {
		return resolution;
	}

	public String getExtension() {
		return extension;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

}
