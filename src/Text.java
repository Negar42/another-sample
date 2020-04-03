public class Text extends File {
	private String text;

	public Text(String name, int size, String text) {
		this.name = name;
		this.size = size;
		this.type = "txt";
		this.text = text;
		this.containingDrive = Manager.getDriveLocation();
		this.containingFolder = Manager.getFolderLocation();
	}

	public Text(Text text, Folder folder, Drive drive) {
		this.name = text.name;
		this.size = text.size;
		this.type = "txt";
		this.text = text.text;
		this.containingDrive = drive;
		this.containingFolder = folder;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
