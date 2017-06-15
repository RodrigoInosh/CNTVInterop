package cl.subtel.interop.cntv.calculotvd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.cxf.helpers.IOUtils;

import cl.subtel.interop.cntv.dto.DocumentoDTO;

public class CarpetaTecnica {

	public static String saveFile(String userID, DocumentoDTO technical_folder) {
		String file_name = technical_folder.getNombreArchivo();
		DataSource fds = new FileDataSource("C:\\Users\\rinostroza\\Documents\\pruebas\\Downloads.zip");
		DataHandler handler = new DataHandler(fds);
		// String file_directory = "/Documentos_tecnicos/"+userID+"/";
		String file_directory = "C:\\Users\\rinostroza\\Documents\\pruebas\\" + userID + "\\";

		try {
			// InputStream ins = technical_folder.getArchivo().getInputStream();
			InputStream ins = handler.getInputStream();
			File zipped_file_folder = new File(file_directory);

			zipped_file_folder.mkdirs();

			OutputStream fos = new FileOutputStream(new File(file_directory + file_name));
			IOUtils.copy(ins, fos);
			fos.close();
			ins.close();
			unzip(file_directory + file_name, file_directory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file_directory;
	}

	private static void unzip(String zipFilePath, String destDir) {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis, Charset.forName("IBM437")); // Charset
																						// para
																						// caracteres
																						// espciales
																						// (Ñ)
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
