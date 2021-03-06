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

import org.apache.cxf.helpers.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cl.subtel.interop.cntv.dto.DocumentoDTO;
import cl.subtel.interop.cntv.util.MongoDBUtils;

public class CarpetaTecnica {
	private static final Logger log = LogManager.getLogger();

	public static String saveFile(String userID, DocumentoDTO technical_folder) throws IOException {
		String file_name = technical_folder.getNombreArchivo();
		String file_directory = "/Documentos_tecnicos/" + userID + "/";

		InputStream ins = technical_folder.getArchivo().getInputStream();
		File zipped_file_folder = new File(file_directory);
		zipped_file_folder.mkdirs();
		OutputStream fos = new FileOutputStream(new File(file_directory + file_name));
		IOUtils.copy(ins, fos);
		fos.close();
		ins.close();
		unzip(file_directory + file_name, file_directory);

		return file_directory;
	}

	public static String getFormattedName(String file_name) {
		String formatted_name = "";

		if (file_name.contains("file_cat_")) {
			formatted_name = file_name.replaceAll("file_cat_", "");
		} else if (file_name.contains("Zona") && file_name.contains("PTx")) {
			file_name = file_name.replaceAll("_[0-9]{12}", "");

			String aux[] = file_name.split("_");
			String new_file_name = "";

			for (int ix = 0; ix < aux.length - 2; ix++) {
				new_file_name += aux[ix] + "_";
			}

			String identificador = file_name.split("\\(")[1];
			new_file_name += ('(' + identificador);
			formatted_name = new_file_name;
		} else {
			formatted_name = file_name;
		}

		return formatted_name;
	}

	public static void unzip(String zipFilePath, String destDir) throws IOException {
		File dir = new File(destDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		FileInputStream fis;
		byte[] buffer = new byte[1024];

		fis = new FileInputStream(zipFilePath);
		ZipInputStream zis = new ZipInputStream(fis, Charset.forName("UTF-8"));
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {

			String fileName = ze.getName();
			System.out.println(destDir + File.separator + fileName);
			File newFile = new File(destDir + File.separator + fileName);
//			new File(newFile.getParent()).mkdirs();
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;

			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
			zis.closeEntry();
			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
		fis.close();

		File file = new File(zipFilePath);
		file.delete();

	}

	public static String validateDataTecnica(String temp_folder, String codigo_postulacion, int userID) {
		String validate_message = "";
		String nombre_archivo = "";
		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		int files_count = files_list.length;
		JSONObject datos_sist_principal = new JSONObject();

		for (int ix = 0; ix < files_count; ix++) {
			nombre_archivo = files_list[ix].getName();
			if (nombre_archivo.contains("ZonaServicio_PTx0") && nombre_archivo.contains("pdf")) {
				try {
					datos_sist_principal = MongoDBUtils.getDatosTecnicosConcurso(nombre_archivo, codigo_postulacion,
							userID);
					DatosElemento datos_elemento = DatosElemento.createObjectElementoDatos(0L, datos_sist_principal);
					validate_message = datos_elemento.validateData();
				} catch (JSONException e) {
					log.error("error validateDataTecnica: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return validate_message;
	}

	public static void deleteTempFolder(String temp_folder) {
		log.debug("Borrando carpeta");

		if (!"".equals(temp_folder)) {
			try {
				File directory = new File(temp_folder);

				if (directory.exists()) {
					File files[] = directory.listFiles();
					for (File current_file : files) {
						current_file.delete();
					}
					directory.delete();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
