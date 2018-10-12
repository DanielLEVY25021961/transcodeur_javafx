/**
 * 
 */
package levy.daniel.application.apptechnic.configurationmanagers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import levy.daniel.application.ConfigurationApplicationManager;

/**
 * CLASSE PreferencesManager :<br/>
 * Classe Utilitaire chargée de gérer les préférences du Transcodeur.<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * Preferences, preferencces, properties, Properties,<br/>
 * sauver Properties, sauver Properties en XML, <br/>
 * fichier properties, fichier Properties, <br/>
 * créer fichier sur disque dur, HDD, créer arborescence sur disque dur,<br/>
 * enregistrer Properties dans fichier .properties,<br/>
 * enregistrer Properties dans fichier .xml,<br/>
 * lire fichier .properties,
 * lire fichier .xml,<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author daniel.levy Lévy
 * @version 1.0
 * @since 29 mai 2018
 *
 */
public final class PreferencesManager {

	// ************************ATTRIBUTS************************************/

	/**
	 * CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR : String :<br/>
	 * Charset supposé pour le fichier en entrée en dur.<br/>
	 * N'est utilisé que si l'application ne peut lire le Charset 
	 * indiqué dans preferences.properties.<br/>
	 * "Windows-1252".<br/>
	 */
	public static final String CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
		= "Windows-1252";
	
	/**
	 * CHARSET_STRING_SORTIE_PAR_DEFAUT_EN_DUR : String :<br/>
	 * Charset par défaut de sortie du transcodage écrit en dur.<br/>
	 * N'est utilisé que si l'application ne peut lire le Charset 
	 * indiqué dans preferences.properties.<br/>
	 * "UTF-8".<br/>
	 */
	public static final String CHARSET_STRING_SORTIE_PAR_DEFAUT_EN_DUR 
		= "UTF-8";
	
	/**
	 * EGAL : char :<br/>
	 * '='.<br/>
	 */
	public static final char EGAL = '=';
	
	/**
	 * SAUT_LIGNE_JAVA : char :<br/>
	 * '\n'.<br/>
	 */
	public static final char SAUT_LIGNE_JAVA = '\n';

	/**
	 * CHARSET_UTF8 : Charset :<br/>
	 * Charset.forName("UTF-8").<br/>
	 * Eight-bit Unicode (or UCS) Transformation Format.<br/> 
	 */
	public static final Charset CHARSET_UTF8 
		= Charset.forName("UTF-8");

	
	/**
	 * KEY_CHARSET_SUPPOSE : String :<br/>
	 * "encodage.entree.suppose".<br/>
	 */
	public static final String KEY_CHARSET_SUPPOSE 
		= "encodage.entree.suppose";
	
	/**
	 * KEY_REP_ENTREE : String :<br/>
	 * "repertoire.entree".<br/>
	 */
	public static final String KEY_REP_ENTREE 
		= "repertoire.entree";
	
	/**
	 * KEY_CHARSET_SORTIE : String :<br/>
	 * "encodage.sortie".<br/>
	 */
	public static final String KEY_CHARSET_SORTIE 
			= "encodage.sortie";
	
	/**
	 * charsetSupposeEntreeParDefaut : Charset :<br/>
	 * Charset par défaut avec lequel on suppose que les 
	 * fichiers en entrée sont encodés.<br/>
	 */
	private static Charset charsetSupposeEntreeParDefaut;
		
	/**
	 * repFichiersEntreeParDefaut : File :<br/>
	 * Répertoire par défaut des fichiers à transcoder.<br/>
	 */
	private static File repFichiersEntreeParDefaut;
	
	/**
	 * charsetSortieParDefaut : Charset :<br/>
	 * Charset de transcodage par défaut.<br/>
	 */
	private static Charset charsetSortieParDefaut;

	/**
	 * preferences : Properties :<br/>
	 * Properties encapsulant les préférences.<br/>
	 */
	private static Properties preferences;
	
	/**
	 * pathAbsoluPreferencesProperties : Path :<br/>
	 * Path absolu vers preferences.properties.<br/>
	 */
	private static Path pathAbsoluPreferencesProperties;
	
	/**
	 * pathAbsoluPreferencesXml : Path :<br/>
	 * Path absolu vers preferences.xml.<br/>
	 */
	private static Path pathAbsoluPreferencesXml;
	
	/**
	 * filePreferencesProperties : File :<br/>
	 * fichier preferences.properties.<br/>
	 */
	private static File filePreferencesProperties;
	
	/**
	 * filePreferencesXml : File :<br/>
	 * fichier preferences.xml.<br/>
	 */
	private static File filePreferencesXml;
	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG 
		= LogFactory.getLog(PreferencesManager.class);
	

	// *************************METHODES************************************/

	 /**
	 * method CONSTRUCTEUR PreferencesManager() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * - private pour bloquer l'instanciation.<br/>
	 */
	private PreferencesManager() {		
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	

	
	/**
	 * initialise preferences en lisant le fichier 
	 * preferences.properties.<br/>
	 * <ul>
	 * <li>instancie le Properties preferences et l'alimente 
	 * avec le fichier preferences.properties.</li>
	 * <li>(Optionnel) instancie le Properties preferences 
	 * et l'alimente avec le fichier preferences.xml.</li>
	 * </ul>
	 * 
	 * @throws Exception 
	 */
	private static void initialiserPreferences() throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* instancie le Properties preferences et l'alimente 
			 * avec le fichier preferences.properties. */
			lireFichierPreferencesProperties();
			
			/* instancie le Properties preferences et l'alimente 
			 * avec le fichier preferences.xml. */
//			lireFichierPreferencesXml();
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de initialiserPreferences().__________________________________
	

	
	/**
	 * sauvegarde sur disque un fichier 
	 * preferences.properties initial.<br/>
	 * <ul>
	 * <li>remplit le fichier preferences.properties 
	 * avec des Properties.</li>
	 * </ul>
	 * 
	 * @throws Exception 
	 */
	private static void initialiserFichierPropertiesInitial() 
											throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				preferences = new Properties();
			}
			
			/* Ajoute les propriétés initiales à preferences. */
			ajouterProperties();
			
			/* initialise les fichiers preferences. */
			instancierAttributsFichierProperties();
			
			/* ECRITURE SUR DISQUE. */
			enregistrerFichierPreferencesProperties();
			enregistrerFichierPreferencesXml();
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de creerFichierPropertiesInitial().___________________________
	


	/**
	 * Ajoute des propriétés initiales au Properties <b>preferences</b>.<br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 */
	private static void ajouterProperties() {
		
		synchronized (PreferencesManager.class) {
			
			preferences.setProperty(KEY_CHARSET_SUPPOSE, CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR);
			preferences.setProperty(KEY_REP_ENTREE, "./src/test/resources/fichiers/Trafic_Histonat/Trafic_2016/0_Fichiers_originaux");
			preferences.setProperty(KEY_CHARSET_SORTIE, CHARSET_STRING_SORTIE_PAR_DEFAUT_EN_DUR);
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de ajouterProperties()._______________________________________
	
	
	
	/**
	 * Instancie tous les attributs relatifs 
	 * aux fichier de preferences.<br/>
	 * Crée les fichiers vides (et leur arborescence) sur HDD.<br/>
	 * <ul>
	 * <li>instancie pathAbsoluPreferencesProperties.</li>
	 * <li>instancie pathAbsoluPreferencesXml.</li>
	 * <li>instancie filePreferencesProperties.</li>
	 * <li>instancie filePreferencesXml.</li>
	 * <li>Crée sur le disque dur l'arborescence et le fichier 
	 * filePreferencesProperties si nécessaire.</li>
	 * <li>Crée sur le disque dur l'arborescence et le fichier 
	 * filePreferencesXml si nécessaire.</li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	private static void instancierAttributsFichierProperties() 
			throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			final Path pathRessourcesExternes 
			= Paths.get(ConfigurationApplicationManager
					.getPathRessourcesExternes());
		
			if (pathAbsoluPreferencesProperties == null) {
				pathAbsoluPreferencesProperties 
				= pathRessourcesExternes
					.resolve("preferences/preferences.properties");
			}
			
			if (pathAbsoluPreferencesXml == null) {
				pathAbsoluPreferencesXml 
				= pathRessourcesExternes
					.resolve("preferences/preferences.xml");
			}
			
			if (filePreferencesProperties == null) {
				filePreferencesProperties 
				= pathAbsoluPreferencesProperties.toFile();
				
				if (!filePreferencesProperties.exists()) {
					creerRepertoiresArbo(filePreferencesProperties);
					Files.createFile(pathAbsoluPreferencesProperties);
				}				
			}
			
			if (filePreferencesXml == null) {
				filePreferencesXml 
				= pathAbsoluPreferencesXml.toFile();
				
				if (!filePreferencesXml.exists()) {
					creerRepertoiresArbo(filePreferencesXml);
					Files.createFile(pathAbsoluPreferencesXml);
				}	
			}
					
		} // Fin du bloc synchronized.__________________
		
	} // Fin de instancierAttributsFichierProperties().____________________
	

	
	/**
	 * Crée sur disque dur l'arborescence des répertoires 
	 * parent de pFile.<br/>
	 * <ul>
	 * <li><code>Files.createDirectories(pathParent);</code></li>
	 * </ul>
	 * - ne fait rien si pFile == null.<br/>
	 * - ne fait rien si pFile est une racine (pas de parent).<br/>
	 * </br/>
	 *
	 * @param pFile : File : 
	 * fichier dont on veut créer l'arborescence sur disque dur.<br/>
	 * 
	 * @throws IOException
	 */
	private static void creerRepertoiresArbo(
			final File pFile) throws IOException {
		
		/* ne fait rien si pFile == null. */
		if (pFile == null) {
			return;
		}
		
		final Path pathFile = pFile.toPath();
		final Path pathParent = pathFile.getParent();
		
		/* ne fait rien si pFile est une racine (pas de parent). */
		if (pathParent != null) {
			Files.createDirectories(pathParent);
		}
				
	} // Fin de creerRepertoiresArbo(...)._________________________________
	
	
	
	/**
	 * retourne un Charset supposé pour le fichier 
	 * en entrée par défaut.<br/>
	 * <ul>
	 * <li>lit le charset stocké dans preferences.properties 
	 * si il n'est pas null.</li>
	 * <li>Windows-1252 (ANSI) sinon.</li>
	 * </ul>
	 *
	 * @return : Charset.<br/>
	 * 
	 * @throws Exception 
	 */
	private static Charset fournirCharsetSupposeEntreeParDefaut() 
			throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}
			
			if (charsetSupposeEntreeParDefaut == null) {
				
				/* lecture dans le properties. */
				final String charsetDansPropertiesString 
					= preferences
						.getProperty(
								fournirKeyCharsetSupposeEntreeParDefaut());
				
				if (charsetDansPropertiesString != null) {
					try {
						charsetSupposeEntreeParDefaut 
						= Charset.forName(charsetDansPropertiesString);
					} catch (Exception e) {
						charsetSupposeEntreeParDefaut 
						= Charset.forName(CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR);
					}
					
				}
				else {
					charsetSupposeEntreeParDefaut 
						= Charset.forName(CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR);
				}
			}
			
			return charsetSupposeEntreeParDefaut;
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de fournirCharsetSupposeEntreeParDefaut().____________________
	

	
	/**
	 * fournit la clé de l'encodage supposé dans 
	 * preferences.properties.<br/>
	 * <ul>
	 * <li>"encodage.entree.suppose"</li>
	 * </ul>
	 *
	 * @return : String.<br/>
	 */
	private static String fournirKeyCharsetSupposeEntreeParDefaut() {
		return KEY_CHARSET_SUPPOSE;
	} // Fin de fournirKeyCharsetSupposeEntreeParDefaut()._________________
	

	
	/**
	 * retourne le répertoire pour les fichiers 
	 * en entrée par défaut.<br/>
	 * <ul>
	 * <li>lit le répertoire stocké dans 
	 * ressources_externes/preferences/preferences.properties 
	 * si il n'est pas null.</li>
	 * <li>"./src/test/resources/fichiers" sinon.</li>
	 * </ul>
	 * - retourne null si repFichiersEntreeParDefaut 
	 * n'existe pas sur le HDD.<br/>
	 * <br/>
	 *
	 * @return : File.<br/>
	 * @throws Exception 
	 */
	private static File fournirRepFichiersEntreeParDefaut() 
			throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}

			if (repFichiersEntreeParDefaut == null) {
				
				/* lecture dans le properties. */
				final String fileDansPropertiesString 
					= preferences
						.getProperty(
								fournirKeyRepEntree());
				
				if (fileDansPropertiesString != null) {
					repFichiersEntreeParDefaut 
						= new File(fileDansPropertiesString);
				}
				else {
					repFichiersEntreeParDefaut 
						= new File("./src/test/resources/fichiers");
				}
			}
			
			if (repFichiersEntreeParDefaut.exists()) {
				return repFichiersEntreeParDefaut;
			}
			
			/* retourne null si repFichiersEntreeParDefaut 
			 * n'existe pas sur le HDD. */
			return null;
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de fournirRepFichiersEntreeParDefaut()._______________________
	
	
	
	/**
	 * fournit la clé du répertoire par défaut des 
	 * fichiers en entrée.<br/>
	 * <ul>
	 * <li>"repertoire.entree"</li>
	 * </ul>
	 *
	 * @return : String.<br/>
	 */
	private static String fournirKeyRepEntree() {
		return KEY_REP_ENTREE;
	} // Fin de fournirKeyRepEntree()._____________________________________
	

	
	/**
	 * retourne un Charset par défaut pour le transcodage (sortie).<br/>
	 * <ul>
	 * <li>lit le charset stocké dans preferences.properties 
	 * si il n'est pas null.</li>
	 * <li>UTF-8 sinon.</li>
	 * </ul>
	 *
	 * @return : Charset.<br/>
	 * 
	 * @throws Exception 
	 */
	private static Charset fournirCharsetSortieParDefaut() 
			throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}
			
			if (charsetSortieParDefaut == null) {
				
				/* lecture dans le properties. */
				final String charsetSortieParDefautString 
					= preferences
						.getProperty(
								fournirKeyCharsetSortieParDefaut());
				
				if (charsetSortieParDefautString != null) {
					try {
						charsetSortieParDefaut 
						= Charset.forName(charsetSortieParDefautString);
					} catch (Exception e) {
						charsetSortieParDefaut 
						= Charset.forName(CHARSET_STRING_SORTIE_PAR_DEFAUT_EN_DUR);
					}
					
				}
				else {
					charsetSortieParDefaut 
						= Charset.forName(CHARSET_STRING_SORTIE_PAR_DEFAUT_EN_DUR);
				}
			}
			
			return charsetSortieParDefaut;
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de fournirCharsetSortieParDefaut().___________________________
	

	
	/**
	 * fournit la clé de l'encodage de sortie dans 
	 * preferences.properties.<br/>
	 * <ul>
	 * <li>"encodage.sortie"</li>
	 * </ul>
	 *
	 * @return : String.<br/>
	 */
	private static String fournirKeyCharsetSortieParDefaut() {
		return KEY_CHARSET_SORTIE;
	} // Fin de fournirKeyCharsetSortieParDefaut()._________________
	

	
	/**
	 * <ul>
	 * <li>Instancie le <i>Properties</i> <b>preferences</b> 
	 * si il est null.</li>
	 * <li>alimente le <i>fichier</i> .properties correspondant
	 * avec des valeurs en dur de la classe si nécessaire.</li>
	 *</ul>
	 *
	 * @throws Exception
	 */
	private static void initialiserPreferencesEtProperties() 
			throws Exception {
		
		/* instancie le Properties preferences 
		 * si preferences == null. */
		if (preferences == null) {
			initialiserPreferences();
		}
		
		/* remplit le fichier .properties avec des valeurs en dur 
		 * de la classe si il est vide. */
		if (preferences.isEmpty()) {
			initialiserFichierPropertiesInitial();
		}

	} // Fin de initialiserPreferencesEtProperties().______________________

	
	
	/**
	 * <b>Crée ou met à jour une Property</b> dans 
	 * le <i>Properties</i> <b>preferences</b>.<br/>
	 * <ul>
	 * <li>initialise le <i>Properties</i> <b>preferences</b> 
	 * et remplit le <i>fichier</i> .properties si nécessaire.</li>
	 * <li>Crée ou maj dans l'objet Properties <b>preferences</b> 
	 * <i>sans enregistrer la modification sur le disque dur</i>.</li>
	 * <li>preferences.setProperty(pKey, pValue);</li>
	 * </ul>
	 * - retourne false si pKey == null.<br/>
	 * - retourne false si pValue == null.<br/>
	 * <br/>
	 *
	 * @param pKey : String : Clé.<br/>
	 * @param pValue : String : Valeur.<br/>
	 * 
	 * @return : boolean : true si la property a été créée.<br/>
	 * 
	 * @throws Exception 
	 */
	public static boolean creerOuModifierProperty(
			final String pKey
				, final String pValue) throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* initialise le Properties preferences et 
			 * remplit le fichier .properties si nécessaire. */
			initialiserPreferencesEtProperties();

			
			/* retourne false si pKey == null. */
			if (pKey == null) {
				return false;
			}
			
			/* retourne false si pValue == null. */
			if (pValue == null) {
				return false;
			}
			
			/* crée ou met à jour la Property dans preferences. */
			preferences.setProperty(pKey, pValue);
			
			return true;
			
		} // Fin du bloc synchronized.__________________
				
	} // Fin de creerOuModifierProperty(...).______________________________

	
	
	/**
	 * <b>Retire une Property</b> dans 
	 * le <i>Properties</i> <b>preferences</b>.<br/>
	 * <ul>
	 * <li>initialise le <i>Properties</i> <b>preferences</b> 
	 * et remplit le <i>fichier</i> .properties si nécessaire.</li>
	 * <li>retire dans l'objet Properties <b>preferences</b> 
	 * <i>sans enregistrer la modification sur le disque dur 
	 * (.properties)</i>.</li>
	 * <li><code>preferences.remove(pKey);</code>.</li>
	 * </ul>
	 * - retourne false si pKey == null.<br/>
	 * <br/>
	 *
	 * @param pKey : String : Clé.<br/>
	 * 
	 * @return : boolean : true si la property a été retirée.<br/>
	 * 
	 * @throws Exception 
	 */
	public static boolean retirerProperty(
			final String pKey) 
					throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* initialise le Properties preferences et 
			 * remplit le fichier .properties si nécessaire. */
			initialiserPreferencesEtProperties();
			
			/* retourne false si pKey == null. */
			if (pKey == null) {
				return false;
			}
			
			/* retire la Property de preferences. */
			preferences.remove(pKey);
			
			return true;
			
		} // Fin du bloc synchronized.__________________
				
	} // Fin de retirerProperty(...).______________________________________
	

	
	/**
	 * vide le <i>Properties</i> <b>preferences</b>.<br/>
	 * <ul>
	 * <li>initialise le <i>Properties</i> <b>preferences</b> 
	 * et remplit le <i>fichier</i> .properties si nécessaire.</li>
	 * <li>vide l'objet <i>Properties</i> <b>preferences</b> 
	 * sans vider le <i>fichier</i> .properties correspondant 
	 * sur le disque dur.</li>
	 * <li><code>preferences.remove(cle);</code>.</li>
	 * </ul>
	 * - retourne false si l'ensemble des clés du 
	 * Properties preferences est null.<br/>
	 * <br/>
	 *
	 * @return : boolean : true si preferences a été vidée.<br/>
	 * 
	 * @throws Exception 
	 */
	public static boolean viderPreferences() throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* initialise le Properties preferences et 
			 * remplit le fichier .properties si nécessaire. */
			initialiserPreferencesEtProperties();
				
			final Set<String> clesSet 
				= preferences.stringPropertyNames();
			
			/* retourne false si l'ensemble des clés 
			 * du Properties preferences est null. */
			if (clesSet == null) {
				return false;
			}
			
			/* vidage du Properties preferences. */
			for (final String cle : clesSet) {
				preferences.remove(cle);
			}
			
			return true;
			
		} // Fin du bloc synchronized.__________________
		
	} // Fin de viderPreferences().________________________________________
	
	
	
	/**
	 * <b>Enregistre en UTF8</b> le <i>Properties</i> preferences dans 
	 * le <i>fichier</i> <b>ressources_externes/preferences/
	 * preferences.properties</b>.<br/>
	 * <ul>
	 * <li>initialise le <i>Properties</i> <b>preferences</b> 
	 * et remplit le <i>fichier</i> .properties si nécessaire.</li>
	 * <li>enregistre le <i>Properties</i> <b>preferences</b> 
	 * sur disque dur dans le <i>fichier</i> 
	 * .properties correspondant.</li>
	 * <li>Prise en compte (stockage) 
	 * d'une modification d'une Property.</li>
	 * <li><code>preferences.store(writer, null);</code></li>
	 * </ul>
	 * 
	 * @throws Exception 
	 */
	public static void enregistrerFichierPreferencesProperties() 
			throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* initialise le Properties preferences et 
			 * remplit le fichier .properties si nécessaire. */
			initialiserPreferencesEtProperties();

			/* initialise les fichiers preferences. */
			instancierAttributsFichierProperties();
			
			/* try-with-resource qui se charge du close(). */
			try (Writer writer = Files.newBufferedWriter(
					pathAbsoluPreferencesProperties, CHARSET_UTF8)) {
				
				/* enregistre le Properties preferences sur disque dur 
				 * dans le fichier .properties correspondant. */
				preferences.store(writer, null);
				
			}

		} // Fin du bloc synchronized.__________________
		
	} // Fin de enregistrerFichierPreferences().___________________________
	

	
	/**
	 * <b>Enregistre en UTF8</b> le <i>Properties</i> preferences dans 
	 * le <i>fichier</i> <b>ressources_externes/preferences/
	 * preferences.xml</b>.<br/>
	 * <ul>
	 * <li>Prise en compte (stockage) 
	 * d'une modification d'une Property.</li>
	 * <li><code>preferences.storeToXML(
	 * outputStream, null, "UTF-8");</code></li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	public static void enregistrerFichierPreferencesXml() 
			throws Exception {
				
		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}

			/* initialise les fichiers preferences. */
			instancierAttributsFichierProperties();
			
			/* try-with-resource qui se charge du close(). */
			try (OutputStream outputStream 
				= Files.newOutputStream(pathAbsoluPreferencesXml)) {
				preferences.storeToXML(outputStream, null, "UTF-8");
			}

		} // Fin du bloc synchronized.__________________

	} // Fin de enregistrerFichierPreferencesXml().________________________
	
	
	
	/**
	 * instancie le <i>Properties</i> <b>preferences</b> 
	 * et l'alimente en décodant en UTF8 le <i>fichier</i> 
	 * <b>ressources_externes/preferences/
	 * preferences.properties</b>.<br/>
	 * <ul>
	 * <li><code>preferences.load(inputStream);</code></li>
	 * </ul>
	 * @throws Exception 
	 */
	public static void lireFichierPreferencesProperties() throws Exception {

		synchronized (PreferencesManager.class) {
			
			/* initialise les attributs relatifs aux fichiers preferences. */
			instancierAttributsFichierProperties();
					
			/* try-with-resource qui se charge du close(). */
			try (Reader reader = Files.newBufferedReader(
					pathAbsoluPreferencesProperties, CHARSET_UTF8)) {
				
				/* instancie le Properties preferences. */
				preferences = new Properties();
				
				/* décode le fichier .properties en UTF8 
				 * et le charge dans le Properties preferences.*/
				preferences.load(reader);
		
			}

		} // Fin du bloc synchronized.__________________
				
	} // Fin de lireFichierPreferences().__________________________________
	

	
	/**
	 * instancie le <i>Properties</i> <b>preferences</b> 
	 * et l'alimente en décodant en UTF8 le <i>fichier</i> 
	 * <b>ressources_externes/preferences/
	 * preferences.xml</b>.<br/>
	 * <ul>
	 * <li><code>preferences.loadFromXML(inputStream);</code></li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	public static void lireFichierPreferencesXml() throws Exception {

		synchronized (PreferencesManager.class) {
			
			/* initialise les fichiers preferences. */
			instancierAttributsFichierProperties();
			
			/* try-with-resource qui se charge du close(). */
			try (InputStream inputStream 
				= Files.newInputStream(pathAbsoluPreferencesXml)) {
				
				/* instancie le Properties preferences. */
				preferences = new Properties();
				
				/* décode le fichier .xml en UTF8 
				 * et le charge dans le Properties preferences.*/
				preferences.loadFromXML(inputStream);
				
			}

		} // Fin du bloc synchronized.__________________
		
	} // Fin de lireFichierPreferences().__________________________________


	
	/**
	 * fournit une String pour l'affichage de preferences.properties.<br/>
	 * <br/>
	 * - retourne null si preferences == null.<br/>
	 * <br/>
	 *
	 * @return : String.<br/>
	 * @throws Exception 
	 */
	public static String afficherPreferences() throws Exception {

		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}
			
			/* retourne null si preferences == null. */
			if (preferences == null) {
				return null;
			}
			
			final StringBuffer stb = new StringBuffer();
			
			for (final String key : preferences.stringPropertyNames()) {
				stb.append(key);
				stb.append(EGAL);
				stb.append(preferences.getProperty(key));
				stb.append(SAUT_LIGNE_JAVA);
			}
			
			return stb.toString();

		} // Fin du bloc synchronized.__________________
		
	} // Fin de afficherPreferences()._____________________________________
	
	

	/**
	 * Getter des Properties encapsulant les préférences.<br/>
	 * <br/>
	 *
	 * @return preferences : Properties : this.preferences.<br/>
	 * @throws Exception 
	 */
	public static Properties getPreferences() throws Exception {

		synchronized (PreferencesManager.class) {
			
			if (preferences == null) {
				initialiserPreferences();
			}
			
			if (preferences.isEmpty()) {
				initialiserFichierPropertiesInitial();
			}

			return preferences;

		} // Fin du bloc synchronized.__________________
		
	} // Fin de getPreferences().__________________________________________

	
	
	/**
	 * Getter du Path absolu vers preferences.properties.<br/>
	 * <br/>
	 *
	 * @return pathAbsoluPreferencesProperties : Path : 
	 * this.pathAbsoluPreferencesProperties.<br/>
	 */
	public static Path getPathAbsoluPreferencesProperties() {
		return pathAbsoluPreferencesProperties;
	} // Fin de getPathAbsoluPreferencesProperties().______________________



	/**
	* Setter du Path absolu vers preferences.properties.<br/>
	* <br/>
	*
	* @param pPathAbsoluPreferencesProperties : Path : 
	* valeur à passer à this.pathAbsoluPreferencesProperties.<br/>
	*/
	public static void setPathAbsoluPreferencesProperties(
			final Path pPathAbsoluPreferencesProperties) {
		pathAbsoluPreferencesProperties = pPathAbsoluPreferencesProperties;
	} // Fin de setPathAbsoluPreferencesProperties(...).___________________



	/**
	 * Getter du Path absolu vers preferences.xml.<br/>
	 * <br/>
	 *
	 * @return pathAbsoluPreferencesXml : Path : 
	 * this.pathAbsoluPreferencesXml.<br/>
	 */
	public static Path getPathAbsoluPreferencesXml() {
		return pathAbsoluPreferencesXml;
	} // Fin de getPathAbsoluPreferencesXml()._____________________________



	/**
	* Setter du Path absolu vers preferences.xml.<br/>
	* <br/>
	*
	* @param pPathAbsoluPreferencesXml : Path : 
	* valeur à passer à this.pathAbsoluPreferencesXml.<br/>
	*/
	public static void setPathAbsoluPreferencesXml(
			final Path pPathAbsoluPreferencesXml) {
		pathAbsoluPreferencesXml = pPathAbsoluPreferencesXml;
	} // Fin de setPathAbsoluPreferencesXml(...).__________________________



	/**
	 * Getter du fichier preferences.properties.<br/>
	 * <br/>
	 *
	 * @return filePreferencesProperties : File : 
	 * this.filePreferencesProperties.<br/>
	 */
	public static File getFilePreferencesProperties() {
		return filePreferencesProperties;
	} // Fin de getFilePreferencesProperties().____________________________



	/**
	* Setter du fichier preferences.properties.<br/>
	* <br/>
	*
	* @param pFilePreferencesProperties : File : 
	* valeur à passer à this.filePreferencesProperties.<br/>
	*/
	public static void setFilePreferencesProperties(
			final File pFilePreferencesProperties) {
		filePreferencesProperties = pFilePreferencesProperties;
	} // Fin de setFilePreferencesProperties(...)._________________________



	/**
	 * Getter du fichier preferences.xml.<br/>
	 * <br/>
	 *
	 * @return filePreferencesXml : File : 
	 * this.filePreferencesXml.<br/>
	 */
	public static File getFilePreferencesXml() {
		return filePreferencesXml;
	} // Fin de getFilePreferencesXml().___________________________________



	/**
	* Setter du fichier preferences.xml.<br/>
	* <br/>
	*
	* @param pFilePreferencesXml : File : 
	* valeur à passer à this.filePreferencesXml.<br/>
	*/
	public static void setFilePreferencesXml(
			final File pFilePreferencesXml) {
		filePreferencesXml = pFilePreferencesXml;
	} // Fin de setFilePreferencesXml(...).________________________________



	/**
	 * Getter du Charset par défaut avec lequel on suppose que les 
	 * fichiers en entrée sont encodés.<br/>
	 * <br/>
	 *
	 * @return charsetSupposeEntreeParDefaut : Charset : 
	 * this.charsetSupposeEntreeParDefaut.<br/>
	 * 
	 * @throws Exception 
	 */
	public static Charset getCharsetSupposeEntreeParDefaut() 
			throws Exception {
		return fournirCharsetSupposeEntreeParDefaut();
	} // Fin de getCharsetSupposeEntreeParDefaut().________________________


	
	/**
	* Setter du Charset par défaut avec lequel on suppose que les 
	* fichiers en entrée sont encodés.<br/>
	* <b>Enregistre la valeur sur disque</b>.<br/>
	* <br/>
	* - ne fait rien si pCharsetSupposeEntreeParDefaut == null.<br/>
	* <br/>
	*
	* @param pCharsetSupposeEntreeParDefaut : Charset : 
	* valeur à passer à this.charsetSupposeEntreeParDefaut.<br/>
	* 
	 * @throws Exception 
	*/
	public static void setCharsetSupposeEntreeParDefaut(
			final Charset pCharsetSupposeEntreeParDefaut) 
					throws Exception {

		synchronized (PreferencesManager.class) {
			
			/* ne fait rien si pCharsetSupposeEntreeParDefaut == null. */
			if (pCharsetSupposeEntreeParDefaut != null) {
				
				charsetSupposeEntreeParDefaut = pCharsetSupposeEntreeParDefaut;
				
				final String nomCharset 
					= pCharsetSupposeEntreeParDefaut.displayName();
				
				if (preferences == null) {
					initialiserPreferences();
				}
				
				if (preferences.isEmpty()) {
					initialiserFichierPropertiesInitial();
				}
				
				creerOuModifierProperty(KEY_CHARSET_SUPPOSE, nomCharset);
				
				enregistrerFichierPreferencesProperties();
				enregistrerFichierPreferencesXml();
			}

		} // Fin du bloc synchronized.__________________
						
	} // Fin de setCharsetSupposeEntreeParDefaut(...)._____________________


	
	/**
	 * Getter du Répertoire par défaut des fichiers à transcoder.<br/>
	 * <br/>
	 *
	 * @return this.repFichiersEntreeParDefaut : File.<br/>
	 * 
	 * @throws Exception 
	 */
	public static File getRepFichiersEntreeParDefaut() throws Exception {
		return fournirRepFichiersEntreeParDefaut();
	} // Fin de getRepFichiersEntreeParDefaut().___________________________



	/**
	* Setter du Répertoire par défaut des fichiers à transcoder.<br/>
	* <b>Enregistre la valeur sur disque</b>.<br/>
	* <br/>
	* - ne fait rien si pRepFichiersEntreeParDefaut == null.<br/>
	* - n'enregiste pas si pRepFichiersEntreeParDefaut n'existe pas.<br/>
	* <br/>
	*
	* @param pRepFichiersEntreeParDefaut : File : 
	* valeur à passer à this.repFichiersEntreeParDefaut.<br/>
	* 
	 * @throws Exception 
	*/
	public static void setRepFichiersEntreeParDefaut(
			final File pRepFichiersEntreeParDefaut) throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* ne fait rien si pRepFichiersEntreeParDefaut == null. */
			if (pRepFichiersEntreeParDefaut != null) {
				
				repFichiersEntreeParDefaut = pRepFichiersEntreeParDefaut;
				
				/* n'enregiste pas si pRepFichiersEntreeParDefaut n'existe pas. */
				if (pRepFichiersEntreeParDefaut.exists()) {
					
					if (preferences == null) {
						initialiserPreferences();
					}
					
					if (preferences.isEmpty()) {
						initialiserFichierPropertiesInitial();
					}
					
					creerOuModifierProperty(KEY_REP_ENTREE
							, pRepFichiersEntreeParDefaut.getAbsolutePath());
					
					enregistrerFichierPreferencesProperties();
					enregistrerFichierPreferencesXml();
				}
			}
		} // Fin du bloc synchronized.__________________		

	} // Fin de setRepFichiersEntreeParDefaut(...).________________________



	/**
	 * Getter du Charset de transcodage par défaut.<br/>
	 * <br/>
	 *
	 * @return charsetSortieParDefaut : Charset : 
	 * charsetSortieParDefaut.<br/>
	 * @throws Exception 
	 */
	public static Charset getCharsetSortieParDefaut() throws Exception {
		return fournirCharsetSortieParDefaut();
	} // Fin de getCharsetSortieParDefaut()._______________________________



	/**
	* Setter du Charset de transcodage par défaut.<br/>
	* <b>Enregistre la valeur sur disque</b>.<br/>
	* <br/>
	* - ne fait rien si pCharsetSortieParDefaut == null.<br/>
	* <br/>
	*
	* @param pCharsetSortieParDefaut : Charset : 
	* valeur à passer à charsetSortieParDefaut.<br/>
	* 
	 * @throws Exception 
	*/
	public static void setCharsetSortieParDefaut(
			final Charset pCharsetSortieParDefaut) 
					throws Exception {
		
		synchronized (PreferencesManager.class) {
			
			/* ne fait rien si pCharsetSupposeEntreeParDefaut == null. */
			if (pCharsetSortieParDefaut != null) {
				
				charsetSortieParDefaut = pCharsetSortieParDefaut;
				
				final String nomCharset 
					= pCharsetSortieParDefaut.displayName();
				
				if (preferences == null) {
					initialiserPreferences();
				}
				
				if (preferences.isEmpty()) {
					initialiserFichierPropertiesInitial();
				}
				
				creerOuModifierProperty(KEY_CHARSET_SORTIE, nomCharset);
				
				enregistrerFichierPreferencesProperties();
				enregistrerFichierPreferencesXml();
			}

		} // Fin du bloc synchronized.__________________
						
	} // Fin de setCharsetSortieParDefaut(...).____________________________

		
	
} // FIN DE LA CLASSE PreferencesManager.------------------------------------
