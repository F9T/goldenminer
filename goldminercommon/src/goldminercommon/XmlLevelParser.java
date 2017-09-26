package goldminercommon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlLevelParser {

	/**
	 * Serialise dans un fichier XML les niveaux
	 * @param _levelInfo Les infos des niveaux
	 * @param _path le chemin du fichier à enregistrer
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void serialize(LevelInfo _levelInfo, String _path) throws JAXBException, IOException {
		JAXBContext context=JAXBContext.newInstance(LevelInfo.class);
		Marshaller marshaller=context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		StringWriter stringWriter=new StringWriter();
		marshaller.marshal(_levelInfo, stringWriter);
		FileWriter fileWriter=new FileWriter(_path);
		fileWriter.write(stringWriter.toString());
		fileWriter.close();
	}
	
	/**
	 * Deserialise le fichier des niveaux
	 * @param _file le fichier a deserialiser
	 * @return les infos des niveaux
	 * @throws JAXBException
	 */
	public static LevelInfo deserialize(InputStream _inputStream) throws JAXBException {
		JAXBContext context=JAXBContext.newInstance(LevelInfo.class);
		Unmarshaller unmarshaller=context.createUnmarshaller();
		LevelInfo levelInfo=(LevelInfo)unmarshaller.unmarshal(_inputStream);
		return levelInfo;
	}
	
	/**
	 * Deserialise le fichier des niveaux
	 * @param _file le fichier a deserialiser
	 * @return les infos des niveaux
	 * @throws JAXBException
	 */
	public static LevelInfo deserialize(File _file) throws JAXBException {
		JAXBContext context=JAXBContext.newInstance(LevelInfo.class);
		Unmarshaller unmarshaller=context.createUnmarshaller();
		LevelInfo levelInfo=(LevelInfo)unmarshaller.unmarshal(_file);
		return levelInfo;
	}
}