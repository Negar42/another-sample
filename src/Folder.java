import java.util.ArrayList;

public class Folder {
	private String name;
	private int size;
	private static ArrayList<Folder> allFolders = new ArrayList<Folder>();
	private ArrayList<Folder> includingFolders;
	private ArrayList<File> includingFiles;
	private Folder containingFolder;
	private Drive containingDrive;
	private int openedTimes;
	private String absoluteAddress;

	public Folder(String name) {
		this.name = name;
		this.containingDrive = Manager.getDriveLocation();
		this.containingFolder = Manager.getFolderLocation();
		includingFolders = new ArrayList<Folder>();
		includingFiles = new ArrayList<File>();
		allFolders.add(this);
	}

	public Folder(Folder folder, Folder headFolder, Drive headDrive) {
		this.name = folder.name;
		this.containingDrive = headDrive;
		this.containingFolder = headFolder;
		includingFolders = new ArrayList<Folder>();
		includingFiles = new ArrayList<File>();
		allFolders.add(this);
	}

	public boolean includesFolder(String folderName) {
		for (Folder folder : getIncludingFolders()) {
			if (folder.getName().equalsIgnoreCase(folderName)) {
				return true;
			}
		}
		return false;
	}

	public boolean includesFile(String fileName) {
		for (File file : getIncludingFiles()) {
			if (file.getName().equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}

	public Folder getFolderByName(String folderName) {
		for (Folder folder : includingFolders) {
			if (folder.getName().equalsIgnoreCase(folderName)) {
				return folder;
			}
		}
		return null;
	}

	public File getFileByName(String fileName) {
		for (File file : includingFiles) {
			if (file.getName().equalsIgnoreCase(fileName)) {
				return file;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public ArrayList<Folder> getIncludingFolders() {
		return includingFolders;
	}

	public ArrayList<File> getIncludingFiles() {
		return includingFiles;
	}

	public Folder getContainingFolder() {
		return containingFolder;
	}

	public Drive getContainingDrive() {
		return containingDrive;
	}

	public int getOpenedTimes() {
		return openedTimes;
	}

	public void updateOpenedTimes() {
		openedTimes++;
	}

	public void addToIncludingFolders(Folder folder) {
		includingFolders.add(folder);
	}

	public void addToIncludingFiles(File file) {
		includingFiles.add(file);
	}

	@Override
	public String toString() {
		return name + " " + size + "MB";
	}

	public void setIncludingFolders(ArrayList<Folder> includingFolders) {
		this.includingFolders = includingFolders;
	}

	public void setIncludingFiles(ArrayList<File> includingFiles) {
		this.includingFiles = includingFiles;
	}

	public static ArrayList<Folder> getAllFolders() {
		return allFolders;
	}

	public void setContainingFolder(Folder containingFolder) {
		this.containingFolder = containingFolder;
	}

	public void setContainingDrive(Drive containingDrive) {
		this.containingDrive = containingDrive;
	}

	public void setOpenedTimes(int openedTimes) {
		this.openedTimes = openedTimes;
	}

	public String getAbsoluteAddress() {
		return absoluteAddress;
	}

	public void setAbsoluteAddress(String absoluteAddress) {
		this.absoluteAddress = absoluteAddress;
	}

}
