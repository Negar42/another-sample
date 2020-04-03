import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OSCommandProcessor {
	public static Scanner scanner = new Scanner(System.in);
	private String command;

	private Manager manager;
	private Pattern pattern;
	private Matcher matcher;

	public OSCommandProcessor(Manager manager) {
		this.manager = manager;
	}

	public void installCommand() {

		while (!(command = scanner.nextLine()).trim().startsWith("install OS")) {
			System.out.println("invalid command");
		}
		pattern = Pattern.compile("install OS\\s(\\S+)\\s(\\S+)");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			manager.installOs(matcher.group(1), matcher.group(2));
		} else {
			System.out.println("invalid command");
		}

		while (!(command = scanner.nextLine()).trim().matches("(\\d+)\\s(\\d+)")) {
			System.out.println("invalid command");
		}
		int number = 0;
		pattern = Pattern.compile("(\\d+)\\s(\\d+)");
		matcher = pattern.matcher(command);
		if (matcher.find()) {
			manager.getHardInformation(Integer.parseInt(matcher.group(1)), number = Integer.parseInt(matcher.group(2)));
		}
		while (manager.getAllDrives().size() < number) {
			manager.installDrives((command = scanner.nextLine()).split(" "));
		}
	}

	public void run() {
		manager.goToSystemDrive();
		while (!(command = scanner.nextLine().trim()).equals("end")) {
			if (command.matches("open\\s(\\S+)")) {
				openFolder(command.replace("open ", ""));
			} else if (command.matches("go to drive\\s(\\S+)")) {
				goToDrive(command.replace("go to drive ", ""));
			} else if (command.equals("back")) {
				processBack();
			} else if (command.matches("create folder\\s(\\S+)")) {
				processCreateFolder(command.replace("create folder ", ""));
			} else if (command.matches("create file\\s(\\S+)\\s(\\S+)\\s(\\S+)")) {
				processCreateFile(command.split(" "));
			} else if (command.matches("delete file\\s(\\S+)")) {
				processDeleteFile(command.replace("delete file ", ""));
			} else if (command.matches("delete folder\\s(\\S+)")) {
				processDeleteFolder(command.replace("delete folder ", ""));
			} else if (command.matches("rename file\\s(\\S+)\\s(\\S+)")) {
				processFileRename(command.split(" "));
			} else if (command.matches("rename folder\\s(\\S+)\\s(\\S+)")) {
				processFolderRename(command.split(" "));
			} else if (command.equals("status")) {
				processStatus();
			} else if (command.equals("print drives status")) {
				processDrivesStatus();
			} else if (command.startsWith("copy file")) {
				processCopyFile(command.replace("copy file ", ""));
			} else if (command.startsWith("copy folder")) {
				processCopyFolder(command.replace("copy folder ", ""));
			} else if (command.startsWith("cut file")) {
				processCutFile(command.replace("cut file ", ""));
			} else if (command.startsWith("cut folder")) {
				processCutFolder(command.replace("cut folder ", ""));
			} else if (command.equals("paste")) {
				processPaste();
			} else if (command.equals("print OS information")) {
				manager.printOsInformation();
			} else if (command.matches("print file stats\\s(\\S+)")) {
				manager.printFileStats(command.replace("print file stats ", ""));
			} else if (command.matches("write text\\s(\\S+)")) {
				processWriting(command.replace("write text ", ""));
			} else if (command.equals("print frequent folders")) {
				manager.printFrequents();
			} else {
				System.out.println("invalid command");
			}
		}

	}

	private void openFolder(String folderName) {
		manager.goToFolderLocation(folderName);
	}

	private void goToDrive(String driveName) {
		manager.goToDriveLocation(driveName);
	}

	private void processBack() {
		manager.goBack();
	}

	private void processCreateFolder(String folderName) {
		manager.createFolder(folderName);
	}

	private void processCreateFile(String[] fileInformation) {
		manager.createFile(fileInformation[2], fileInformation[3], Integer.parseInt(fileInformation[4]));
	}

	private void processDeleteFile(String fileName) {
		System.out.println(manager.deleteFile(fileName));
	}

	private void processDeleteFolder(String folderName) {
		System.out.println(manager.deleteFolder(folderName));
	}

	private void processFileRename(String[] fileNames) {
		System.out.println(manager.renameFile(fileNames[2], fileNames[3]));
	}

	private void processFolderRename(String[] folderNames) {
		System.out.println(manager.renameFolder(folderNames[2], folderNames[3]));
	}

	private void processStatus() {
		System.out.println(manager.printStatus());
	}

	private void processDrivesStatus() {
		System.out.println(manager.printDrivesStatus());
	}

	private void processCopyFile(String files) {
		manager.copyFiles(files.split(" "));
	}

	private void processCopyFolder(String folders) {
		manager.copyFolders(folders.split(" "));
	}

	private void processCutFile(String files) {
		manager.cutFiles(files.split(" "));
	}

	private void processCutFolder(String folders) {
		manager.cutFolders(folders.split(" "));
	}

	private void processPaste() {
		manager.paste();
	}

	private void processWriting(String file) {
		String text = scanner.nextLine();
		manager.changeText(file, text);
	}

}
