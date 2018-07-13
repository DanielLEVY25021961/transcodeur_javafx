package levy.daniel.application.model.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import levy.daniel.application.apptechnic.configurationmanagers.ManagerPaths;


/**
 * CLASSE JPAUtilsTest :<br/>
 * Test JUnit de la classe JPAUtilsTest.<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * String vers Path : Paths.get("...");, String to Path, <br/>
 * Path vers String : path.toString();, Path to String, <br/>
 * Ajouter un path2 à path1 : path1.resolve(path2); <br/>
 * Path vers File : path.toFile();, Path to File, <br/>
 * copie de fichiers, Files.copy(), <br/>
 * déplacement de fichiers, écrasement, <br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author dan Lévy
 * @version 1.0
 * @since 27 mai 2018
 *
 */
@PersistenceUnit
public class JPAUtilsTest {

	// ************************ATTRIBUTS************************************/
	
	/**
	 * AFFICHAGE_GENERAL : Boolean :<br/>
	 * Boolean qui commande l'affichage pour tous les tests.<br/>
	 */
	public static final Boolean AFFICHAGE_GENERAL = true;

	
	/**
	 * PERSISTENCE_UNIT : String :<br/>
	 * Persistence-unit déclarée dans META-INF/Persistence.xml.<br/>
	 * "persistence_unit_base-adresses_javafx".<br/>
	 */
	public static final String PERSISTENCE_UNIT 
		= "persistence_unit_base-adresses_javafx";


	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG = LogFactory.getLog(JPAUtilsTest.class);

	// *************************METHODES************************************/
	
	
	 /**
	 * method CONSTRUCTEUR JPAUtilsTest() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public JPAUtilsTest() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	
	

	/**
	 * method testGetEntityManagerFactory() :<br/>
	 * Teste la méthode getEntityManagerFactory().<br/>
	 * <ul>
	 * <li>garantit que l'EntityManagerFactory n'est pas null.</li>
	 * <li>garantit que getEntityManagerFactory() retourne un Singleton.</li>
	 * </ul>
	 */
	@SuppressWarnings("unused")
	@Test
	public void testGetEntityManagerFactory() {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE JPAUtilsTest - méthode testGetEntityManagerFactory() ********** ");
		}
		
		final EntityManagerFactory emf 
			= JPAUtils.getEntityManagerFactory();
		
		/* garantit que l'EntityManagerFactory n'est pas null. */
		assertNotNull(
				"l' EntityManagerFactory ne doit pas être null : "
					, emf);
		
		final EntityManagerFactory emf2 
			= JPAUtils.getEntityManagerFactory();

		/* garantit que getEntityManagerFactory() retourne un Singleton. */
		assertSame(
				"getEntityManagerFactory() doit retourner un Singleton : "
					, emf
						, emf2);
		
	} // Fin de testGetEntityManagerFactory()._____________________________


	
	/**
	 * method testGetProperties() :<br/>
	 * Teste la méthode getProperties().<br/>
	 * <br/>
	 */
	@SuppressWarnings("unused")
	@Test
	public void testGetProperties() {
				
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = true;
		// **********************************
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE JPAUtilsTest - méthode testGetProperties() ********** ");
		}
		
		final Map<String, Object> properties = JPAUtils.getProperties();

		assertNotNull(
				"les Properties ne doivent pas être null : "
				, properties);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("PERSISTENCE-UNIT : " 
					+ JPAUtils.getProperty(
							JPAUtils.PROPERTY_PERSISTENCE_UNIT));
			System.out.println();
			System.out.println(JPAUtils.afficherPrincipalesProperties());
			System.out.println();
//			System.out.println(JPAUtils.afficherProperties());
		}
	} // Fin de testGetProperties()._______________________________________
	

	
	/**
	 * Remet en place le contenu du persistence.xml de PROD.<br/>
	 * <ul>
	 * <li>persistence.xml avait préalablement été copié 
	 * dans "persistence.xml.original".</li>
	 * <li>le contenu de persistence.xml avait été remplacé par le 
	 * contenu de <b>persistence-test.xml</b> durant les tests.</li>
	 * <li>Remplace le contenu de persistence.xml 
	 * (en réalité persistence-test.xml) par le contenu original 
	 * sauvegardé dans "persistence.xml.original".</li>
	 * <li>détruit ensuite "persistence.xml.original".</li>
	 * <li>l'état INITIAL (avant tests) est ainsi restitué.</li>
	 * </ul>
	 * - ne fait rien si pathFichierOriginal == null.<br/>
	 * - ne fait rien si fileOriginal n'existe pas.<br/>
	 * <br/>
	 *
	 * @throws IOException
	 */
	private void activerPersistenceXml() throws IOException {
		
		final Path pathFichierOriginal 
			= this.fournirPathAbsoluPersistenceXml()
				.resolveSibling("persistence.xml.original");
		
		/* ne fait rien si pathFichierOriginal == null. */
		if (pathFichierOriginal == null) {
			return;
		}
		
		final File fileOriginal = pathFichierOriginal.toFile();
		
		/* ne fait rien si fileOriginal n'existe pas. */
		if (!fileOriginal.exists()) {
			return;
		}
		
		/* Remplace le contenu de persistence.xml 
		 * (en réalité persistence-test.xml) par le contenu original 
		 * sauvegardé dans "persistence.xml.original". */
		this.remplacerFichier(
				pathFichierOriginal
					, this.fournirPathAbsoluPersistenceXml());
		
		/* détruit ensuite "persistence.xml.original".*/
		Files.deleteIfExists(pathFichierOriginal);
		
	} // Fin de activerPersistenceXml().___________________________________
	

	
	/**
	 * .<br/>
	 * <ul>
	 * <li>Crée une copie de src/main/resources/META-INF/
	 * <b>persistence.xml</b> nommée "persistence.xml.original" 
	 * dans le même répertoire src/main/resources/META-INF/.</li>
	 * <li>Remplace le contenu de src/main/resources/META-INF/
	 * <b>persistence.xml</b> par le contenu de 
	 * src/test/resources/META-INF/<b>persistence-test.xml</b></li>
	 * <li>Le contenu de persistence-test.xml et donc lu 
	 * par l'EntityManagerFactory.</li>
	 * </ul>
	 *
	 * @throws IOException
	 */
	private void activerPersistenceTestXml() throws IOException {

		final Path pathAbsoluPersistenceXml 
			= this.fournirPathAbsoluPersistenceXml();
		final Path pathAbsoluPersistenceTestXml 
			= this.fournirPathAbsoluPersistenceTestXml();

		/* Copie persistence.xml sous persistence.xml.original. */
		this.copierFichierDansMemeRepertoire(
				pathAbsoluPersistenceXml
					, "persistence.xml.original");

		/* Remplace persistence.xml par le contenu 
		 * de persistence-test.xml. */
		this.remplacerFichier(
				pathAbsoluPersistenceTestXml
					, pathAbsoluPersistenceXml);

	} // Fin de activerPersistenceTestXml()._______________________________
	
	
	
	/**
	 * Copie le fichier situé à pPath dans le <b>même répertoire</b> 
	 * en nommant la copie pNouveauNom.<br/>
	 * <ul>
	 * <li>recopie le fichier situé à pPath</li>
	 * <li>nomme la copie pNouveauNom.</li>
	 * </ul>
	 * - ne fait rien si pPath == null.<br/>
	 * - ne fait rien si le fichier situé à pPath n'existe pas.<br/>
	 * - ne fait rien si le file situé à pPath est un répertoire.<br/>
	 * - ne fait rien si pNouveauNom == null.<br/>
	 * <br/>
	 *
	 * @param pPath : Path : path du fichier à copier.<br/>
	 * @param pNouveauNom : String : Nom à donner à la copie.<br/>
	 * 
	 * @throws IOException 
	 */
	private void copierFichierDansMemeRepertoire(
			final Path pPath
				, final String pNouveauNom) throws IOException {
		
		/* ne fait rien si pPath == null. */
		if (pPath == null) {
			return;
		}
		
		final File fileACopier = pPath.toFile();
		
		/* ne fait rien si le fichier situé à pPath n'existe pas. */
		if (!fileACopier.exists()) {
			return;
		}
		
		/* ne fait rien si le file situé à pPath est un répertoire. */
		if (fileACopier.isDirectory()) {
			return;
		}
		
		/* ne fait rien si pNouveauNom == null. */
		if (pNouveauNom == null) {
			return;
		}
		
		/* COPY et RENOMMAGE. */
		Files.copy(pPath
					, pPath.resolveSibling(pNouveauNom)
						, StandardCopyOption.REPLACE_EXISTING);
		
	} // Fin de copierFichierDansMemeRepertoire(...).______________________
	
	
	
	/**
	 * Ecrase le fichier situé à pPathFichierARemplacer 
	 * et le remplace par le fichier situé à pPathFichierRemplacement.<br/>
	 * <ul>
	 * <li>Remplace le contenu de pPathFichierARemplacer 
	 * par le contenu de 
	 * pPathFichierRemplacement.</li>
	 * </ul>
	 * - ne fait rien si pPathFichierRemplacement == null.<br/>
	 * - ne fait rien si fileRemplacement n'existe pas.<br/>
	 * - ne fait rien si fileRemplacement est un répertoire.<br/>
	 * - ne fait rien si pPathFichierARemplacer == null.<br/>
	 * - ne fait rien si fileARemplacert n'existe pas.<br/>
	 * - ne fait rien si fileARemplacert est un répertoire.<br/>
	 * <br/>
	 *
	 * @param pPathFichierRemplacement : java.nio.Path : 
	 * Path du fichier qui doit remplacer pPathFichierARemplacer.<br/>
	 * @param pPathFichierARemplacer : java.nio.Path : 
	 * Path du fichier à remplacer.<br/>
	 * 
	 * @throws IOException
	 */
	private void remplacerFichier(
			final Path pPathFichierRemplacement
				, final Path pPathFichierARemplacer) throws IOException {
		
		/* ne fait rien si pPathFichierRemplacement == null. */
		if (pPathFichierRemplacement == null) {
			return;
		}
		
		final File fileRemplacement = pPathFichierRemplacement.toFile();
		
		/* ne fait rien si fileRemplacement n'existe pas. */
		if (!fileRemplacement.exists()) {
			return;
		}
		
		/* ne fait rien si fileRemplacement est un répertoire. */
		if (fileRemplacement.isDirectory()) {
			return;
		}
		
		/* ne fait rien si pPathFichierARemplacer == null. */
		if (pPathFichierARemplacer == null) {
			return;
		}
		
		final File fileARemplacert = pPathFichierARemplacer.toFile();
		
		/* ne fait rien si fileARemplacert n'existe pas. */
		if (!fileARemplacert.exists()) {
			return;
		}
		
		/* ne fait rien si fileARemplacert est un répertoire. */
		if (fileARemplacert.isDirectory()) {
			return;
		}
		
		/* ECRASEMENT - REMPLACEMENT du fichier destination. */
		Files.copy(
				pPathFichierRemplacement
					, pPathFichierARemplacer
						, StandardCopyOption.REPLACE_EXISTING);
		
	} // Fin de remplacerFichier(...)._____________________________________
	
	
	
	/**
	 * Fournit le path ABSOLU des main/resources.<br/>
	 * <ul>
	 * <li>Par exemple :<br/>
	 * <code>D:\Donnees\eclipse\eclipseworkspace_oxygen
	 * \adresses_javafx\src\main\resources</code>.</li>
	 * </ul>
	 *
	 * @return Path.<br/>
	 * 
	 * @throws IOException
	 */
	private Path fournirPathAbsoluMainResources() throws IOException {
		
		final Path pathProjet = ManagerPaths.getPathPresentProjet();
		final Path pathMainJavaResources 
			= pathProjet.resolve("src/main/resources");
		
		return pathMainJavaResources;
		
	} // Fin de fournirPathAbsoluMainResources().__________________________

	
	
	/**
	 * Fournit le path ABSOLU de main/resources
	 * /META-INF/persistence.xml.<br/>
	 * <ul>
	 * <li>Par exemple :<br/>
	 * <code>D:\Donnees\eclipse\eclipseworkspace_oxygen\
	 * adresses_javafx\src\main\resources\META-INF\
	 * persistence.xml</code>.</li>
	 * </ul>
	 *
	 * @return Path.<br/>
	 * 
	 * @throws IOException
	 */
	private Path fournirPathAbsoluPersistenceXml() throws IOException {
		
		final String pathStringRelatifPersistenceXml 
			= "META-INF/persistence.xml";
		
		final Path pathSRelatifPersistenceXml 
			= Paths.get(pathStringRelatifPersistenceXml);
		
		final Path pathAbsoluPersistenceXml 
			= this.fournirPathAbsoluMainResources()
				.resolve(pathSRelatifPersistenceXml);
		
		return pathAbsoluPersistenceXml;
		
	} // Fin de fournirPathAbsoluPersistenceXml()._________________________


		
	/**
	 * Fournit le path ABSOLU des test/resources.<br/>
	 * <ul>
	 * <li>Par exemple :<br/>
	 * <code>D:\Donnees\eclipse\eclipseworkspace_oxygen
	 * \adresses_javafx\src\test\resources</code>.</li>
	 * </ul>
	 *
	 * @return Path.<br/>
	 * 
	 * @throws IOException
	 */
	private Path fournirPathAbsoluTestResources() throws IOException {
		
		final Path pathProjet = ManagerPaths.getPathPresentProjet();
		final Path pathTestJavaResources 
			= pathProjet.resolve("src/test/resources");
		
		return pathTestJavaResources;
		
	} // Fin de fournirPathAbsoluTestResources().__________________________
	
	
	
	/**
	 * Fournit le path ABSOLU de test/resources
	 * /META-INF/persistence-test.xml.<br/>
	 * <ul>
	 * <li>Par exemple :<br/>
	 * <code>D:\Donnees\eclipse\eclipseworkspace_oxygen\
	 * adresses_javafx\src\test\resources\META-INF\
	 * persistence-test.xml</code>.</li>
	 * </ul>
	 *
	 * @return Path.<br/>
	 * 
	 * @throws IOException
	 */
	private Path fournirPathAbsoluPersistenceTestXml() throws IOException {
		
		final String pathStringRelatifPersistenceTestXml 
			= "META-INF/persistence-test.xml";
		
		final Path pathSRelatifPersistenceTestXml 
			= Paths.get(pathStringRelatifPersistenceTestXml);
		
		final Path pathAbsoluPersistenceTestXml 
			= this.fournirPathAbsoluTestResources()
				.resolve(pathSRelatifPersistenceTestXml);
		
		return pathAbsoluPersistenceTestXml;
		
	} // Fin de fournirPathAbsoluPersistenceTestXml()._____________________
	

	
	
	/**
	 * .<br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	@BeforeClass
	public static void changerPersistenceXml() throws Exception {
		
		System.out.println("AVANT LA CLASSE");
		
		/* active le persistence-test.xml de TEST. */
		JPAUtils.activerPersistenceTestXml();
				
		System.out.println("AVANT LA CLASSE - APrès Persistence-test.xml");
		
	} // Fin de changerPersistenceXml().___________________________________
	
	
	
	/**
	 * .<br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 *
	 * @throws IOException
	 */
	@AfterClass
	public static void remettrePersistenceXml() throws IOException {
		
		System.out.println("APRES LA CLASSE");
		
		/* ré-active le persistence.xml de PROD. */
		JPAUtils.activerPersistenceXml();
		
		System.out.println("APRES LA CLASSE - APrès Persistence.xml");
		
	} // Fin de remettrePersistenceXml().__________________________________

	
	
} // FIN DE LA CLASSE JPAUtilsTest.------------------------------------------
