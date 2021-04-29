package com.bolsadeideas.spring.horario.datajpa.app.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@Primary
public class UplodaFileServiceImpl implements IUploadService {

	private static final String UPLOAD = "propuestas";

	@Override
	public Resource load(String filename) throws MalformedURLException {

		Path pathFoto = getPath(filename);
		Resource resource = null;

		resource = new UrlResource(pathFoto.toUri());
		if (!resource.exists() || !resource.isReadable()) {
			throw new RuntimeException("Error no se puede cargar la Subir el documento: " + pathFoto.toString());
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {

		String uniqueFile = UUID.randomUUID().toString();
		Path rootPath = getPath(uniqueFile);
		Files.copy(file.getInputStream(), rootPath);
		
		return uniqueFile;
	}

	@Override
	public boolean delete(String filename) {
		
		Path pathRoot = getPath(filename);
		File archivo = pathRoot.toFile();
		if (archivo.exists() && archivo.canRead()) {
			return archivo.delete();
		}
			return false;

	}

	public Path getPath(String filename) {

		return Paths.get(UPLOAD).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(getPath("").toFile());
		
	}

	@Override
	public void init() throws IOException{
		File directorio = new File(getPath("").toString());
			if(!directorio.exists()) {
				directorio.mkdirs();
			}
	}
}
