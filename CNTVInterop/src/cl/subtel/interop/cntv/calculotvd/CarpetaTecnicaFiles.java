package cl.subtel.interop.cntv.calculotvd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import org.apache.cxf.helpers.IOUtils;

public class CarpetaTecnicaFiles {

	public final static String PATH = "/cntv/Postulaciones";
	public final static String SERVER_ROOT_PATH = "\\\\Repcedoc\\Dosubtel\\CNTV\\Postulaciones";
	// public final static String PATH =
	// "C:\\Users\\rinostroza\\Documents\\pruebas";
	public final static String FILE_SEPARATOR = "/";

	public final static String LISTADO_EQUIPOS = "file_cat_listado_equipos";
	public final static String MEMORIA_CALCULOS_PERIDAS = "file_cat_memoria_calculo_perdidas";
	public final static String MODELAMIENTO_ANTENA = "file_cat_formulario_modelamiento_antena";
	public final static String DIAGRAMA_RADIACION_HORIZONTAL = "file_cat_diagrama_radiacion_horizontal";
	public final static String DIAGRAMA_RADIACION_VERTICAL = "file_cat_diagrama_radiacion_vertical";
	public final static String DESCRIPCION_EQUIPAMIENTO = "file_cat_descripcion_equipamiento";
	public final static String CATALOGO_EQUIPOS = "file_cat_especificaciones_tecnicas_equipos";
	public final static String SERVICIOS_INTERACTIVOS = "file_cat_servicios_interactivos";
	public final static String CERTIFICADO_DGAC = "file_cat_certificado_dgac";
	public final static String EFICIENCIA_ESPECTRAL = "file_cat_estudio_eficiencia_espectral";
	public final static String ZONA_SERVICIO_PTX0 = "ZonaServicio_PTx0";
	public final static String ZONA_SERVICIO_PTX1 = "ZonaServicio_PTx1";
	public final static String ZONA_SERVICIO_PTX2 = "ZonaServicio_PTx2";
	public final static String ZONA_COBERTURA_PTX0 = "ZonaCobertura_PTx0";
	public final static String ZONA_COBERTURA_PTX1 = "ZonaCobertura_PTx1";
	public final static String ZONA_COBERTURA_PTX2 = "ZonaCobertura_PTx2";
	public final static String ZONA_URBANA_PTX0 = "ZonaUrbana_PTx0";
	public final static String ZONA_URBANA_PTX1 = "ZonaUrbana_PTx1";
	public final static String ZONA_URBANA_PTX2 = "ZonaUrbana_PTx2";

	public final static String COD_LISTADO_EQUIPOS = "TVDLE";
	public final static String COD_MEMORIA_CALCULOS_PERIDAS = "TVDMCP";
	public final static String COD_MODELAMIENTO_ANTENA = "TVDMA";
	public final static String COD_DIAGRAMA_RADIACION_HORIZONTAL = "TVDHSR";
	public final static String COD_DIAGRAMA_RADIACION_VERTICAL = "TVDVSR";
	public final static String COD_DESCRIPCION_EQUIPAMIENTO = "TVDDE";
	public final static String COD_CATALOGO_EQUIPOS = "TVDCE";
	public final static String COD_SERVICIOS_INTERACTIVOS = "TVDSI";
	public final static String COD_CERTIFICADO_DGAC = "TVDCDG";
	public final static String COD_EFICIENCIA_ESPECTRAL = "TVDEE";
	public final static String COD_ZONA_SERVICIO_PTX0 = "TVDZS0";
	public final static String COD_ZONA_SERVICIO_PTX1 = "TVDZS1";
	public final static String COD_ZONA_SERVICIO_PTX2 = "TVDZS2";
	public final static String COD_ZONA_COBERTURA_PTX0 = "TVDZC0";
	public final static String COD_ZONA_COBERTURA_PTX1 = "TVDZC1";
	public final static String COD_ZONA_COBERTURA_PTX2 = "TVDZC2";
	public final static String COD_ZONA_URBANA_PTX0 = "TVDZU0";
	public final static String COD_ZONA_URBANA_PTX1 = "TVDZU1";
	public final static String COD_ZONA_URBANA_PTX2 = "TVDZU2";

	public static String getListadoEquipos() {
		return LISTADO_EQUIPOS;
	}

	public static String getMemoriaCalculosPeridas() {
		return MEMORIA_CALCULOS_PERIDAS;
	}

	public static String getModelamientoAntena() {
		return MODELAMIENTO_ANTENA;
	}

	public static String getDiagramaRadiacionHorizontal() {
		return DIAGRAMA_RADIACION_HORIZONTAL;
	}

	public static String getDiagramaRadiacionVertical() {
		return DIAGRAMA_RADIACION_VERTICAL;
	}

	public static String getDescripcionEquipamiento() {
		return DESCRIPCION_EQUIPAMIENTO;
	}

	public static String getCatalogoEquipos() {
		return CATALOGO_EQUIPOS;
	}

	public static String getServiciosInteractivos() {
		return SERVICIOS_INTERACTIVOS;
	}

	public static String getCertificadoDgac() {
		return CERTIFICADO_DGAC;
	}

	public static String getEficienciaEspectral() {
		return EFICIENCIA_ESPECTRAL;
	}

	public static String getZonaServicioPtx0() {
		return ZONA_SERVICIO_PTX0;
	}

	public static String getZonaServicioPtx1() {
		return ZONA_SERVICIO_PTX1;
	}

	public static String getZonaServicioPtx2() {
		return ZONA_SERVICIO_PTX2;
	}

	public static String getZonaCoberturaPtx0() {
		return ZONA_COBERTURA_PTX0;
	}

	public static String getZonaCoberturaPtx1() {
		return ZONA_COBERTURA_PTX1;
	}

	public static String getZonaCoberturaPtx2() {
		return ZONA_COBERTURA_PTX2;
	}

	public static String getZonaUrbanaPtx0() {
		return ZONA_URBANA_PTX0;
	}

	public static String getZonaUrbanaPtx1() {
		return ZONA_URBANA_PTX1;
	}

	public static String getZonaUrbanaPtx2() {
		return ZONA_URBANA_PTX2;
	}

	public static String getCodListadoEquipos() {
		return COD_LISTADO_EQUIPOS;
	}

	public static String getCodMemoriaCalculosPeridas() {
		return COD_MEMORIA_CALCULOS_PERIDAS;
	}

	public static String getCodModelamientoAntena() {
		return COD_MODELAMIENTO_ANTENA;
	}

	public static String getCodDiagramaRadiacionHorizontal() {
		return COD_DIAGRAMA_RADIACION_HORIZONTAL;
	}

	public static String getCodDiagramaRadiacionVertical() {
		return COD_DIAGRAMA_RADIACION_VERTICAL;
	}

	public static String getCodDescripcionEquipamiento() {
		return COD_DESCRIPCION_EQUIPAMIENTO;
	}

	public static String getCodCatalogoEquipos() {
		return COD_CATALOGO_EQUIPOS;
	}

	public static String getCodServiciosInteractivos() {
		return COD_SERVICIOS_INTERACTIVOS;
	}

	public static String getCodCertificadoDgac() {
		return COD_CERTIFICADO_DGAC;
	}

	public static String getCodEficienciaEspectral() {
		return COD_EFICIENCIA_ESPECTRAL;
	}

	public static String getCodZonaServicioPtx0() {
		return COD_ZONA_SERVICIO_PTX0;
	}

	public static String getCodZonaServicioPtx1() {
		return COD_ZONA_SERVICIO_PTX1;
	}

	public static String getCodZonaServicioPtx2() {
		return COD_ZONA_SERVICIO_PTX2;
	}

	public static String getCodZonaCoberturaPtx0() {
		return COD_ZONA_COBERTURA_PTX0;
	}

	public static String getCodZonaCoberturaPtx1() {
		return COD_ZONA_COBERTURA_PTX1;
	}

	public static String getCodZonaCoberturaPtx2() {
		return COD_ZONA_COBERTURA_PTX2;
	}

	public static String getCodZonaUrbanaPtx0() {
		return COD_ZONA_URBANA_PTX0;
	}

	public static String getCodZonaUrbanaPtx1() {
		return COD_ZONA_URBANA_PTX1;
	}

	public static String getCodZonaUrbanaPtx2() {
		return COD_ZONA_URBANA_PTX2;
	}

	public static String getSTDOCod(String doc_type) {
		String stdo_cod = "";

		if (doc_type.contains("Zona")) {
			String array_aux[] = doc_type.split("_");
			doc_type = array_aux[0] + "_" + array_aux[1];
		}

		switch (doc_type) {
		case ZONA_SERVICIO_PTX0:
			stdo_cod = COD_ZONA_SERVICIO_PTX0;
			break;
		case ZONA_SERVICIO_PTX1:
			stdo_cod = COD_ZONA_SERVICIO_PTX1;
			break;
		case ZONA_SERVICIO_PTX2:
			stdo_cod = COD_ZONA_SERVICIO_PTX2;
			break;
		case ZONA_COBERTURA_PTX0:
			stdo_cod = COD_ZONA_COBERTURA_PTX0;
			break;
		case ZONA_COBERTURA_PTX1:
			stdo_cod = COD_ZONA_COBERTURA_PTX1;
			break;
		case ZONA_COBERTURA_PTX2:
			stdo_cod = COD_ZONA_COBERTURA_PTX2;
			break;
		case ZONA_URBANA_PTX0:
			stdo_cod = COD_ZONA_URBANA_PTX0;
			break;
		case ZONA_URBANA_PTX1:
			stdo_cod = COD_ZONA_URBANA_PTX1;
			break;
		case ZONA_URBANA_PTX2:
			stdo_cod = COD_ZONA_URBANA_PTX2;
			break;
		case LISTADO_EQUIPOS:
			stdo_cod = COD_LISTADO_EQUIPOS;
			break;
		case MEMORIA_CALCULOS_PERIDAS:
			stdo_cod = COD_MEMORIA_CALCULOS_PERIDAS;
			break;
		case MODELAMIENTO_ANTENA:
			stdo_cod = COD_MODELAMIENTO_ANTENA;
			break;
		case DIAGRAMA_RADIACION_HORIZONTAL:
			stdo_cod = COD_DIAGRAMA_RADIACION_HORIZONTAL;
			break;
		case DIAGRAMA_RADIACION_VERTICAL:
			stdo_cod = COD_DIAGRAMA_RADIACION_VERTICAL;
			break;
		case DESCRIPCION_EQUIPAMIENTO:
			stdo_cod = COD_DESCRIPCION_EQUIPAMIENTO;
			break;
		case CATALOGO_EQUIPOS:
			stdo_cod = COD_CATALOGO_EQUIPOS;
			break;
		case SERVICIOS_INTERACTIVOS:
			stdo_cod = COD_SERVICIOS_INTERACTIVOS;
			break;
		case CERTIFICADO_DGAC:
			stdo_cod = COD_CERTIFICADO_DGAC;
			break;
		case EFICIENCIA_ESPECTRAL:
			stdo_cod = COD_EFICIENCIA_ESPECTRAL;
			break;
		}

		return stdo_cod;
	}

	public static void test_upload(String temp_folder, String user_name) {
		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		String nombre_archivo = "";
		int files_count = files_list.length;

		for (File file : files_list) {
			nombre_archivo = file.getName();
			String doc_path = CarpetaTecnicaFiles.uploadFile(file, nombre_archivo, 12L);
		}
	}

	public static void test_create_folder(Long numero_of_parte) {
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

		File root_folder = new File(PATH);
		if (!root_folder.exists()) {
			try {
				root_folder.mkdir();
			} catch (Exception se) {
				se.printStackTrace();
			}
		}

		File subfolder_year = new File(PATH + FILE_SEPARATOR + year);
		if (!subfolder_year.exists()) {
			try {
				subfolder_year.mkdir();
			} catch (Exception se) {
				se.printStackTrace();
			}
		}

		File subfolder_ofic_partes = new File(PATH + FILE_SEPARATOR + year + FILE_SEPARATOR + numero_of_parte);
		if (!subfolder_ofic_partes.exists()) {
			try {
				subfolder_ofic_partes.mkdir();
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
	}

	public static String uploadFile(File document, String doc_name, Long numero_of_parte) {

		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String doc_path = PATH + FILE_SEPARATOR + year + FILE_SEPARATOR + numero_of_parte + FILE_SEPARATOR;
		String server_path = SERVER_ROOT_PATH + "\\" + year + "\\" + numero_of_parte + "\\";
		doc_name = year + "_" + numero_of_parte + "_" + doc_name;
		doc_name = doc_name.replaceAll("file_cat_", "");

		try {
			File root_folder = new File(PATH);
			if (!root_folder.exists()) {
				try {
					root_folder.mkdir();
				} catch (Exception se) {
					se.printStackTrace();
				}
			}

			File subfolder_year = new File(PATH + FILE_SEPARATOR + year);
			if (!subfolder_year.exists()) {
				try {
					subfolder_year.mkdir();
				} catch (Exception se) {
					se.printStackTrace();
				}
			}

			File subfolder_ofic_partes = new File(PATH + FILE_SEPARATOR + year + FILE_SEPARATOR + numero_of_parte);
			if (!subfolder_ofic_partes.exists()) {
				try {
					subfolder_ofic_partes.mkdir();
				} catch (Exception se) {
					se.printStackTrace();
				}
			}

			InputStream ins = new FileInputStream(document);
			doc_name = CarpetaTecnica.getFormattedName(doc_name);
			OutputStream fos = new FileOutputStream(new File(doc_path + doc_name));
			IOUtils.copy(ins, fos);
			fos.close();
			ins.close();

			server_path += doc_name;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return server_path;
	}
}
