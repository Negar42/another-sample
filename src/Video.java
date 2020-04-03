public class Video extends File {
	private String quality;
	private String length;

	public Video(String name, int size, String quality, String length) {
		this.name = name;
		this.size = size;
		this.type = "mp4";
		this.quality = quality;
		this.length = length;
		this.containingDrive = Manager.getDriveLocation();
		this.containingFolder = Manager.getFolderLocation();
	}

	public Video(Video video, Folder folder, Drive drive) {
		this.name = video.name;
		this.size = video.size;
		this.type = "mp4";
		this.quality = video.quality;
		this.length = video.length;
		this.containingDrive = drive;
		this.containingFolder = folder;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getQuality() {
		return quality;
	}

	public String getLength() {
		return length;
	}

}
