public class File implements Cloneable {
	protected String name;
	protected String type;
	protected int size;
	protected Folder containingFolder;
	protected Drive containingDrive;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public Folder getContainingFolder() {
		return containingFolder;
	}

	public Drive getContainingDrive() {
		return containingDrive;
	}

	@Override
	public String toString() {
		return name + " " + type + " " + size + "MB";
	}

	public void setContainingFolder(Folder containingFolder) {
		this.containingFolder = containingFolder;
	}

	public void setContainingDrive(Drive containingDrive) {
		this.containingDrive = containingDrive;
	}

}
