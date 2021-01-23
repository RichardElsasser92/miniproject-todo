package application.commonClasses.fileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import application.abstractClasses.Task;
import application.appClasses.User;
import application.commonClasses.client.Client_View;

public class User_FM {

	protected Client_View view;
	protected Logger logger = Logger.getLogger("");

	protected String filePath;
	protected static int id;

	public User_FM() {
		this.filePath = "Users.txt";
	}
	

	public List<User> getAll() {
		List<User> list = new ArrayList<User>();

		try {
			Stream<String> lines = loadFileContent();
			lines.forEach(l -> {
				User user = fromProperties(l.split(";"));
				if (user != null) {
					list.add(user);
				}
			});
		} catch (IOException e) {
			logger.warning(e.toString());
		}

		logger.info("Users loaded: " + list.size());

		return list;
	}

	protected User fromProperties(String[] properties) {
		User user = null;
		if(properties.length == 3) {
			user = new User(properties[1],
					properties[2]);
			user.setId(properties[0]);
		}
		return user;
	}

	public void save(User user, boolean isNew) {
		
		String id = user.getId();

		try {
			Stream<String> lines = loadFileContent();
			List<String> list;

			if (isNew) {
			list = lines.collect(Collectors.toList());
			list.add(user.toString());
			} else {
				list = lines.map(line -> {
					if (line.startsWith(id)) {
						line = user.toString();
					}
					return line;
				}).collect(Collectors.toList());
			}

			write(list);
		} catch (IOException e) {
			logger.warning("could not save, Sorry! " + user.getClass().getSimpleName() + ": " + user.toString());
		}
	}

	private Stream<String> loadFileContent() throws IOException {
		// TODO Auto-generated method stub
		return Files.lines(getFile().toPath()).filter(l -> !l.isEmpty());
	}

	private File getFile() throws IOException {
		File file = new File(this.filePath);
		file.createNewFile();
		return file;
	}

	private void write(List<String> lines) throws IOException {
		Files.write(getFile().toPath(), lines, StandardOpenOption.TRUNCATE_EXISTING);
	}

	public void delete(User user) {
		String id = user.getId();

		try {
			write(loadFileContent().filter(l -> !lineBeginsWithID(l, id)).collect(Collectors.toList()));
		} catch (IOException e) {
			logger.warning(e.toString());
		}
	}

	private boolean lineBeginsWithID(String line, String id) {
		return line.split("\\|")[0].equals(id);
	}

}
