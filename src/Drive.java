import java.util.ArrayList;

public class Drive {
	private ArrayList<Folder> includingFolders;
	private ArrayList<File> includingFiles;
	private String name;
	private int size;
	private int freeSpace;

	public Drive(String name, int size) {
		this.name = name;
		this.size = size;
		this.freeSpace = size;
		includingFolders = new ArrayList<Folder>();
		includingFiles = new ArrayList<File>();
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

	public ArrayList<Folder> getIncludingFolders() {
		return includingFolders;
	}

	public ArrayList<File> getIncludingFiles() {
		return includingFiles;
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

	public int getSize() {
		return size;
	}

	public int getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(int freeSpace) {
		this.freeSpace = freeSpace;
	}

	public void addToIncludingFolders(Folder folder) {
		this.includingFolders.add(folder);
	}

	public void addToIncludingFiles(File file) {
		this.includingFiles.add(file);
	}

	@Override
	public String toString() {
		return name + " " + size + "MB" + " " + (size - freeSpace) + "MB";
	}

}
