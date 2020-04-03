import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Manager {
	private ArrayList<Drive> allDrives = new ArrayList<Drive>();
	private Scanner scanner = OSCommandProcessor.scanner;
	private static String OS_NAME;
	private static String OS_VERSION;
	private static int DRIVES_NUM;
	private static int HARD_SIZE;
	private static int freeSpace;
	private static Folder folderLocation;
	private static Drive driveLocation;
	private static ArrayList<Folder> coppiedFolders = new ArrayList<Folder>();
	private static ArrayList<Folder> cuttedFolders = new ArrayList<Folder>();
	private static ArrayList<File> coppiedFiles = new ArrayList<File>();
	private static ArrayList<File> cuttedFiles = new ArrayList<File>();

	public void installOs(String name, String version) {
		OS_NAME = name;
		OS_VERSION = version;
	}

	public void getHardInformation(int size, int number) {

		HARD_SIZE = size;
		DRIVES_NUM = number;
		freeSpace = size;

	}

	public void installDrives(String[] drive) {
		if (drive.length == 2) {
			if (drive[0].matches("[A-Z]") && !hasDriveWithName(drive[0])) {
				if (Integer.parseInt(drive[1]) <= freeSpace) {
					freeSpace -= Integer.parseInt(drive[1]);
					allDrives.add(new Drive(drive[0], Integer.parseInt(drive[1])));
				} else {
					System.out.println("insufficient hard size");
				}

			} else {
				System.out.println("invalid name");
			}
		} else {
			System.out.println("invalid command");
		}
	}

	public void printOsInformation() {
		System.out.println("OS is " + OS_NAME + " " + OS_VERSION);
	}

	public StringBuilder printStatus() {
		StringBuilder status = new StringBuilder();
		status.append(getAbsoluteLocation(folderLocation, driveLocation) + "\n");
		status.append("Folders:");
		if (folderLocation != null) {
			folderLocation.getIncludingFolders().sort(Comparator.comparing(Folder::getName));
			folderLocation.getIncludingFiles().sort(Comparator.comparing(File::getName));
			for (Folder folder : folderLocation.getIncludingFolders()) {
				status.append("\n" + folder.toString());
			}
			status.append("\nFiles:");
			status.append(appendFiles());
		} else {
			driveLocation.getIncludingFolders().sort(Comparator.comparing(Folder::getName));
			driveLocation.getIncludingFiles().sort(Comparator.comparing(File::getName));
			for (Folder folder : driveLocation.getIncludingFolders()) {
				status.append("\n" + folder.toString());
			}
			status.append("\nFiles:");
			status.append(appendFiles());
		}
		return status;
	}

	private StringBuilder appendFiles() {
		StringBuilder files = new StringBuilder();
		if (folderLocation != null) {
			for (File file : folderLocation.getIncludingFiles()) {
				if (file instanceof Image) {
					files.append("\n" + file.toString());
				}
			}
			for (File file : folderLocation.getIncludingFiles()) {
				if (file instanceof Text) {
					files.append("\n" + file.toString());
				}
			}
			for (File file : folderLocation.getIncludingFiles()) {
				if (file instanceof Video) {
					files.append("\n" + file.toString());
				}
			}
		} else {
			for (File file : driveLocation.getIncludingFiles()) {
				if (file instanceof Image) {
					files.append("\n" + file.toString());
				}
			}
			for (File file : driveLocation.getIncludingFiles()) {
				if (file instanceof Text) {
					files.append("\n" + file.toString());
				}
			}
			for (File file : driveLocation.getIncludingFiles()) {
				if (file instanceof Video) {
					files.append("\n" + file.toString());
				}
			}
		}
		return files;
	}

	public StringBuilder printDrivesStatus() {
		StringBuilder drives = new StringBuilder();
		for (Drive drive : allDrives) {
			drives.append("\n" + drive.toString());
		}
		drives.delete(0, 1);
		return drives;
	}

	public void printFileStats(String fileName) {
		StringBuilder stats = new StringBuilder();
		File file;
		if (folderLocation != null) {
			if (folderLocation.includesFile(fileName)) {
				file = folderLocation.getFileByName(fileName);
			} else {
				System.out.println("invalid name");
				return;
			}
		} else {
			if (driveLocation.includesFile(fileName)) {
				file = driveLocation.getFileByName(fileName);
			} else {
				System.out.println("invalid name");
				return;
			}
		}
		stats.append(fileName + " " + file.getType() + "\n");
		stats.append(getAbsoluteLocation(folderLocation, driveLocation) + "\\" + fileName + "\n");
		stats.append("Size: " + file.getSize() + "MB" + "\n");
		if (file instanceof Text) {
			stats.append("Text: " + ((Text) file).getText());
		} else if (file instanceof Image) {
			stats.append("Resolution: " + ((Image) file).getResolution());
			stats.append("\n" + "Extension: " + ((Image) file).getExtension());
		} else if (file instanceof Video) {
			stats.append("Quality: " + ((Video) file).getQuality());
			stats.append("\n" + "Video Length: " + ((Video) file).getLength());
		}
		System.out.println(stats);
	}

	public void printFrequents() {
		ArrayList<Folder> folders = new ArrayList<Folder>();
		folders = Folder.getAllFolders();
		for (Folder folder : folders) {
			folder.setAbsoluteAddress(getAbsoluteLocation(folder.getContainingFolder(), folder.getContainingDrive())
					+ "\\" + folder.getName());
		}
		folders.sort(Comparator.comparing(Folder::getAbsoluteAddress));
		folders.sort(Comparator.comparing(Folder::getOpenedTimes).reversed());

		for (int i = 0; i < 5; i++) {
			if (folders.size() > i) {
				if (folders.get(i).getOpenedTimes() != 0) {
					System.out.println(getAbsoluteLocation(folders.get(i).getContainingFolder(),
							folders.get(i).getContainingDrive()) + "\\" + folders.get(i).getName() + " "
							+ folders.get(i).getOpenedTimes());
				}
			}
		}
	}

	public void goToSystemDrive() {
		driveLocation = allDrives.get(0);
	}

	public void goToDriveLocation(String driveName) {
		if (hasDriveWithName(driveName)) {
			driveLocation = getDriveByName(driveName);
			folderLocation = null;
		} else {
			System.out.println("invalid name");
		}
	}

	public void goToFolderLocation(String folderName) {
		if (folderLocation != null) {
			if (folderLocation.includesFolder(folderName)) {
				folderLocation = folderLocation.getFolderByName(folderName);
				folderLocation.updateOpenedTimes();
			} else {
				System.out.println("invalid name");
			}

		} else {
			if (driveLocation.includesFolder(folderName)) {
				folderLocation = driveLocation.getFolderByName(folderName);
				folderLocation.updateOpenedTimes();
			} else {
				System.out.println("invalid name");
			}
		}
	}

	public Drive getDriveByName(String name) {
		for (Drive drive : allDrives) {
			if (drive.getName().equals(name)) {
				return drive;
			}
		}
		return null;
	}

	public boolean hasDriveWithName(String name) {
		if (getDriveByName(name) == null) {
			return false;
		}
		return true;
	}

	public void goBack() {
		if (folderLocation != null) {
			folderLocation = folderLocation.getContainingFolder();
		}
	}

	public void createFolder(String folderName) {
		if (folderLocation != null) {
			if (!folderLocation.includesFolder(folderName)) {
				folderLocation.addToIncludingFolders(new Folder(folderName));
				System.out.println("folder created");
			} else {
				System.out.println("folder exists with this name");

			}
		} else {
			if (!driveLocation.includesFolder(folderName)) {
				driveLocation.addToIncludingFolders(new Folder(folderName));
				System.out.println("folder created");
			} else {
				System.out.println("folder exists with this name");

			}

		}
		updateDrives();
	}

	public void createFile(String fileName, String format, int size) {
		if (folderLocation != null) {
			createFileInFolder(fileName, format, size);
		} else {
			createFileInDrive(fileName, format, size);
		}
		updateDrives();
	}

	private void createFileInFolder(String fileName, String format, int size) {
		if (!folderLocation.includesFile(fileName)) {
			if (format.equals("img")) {
				if (size <= driveLocation.getFreeSpace()) {
					createImage(fileName, size);
				} else {
					System.out.println("insufficient drive size");
				}

			} else if (format.equals("mp4")) {
				if (size <= driveLocation.getFreeSpace()) {
					createVideo(fileName, size);
				} else {
					System.out.println("insufficient drive size");
				}

			} else if (format.equals("txt")) {
				if (size <= driveLocation.getFreeSpace()) {
					createText(fileName, size);

				} else {
					System.out.println("insufficient drive size");
				}

			} else {
				System.out.println("invalid format");
			}
		} else {
			System.out.println("file exists with this name");
		}
	}

	private void createFileInDrive(String fileName, String format, int size) {
		if (!driveLocation.includesFile(fileName)) {
			if (format.equals("img")) {
				if (size <= driveLocation.getFreeSpace()) {
					createImage(fileName, size);
				} else {
					System.out.println("insufficient drive size");
				}

			} else if (format.equals("mp4")) {
				if (size <= driveLocation.getFreeSpace()) {
					createVideo(fileName, size);
				} else {
					System.out.println("insufficient drive size");
				}

			} else if (format.equals("txt")) {
				if (size <= driveLocation.getFreeSpace()) {
					createText(fileName, size);

				} else {
					System.out.println("insufficient drive size");
				}

			} else {
				System.out.println("invalid format");
			}
		} else {
			System.out.println("file exists with this name");
		}
	}

	private void createVideo(String fileName, int size) {
		System.out.println("Quality:");
		String quality = scanner.nextLine();
		System.out.println("Video Length:");
		String length = scanner.nextLine();
		if (folderLocation != null) {
			folderLocation.addToIncludingFiles(new Video(fileName, size, quality, length));
			updateDrives();

		} else {
			driveLocation.addToIncludingFiles(new Video(fileName, size, quality, length));
			updateDrives();
		}
		System.out.println("file created");
	}

	private void createImage(String fileName, int size) {
		System.out.println("Resolution:");
		String resolution = scanner.nextLine();
		System.out.println("Extension:");
		String extension = scanner.nextLine();
		if (folderLocation != null) {
			folderLocation.addToIncludingFiles(new Image(fileName, size, resolution, extension));
			updateDrives();
		} else {
			driveLocation.addToIncludingFiles(new Image(fileName, size, resolution, extension));
			updateDrives();
		}
		System.out.println("file created");
	}

	private void createText(String fileName, int size) {
		System.out.println("Text:");
		String text = scanner.nextLine();
		if (folderLocation != null) {
			folderLocation.addToIncludingFiles(new Text(fileName, size, text));
			updateDrives();
		} else {
			driveLocation.addToIncludingFiles(new Text(fileName, size, text));
			updateDrives();
		}
		System.out.println("file created");
	}

	public String deleteFile(String fileName) {
		if (folderLocation != null) {
			if (folderLocation.includesFile(fileName)) {
				folderLocation.getIncludingFiles().remove(folderLocation.getFileByName(fileName));
				updateDrives();
				return "file deleted";
			}

		} else {
			if (driveLocation.includesFile(fileName)) {
				driveLocation.getIncludingFiles().remove(driveLocation.getFileByName(fileName));
				updateDrives();

				return "file deleted";
			}

		}
		return "invalid name";
	}

	public String deleteFolder(String folderName) {
		if (folderLocation != null) {
			if (folderLocation.includesFolder(folderName)) {
				folderLocation.getIncludingFolders().remove(folderLocation.getFolderByName(folderName));
				updateDrives();
				return "folder deleted";
			}
		} else {
			if (driveLocation.includesFolder(folderName)) {
				driveLocation.getIncludingFolders().remove(driveLocation.getFolderByName(folderName));
				updateDrives();
				return "folder deleted";

			}
		}
		return "invalid name";
	}

	public String renameFile(String oldName, String newName) {
		if (folderLocation != null) {
			if (folderLocation.includesFile(oldName)) {
				if (!folderLocation.includesFile(newName)) {
					folderLocation.getFileByName(oldName).setName(newName);
					return "file renamed";
				}
				return "file exists with this name";
			}

		} else {
			if (driveLocation.includesFile(oldName)) {
				if (!driveLocation.includesFile(newName)) {
					driveLocation.getFileByName(oldName).setName(newName);
					return "file renamed";
				}
				return "file exists with this name";
			}

		}
		return "invalid name";
	}

	public String renameFolder(String oldName, String newName) {
		if (folderLocation != null) {
			if (folderLocation.includesFolder(oldName)) {
				if (!folderLocation.includesFolder(newName)) {
					folderLocation.getFolderByName(oldName).setName(newName);
					return "folder renamed";
				}
				return "folder exists with this name";
			}

		} else {
			if (driveLocation.includesFolder(oldName)) {
				if (!driveLocation.includesFolder(newName)) {
					driveLocation.getFolderByName(oldName).setName(newName);
					return "folder renamed";
				}
				return "folder exists with this name";
			}

		}
		return "invalid name";
	}

	public void changeText(String fileName, String text) {
		if (folderLocation != null) {
			if (folderLocation.includesFile(fileName)) {
				if (folderLocation.getFileByName(fileName) instanceof Text) {
					((Text) folderLocation.getFileByName(fileName)).setText(text);
				} else {
					System.out.println("this file is not a text file");
					return;
				}

			} else {
				System.out.println("invalid name");
				return;
			}
		} else {
			if (driveLocation.includesFile(fileName)) {
				if (driveLocation.getFileByName(fileName) instanceof Text) {
					((Text) driveLocation.getFileByName(fileName)).setText(text);
				} else {
					System.out.println("this file is not a text file");
					return;
				}
			} else {
				System.out.println("invalid name");
				return;
			}

		}
	}

	public static Folder getFolderLocation() {
		return folderLocation;
	}

	public static Drive getDriveLocation() {
		return driveLocation;
	}

	public String getAbsoluteLocation(Folder origin, Drive drive) {
		StringBuilder location = new StringBuilder();
		Folder folder;
		folder = origin;
		while (folder != null) {
			location.insert(0, "\\" + folder.getName());
			folder = folder.getContainingFolder();
		}
		location.insert(0, drive.getName() + ":");
		return location.toString();
	}

	public ArrayList<Drive> getAllDrives() {
		return allDrives;
	}

	public void copyFiles(String[] fileNames) {
		if (folderLocation != null) {
			for (String name : fileNames) {
				if (!folderLocation.includesFile(name)) {
					System.out.println("invalid name");

					return;
				}
			}
			clearFunctions();
			for (String name : fileNames) {
				coppiedFiles.add(folderLocation.getFileByName(name));
			}
		} else {
			for (String name : fileNames) {
				if (!driveLocation.includesFile(name)) {
					System.out.println("invalid name");
					return;

				}
			}
			clearFunctions();
			for (String name : fileNames) {
				coppiedFiles.add(driveLocation.getFileByName(name));
			}

		}
		System.out.println("files copied");
	}

	public void copyFolders(String[] folderNames) {
		if (folderLocation != null) {
			for (String name : folderNames) {
				if (!folderLocation.includesFolder(name)) {
					System.out.println("invalid name");

					return;
				}
			}
			clearFunctions();
			for (String name : folderNames) {
				coppiedFolders.add(folderLocation.getFolderByName(name));
			}
		} else {
			for (String name : folderNames) {
				if (!driveLocation.includesFolder(name)) {
					System.out.println("invalid name");
					return;

				}
			}
			clearFunctions();
			for (String name : folderNames) {
				coppiedFolders.add(driveLocation.getFolderByName(name));
			}

		}
		System.out.println("folders copied");
	}

	public void cutFiles(String[] fileNames) {
		if (folderLocation != null) {
			for (String name : fileNames) {
				if (!folderLocation.includesFile(name)) {
					System.out.println("invalid name");

					return;
				}
			}
			clearFunctions();
			for (String name : fileNames) {
				cuttedFiles.add(folderLocation.getFileByName(name));
			}
		} else {
			for (String name : fileNames) {
				if (!driveLocation.includesFile(name)) {
					System.out.println("invalid name");
					return;

				}
			}
			clearFunctions();
			for (String name : fileNames) {
				cuttedFiles.add(driveLocation.getFileByName(name));
			}

		}
		System.out.println("files cut completed");
	}

	public void cutFolders(String[] folderNames) {
		if (folderLocation != null) {
			for (String name : folderNames) {
				if (!folderLocation.includesFolder(name)) {
					System.out.println("invalid name");

					return;
				}
			}
			clearFunctions();
			for (String name : folderNames) {
				cuttedFolders.add(folderLocation.getFolderByName(name));
			}
		} else {
			for (String name : folderNames) {
				if (!driveLocation.includesFolder(name)) {
					System.out.println("invalid name");
					return;

				}
			}
			clearFunctions();
			for (String name : folderNames) {
				cuttedFolders.add(driveLocation.getFolderByName(name));
			}

		}
		System.out.println("folders cut completed");
	}

	public void paste() {
		if (!coppiedFiles.isEmpty()) {
			pasteCoppiedFiles();
		} else if (!coppiedFolders.isEmpty()) {
			pasteCoppiedFolders();
		} else if (!cuttedFiles.isEmpty()) {
			pasteCuttedFiles();
		} else if (!cuttedFolders.isEmpty()) {
			pasteCuttedFolders();
		}
	}

	private void pasteCoppiedFiles() {
		if (folderLocation != null) {
			for (File file : coppiedFiles) {
				if (folderLocation.includesFile(file.getName())) {
					System.out.println("file exists with this name");
					return;
				}
			}
			int sum = 0;
			for (File file : coppiedFiles) {
				sum += file.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (File file : coppiedFiles) {
				copyConstructFile(file, folderLocation, driveLocation);
			}
		} else {
			for (File file : coppiedFiles) {
				if (driveLocation.includesFile(file.getName())) {
					System.out.println("file exists with this name");
					return;
				}
			}
			int sum = 0;
			for (File file : coppiedFiles) {
				sum += file.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (File file : coppiedFiles) {
				copyConstructFile(file, folderLocation, driveLocation);
			}

		}

		System.out.println("paste completed");
		clearFunctions();
		updateDrives();
	}

	private void pasteCoppiedFolders() {
		if (folderLocation != null) {
			for (Folder folder : coppiedFolders) {
				if (folderLocation.includesFolder(folder.getName())) {
					System.out.println("folder exists with this name");
					return;
				}
			}
			int sum = 0;
			for (Folder folder : coppiedFolders) {
				sum += folder.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (Folder folder : coppiedFolders) {
				Folder newFolder;
				newFolder = new Folder(folder, folderLocation, driveLocation);
				completePaste(folder, newFolder);
				folderLocation.addToIncludingFolders(newFolder);

			}
		} else {
			for (Folder folder : coppiedFolders) {
				if (driveLocation.includesFolder(folder.getName())) {
					System.out.println("folder exists with this name");
					return;
				}
			}
			int sum = 0;
			for (Folder folder : coppiedFolders) {
				sum += folder.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (Folder folder : coppiedFolders) {
				Folder newFolder;
				newFolder = new Folder(folder, folderLocation, driveLocation);
				completePaste(folder, newFolder);
				driveLocation.addToIncludingFolders(newFolder);
			}

		}
		System.out.println("paste completed");
		clearFunctions();
		updateDrives();
	}

	private void copyConstructFile(File file, Folder folder, Drive drive) {
		if (file.getType().equals("img")) {
			if (folder != null) {
				folder.addToIncludingFiles(new Image((Image) file, folder, drive));
			} else {
				drive.addToIncludingFiles(new Image((Image) file, folder, drive));
			}
		} else if (file.getType().equals("mp4")) {
			if (folder != null) {
				folder.addToIncludingFiles(new Video((Video) file, folder, drive));
			} else {
				drive.addToIncludingFiles(new Video((Video) file, folder, drive));
			}
		} else if (file.getType().contentEquals("txt")) {
			if (folder != null) {
				folder.addToIncludingFiles(new Text((Text) file, folder, drive));
			} else {
				drive.addToIncludingFiles(new Text((Text) file, folder, drive));
			}
		}
	}

	private void completePaste(Folder folder, Folder newFolder) {
		Folder newInnerFolder;
		if (!folder.getIncludingFolders().isEmpty()) {
			for (Folder innerFolder : folder.getIncludingFolders()) {
				newFolder.addToIncludingFolders(newInnerFolder = new Folder(innerFolder, newFolder, driveLocation));
				completePaste(innerFolder, newInnerFolder);
			}
		}
		for (File file : folder.getIncludingFiles()) {
			copyConstructFile(file, newFolder, driveLocation);
		}
	}

	private void pasteCuttedFiles() {
		if (folderLocation != null) {
			for (File file : cuttedFiles) {
				if (folderLocation.includesFile(file.getName())) {
					System.out.println("file exists with this name");
					return;
				}
			}
			int sum = 0;
			for (File file : cuttedFiles) {
				sum += file.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (File file : cuttedFiles) {
				deleteCuttedFile(file);
				folderLocation.addToIncludingFiles(file);
				file.setContainingFolder(folderLocation);
				file.setContainingDrive(driveLocation);
			}
		} else {
			for (File file : cuttedFiles) {
				if (driveLocation.includesFile(file.getName())) {
					System.out.println("file exists with this name");
					return;
				}
			}
			int sum = 0;
			for (File file : cuttedFiles) {
				sum += file.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (File file : cuttedFiles) {
				deleteCuttedFile(file);
				driveLocation.addToIncludingFiles(file);
				file.setContainingFolder(folderLocation);
				file.setContainingDrive(driveLocation);
			}

		}
		System.out.println("paste completed");
		clearFunctions();
		updateDrives();
	}

	private void pasteCuttedFolders() {
		if (folderLocation != null) {
			for (Folder folder : cuttedFolders) {
				if (folderLocation.includesFolder(folder.getName())) {
					System.out.println("folder exists with this name");
					return;
				}
			}
			int sum = 0;
			for (Folder folder : cuttedFolders) {
				sum += folder.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (Folder folder : cuttedFolders) {
				deleteCuttedFolder(folder);
				folderLocation.addToIncludingFolders(folder);
				folder.setContainingFolder(folderLocation);
				folder.setContainingDrive(driveLocation);
				finalPasteTouch(folder);
			}
		} else {
			for (Folder folder : cuttedFolders) {
				if (driveLocation.includesFolder(folder.getName())) {
					System.out.println("folder exists with this name");
					return;
				}
			}
			int sum = 0;
			for (Folder folder : cuttedFolders) {
				sum += folder.getSize();
				if (sum > driveLocation.getFreeSpace()) {
					System.out.println("insufficient drive size");
					return;
				}
			}
			for (Folder folder : cuttedFolders) {
				deleteCuttedFolder(folder);
				driveLocation.addToIncludingFolders(folder);
				folder.setContainingFolder(folderLocation);
				folder.setContainingDrive(driveLocation);
				finalPasteTouch(folder);
			}

		}
		updateDrives();
		clearFunctions();
		System.out.println("paste completed");

	}

	private void deleteCuttedFolder(Folder folder) {
		if (folder.getContainingFolder() != null) {
			folder.getContainingFolder().getIncludingFolders().remove(folder);
		} else {
			folder.getContainingDrive().getIncludingFolders().remove(folder);
		}
	}

	private void deleteCuttedFile(File file) {
		if (file.containingFolder != null) {
			file.getContainingFolder().getIncludingFiles().remove(file);
		} else {
			file.getContainingDrive().getIncludingFiles().remove(file);
		}
	}

	private void finalPasteTouch(Folder folder) {
		folder.setOpenedTimes(0);
		if (!folder.getIncludingFolders().isEmpty()) {
			for (Folder innerFolder : folder.getIncludingFolders()) {
				innerFolder.setContainingDrive(driveLocation);
				finalPasteTouch(innerFolder);
			}
		}
	}

	private void clearFunctions() {
		coppiedFiles.clear();
		coppiedFolders.clear();
		cuttedFiles.clear();
		cuttedFolders.clear();
	}

	private void updateDrives() {
		for (Drive drive : allDrives) {
			drive.setFreeSpace(drive.getSize());
			for (Folder folder : drive.getIncludingFolders()) {
				updateFolder(folder);
				drive.setFreeSpace(drive.getFreeSpace() - folder.getSize());
			}
			for (File file : drive.getIncludingFiles()) {
				drive.setFreeSpace(drive.getFreeSpace() - file.getSize());
			}
		}
	}

	private void updateFolder(Folder folder) {
		folder.setSize(0);
		if (!folder.getIncludingFolders().isEmpty()) {
			for (Folder innerFolder : folder.getIncludingFolders()) {
				updateFolder(innerFolder);
				folder.setSize(folder.getSize() + innerFolder.getSize());
			}
		}
		for (File file : folder.getIncludingFiles()) {
			folder.setSize(folder.getSize() + file.getSize());
		}
	}
}
