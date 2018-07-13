package levy.daniel.application.model.utilitaires.transcodeur;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import levy.daniel.application.apptechnic.configurationmanagers.BundleConfigurationProjetManager;
import levy.daniel.application.apptechnic.configurationmanagers.PreferencesManager;
import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.IFournisseurFichiersService;
import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.impl.FournisseurFichiersTraficService;
import levy.daniel.application.model.utilitaires.gestionnairesfichiers.GestionnaireFichiers;
import levy.daniel.application.model.utilitaires.transcodeur.impl.Transcodeur;


/**
 * CLASSE TranscodeurTest :<br/>
 * Test JUnit de la classe Transcodeur (MODEL).<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * chemin absolu des ressources de test, src/test/resources,
 * path des ressources de test,
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author dan Lévy
 * @version 1.0
 * @since 31 mai 2018
 *
 */
public class TranscodeurTest {

	// ************************ATTRIBUTS************************************/
	
	/**
	 * AFFICHAGE_GENERAL : Boolean :<br/>
	 * Boolean qui commande l'affichage pour tous les tests.<br/>
	 */
	public static final Boolean AFFICHAGE_GENERAL = true;

	/**
	 * UNUSED : String :<br/>
	 * "unused".<br/>
	 */
	private static final String UNUSED ="unused";

	/**
	 * SEP_MOINS_ARERE : String :<br/>
	 * " - ".<br/>
	 */
	public static final String SEP_MOINS_ARERE = " - ";
	

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
	 * CHARSET_UTF8 : Charset :<br/>
	 * Charset.forName("UTF-8").<br/>
	 * Eight-bit Unicode (or UCS) Transformation Format.<br/> 
	 */
	public static final Charset CHARSET_UTF8 
		= Charset.forName("UTF-8");

	
	/**
	 * CHARSET_ANSI : Charset :<br/>
	 * Charset.forName("windows-1252").<br/>
	 * ANSI, CP1252.<br/>
	 * 218 caractères imprimables.<br/>
	 * extension d’ISO-8859-1, qui rajoute quelques caractères: œ, € (euro), 
	 * guillemets anglais (« »), points de suspension (...)
	 * , signe «pour mille» (‰), 
	 * tirets cadratin (— = \u2014 en unicode ) et demi-cadratin (–), ...<br/>
	 */
	public static final Charset CHARSET_ANSI
		= Charset.forName("windows-1252");
	
	
	/**
	 * CHARSET_IBM850 : Charset :<br/>
	 * Charset IBM-850.<br/>
	 * Cp850, MS-DOS Latin-1.<br/>
	 */
	public static final Charset CHARSET_IBM850
		= Charset.forName("IBM-850");
	
	
	/**
	 * CHARSET_US_ASCII : Charset :<br/>
	 * Charset.forName("US-ASCII").<br/>
	 * Seven-bit ASCII, a.k.a. ISO646-US, 
	 * a.k.a. the Basic Latin block of the Unicode character set.<br/>
	 * standard américain.<br/>
	 * American Standard Code for Information Interchange.<br/> 
	 * 128 caractères imprimables.<br/> 
	 * Sert à écrire l’anglo-américain.<br/> 
	 * Ne permet pas d’écrire les langues européennes 
	 * qui utilisent des lettres avec diacritiques (accents, cédille, ...).<br/> 
	 * On ne peut pas écrire en français avec de l’ASCII.<br/>
	 */
	public static final Charset CHARSET_US_ASCII 
		= Charset.forName("US-ASCII");

	
	/**
	 * CHARSET_ISO_8859_1 : Charset :<br/>
	 * Charset.forName("ISO-8859-1").<br/>
	 * Latin1.<br/>
	 * 191 caractères imprimables.<br/> 
	 * Permet d’écrire la plupart des langues d’Europe de l’Ouest.<br/> 
	 * Presque tous les caractères du français y sont (manquent le œ et €).<br/>
	 */
	public static final Charset CHARSET_ISO_8859_1 
		= Charset.forName("ISO-8859-1");
	
	/**
	 * CHARSET_ISO_8859_2 : Charset :<br/>
	 * Charset.forName("ISO-8859-2").<br/>
	 * <br/>
	 */
	public static final Charset CHARSET_ISO_8859_2 
		= Charset.forName("ISO-8859-2");
	
	
	/**
	 * CHARSET_ISO_8859_9 : Charset :<br/>
	 * Charset.forName("ISO-8859-9").<br/>
	 * Latin Alphabet No. 5<br/>
	 */
	public static final Charset CHARSET_ISO_8859_9 
		= Charset.forName("ISO-8859-9");

	
	/**
	 * CHARSET_ISO_8859_15 : Charset :<br/>
	 * Charset.forName("ISO-8859-15").<br/>
	 * Latin9, Latin Alphabet No. 9.<br/>
	 * modifie légèrement ISO-8859-1.<br/> 
	 * Ajout du caractère œ et du symbole monétaire € (Euro) entre autres.<br/>
	 */
	public static final Charset CHARSET_ISO_8859_15 
		= Charset.forName("ISO-8859-15");
	
	/**
	 * fileTest : File :<br/>
	 * Fichier avec des multi-encodages pour les tests.<br/>
	 */
	private static transient File fileTest;
	
	
	/**
	 * transcodeur : Transcodeur :<br/>
	 * transcodeur.<br/>
	 */
	private final transient ITranscodeur transcodeur = new Transcodeur();

	
	/**
	 * fournisseurFichiers : IFournisseurFichiersService :<br/>
	 * Fournisseur de Files abstraits pour les fichiers 
	 * intermédiaires et transcodés.<br/>
	 */
	private final transient IFournisseurFichiersService fournisseurFichiers 
		= new FournisseurFichiersTraficService();
	
	
	/**
	 * SET_CHARSETS : Set&lt;Charset&gt; :<br/>
	 * Set de Charsets.<br/>
	 */
	private static final Set<Charset> SET_CHARSETS = new HashSet<Charset>();
	
	static {
		
		SET_CHARSETS.add(CHARSET_ANSI);
		SET_CHARSETS.add(CHARSET_UTF8);
		SET_CHARSETS.add(CHARSET_IBM850);
		SET_CHARSETS.add(CHARSET_US_ASCII);
		SET_CHARSETS.add(CHARSET_ISO_8859_1);
		SET_CHARSETS.add(CHARSET_ISO_8859_2);
		SET_CHARSETS.add(CHARSET_ISO_8859_9);
		SET_CHARSETS.add(CHARSET_ISO_8859_15);
		
	}
	
	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG = LogFactory.getLog(TranscodeurTest.class);
	

	// *************************METHODES************************************/
	
	 /**
	 * method CONSTRUCTEUR TranscodeurTest() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public TranscodeurTest() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	
	
	
	/**
	 * .<br/>
	 * <br/>
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testTranscoder() throws Exception {
						
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = true;
		// **********************************

		// ******************************************************
		final File fichier = this.fournirFichierDIRO2016();
		final Charset charsetDecodage = CHARSET_ANSI;
		final File fichierANSI = this.fournisseurFichiers.fournirFileAnsi(fichier);
		final File fichierUtf8 = this.fournisseurFichiers.fournirFileUtf8(fichier);
		// ******************************************************
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE TranscodeurTest - méthode testTranscoder() ********** ");
		}
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("fichier = " + fichier.getAbsolutePath());
			System.out.println();
		}
		
		assertTrue("le fichier doit exister : "
				, fichier.exists());

		
		// **********************************************************
		// TRANSCODAGE.		
		final File fileResultat = this.transcodeur.transcoder(
				fichier, charsetDecodage, fichierANSI
					, CHARSET_UTF8, fichierUtf8);
		// **********************************************************
		
		
		// RAPPORT DE TRANSCODAGE.
		final List<String> rapportUtilisateur 
			= this.transcodeur.getRapportUtilisateur();
		final List<String> rapportDeveloppeur 
			= this.transcodeur.getRapportDeveloppeur();
		
		assertNotNull(
				"Le rapport utilisateur ne doit pas être null : "
					, rapportUtilisateur);
		assertNotNull(
				"Le rapport développeur ne doit pas être null : "
					, rapportDeveloppeur);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			if (!rapportUtilisateur.isEmpty()) {
				System.out.println("Rapport Utilisateur");
				System.out.println(this.transcodeur.afficherListeString(rapportUtilisateur));
				System.out.println();
			}
			if (!rapportDeveloppeur.isEmpty()) {
				System.out.println("Rapport Développeur");
				System.out.println(this.transcodeur.afficherListeString(rapportDeveloppeur));
				System.out.println();
			}
			
			System.out.println("Fichier résultat : " + fileResultat.getAbsolutePath());
		}
		
	} // Fin de testTranscoder().__________________________________________


	
	
	/**
	 * method testr() :<br/>
	 * .<br/>
	 * <br/>
	 *
	 * @throws Exception :  :  .<br/>
	 */
//	@SuppressWarnings(UNUSED)
//	@Test
//	public void testr() throws Exception {
//						
//		// **********************************
//		// AFFICHAGE DANS LE TEST ou NON
//		final boolean affichage = true;
//		// **********************************
//		
//		final String ligne = "350001401310016 00240 4110201RD 288 2�me ceinture0080587RD 34 ";
//		final String ligne = "220000200110016 00120 4110201D�partement 35      0000000R";
//		final String ligne = "220000500110016 00120 4110201RD81_St-Ren�        0470985RD10_Le Perray ";
//		final String ligne = "220000503310016 00120 4110201non renseign�       0540640non renseign�";
//		final String ligne = "220001400310016 01640 4113001RD793_Merdrignac    0150722La B�li�re";
//		final String ligne = "220002200110016 01640 4810201RD3                 0920935D�partement 29";
//		final String ligne = "220003600310016 01760 4110201RD766_La B�zardais";
//		final String ligne = "290002100110016 01650 4810201RD 4 Echang K�randr�0180150RD 24 Echang K�rampa0250535Miniou";
//		final String ligne = "440002400310016 01650 4110201RN 171 La Mo�re     0330442RD 16 La Sapini�re  0";
//		final String ligne = "1RN 165 La Mo�re     0640900RD 217-RD 93 Sabli�r0680080Savenay";
//		final String ligne = "560003000310016 01660 4110101ECH.DU CREVY        0330350ECH.RN 166          0410125N166_56 PLOERMESOL";
//		
//		final boolean resultat = this.transcodeur.verifierLigne(ligne);
//		
//		/* AFFICHAGE A LA CONSOLE. */
//		if (AFFICHAGE_GENERAL && affichage) {
//			System.out.println("resultat : " + resultat);
//		}
//		
//		// ******************************************************
//		final File fichier = this.fournirFichierDIRO2016();
//		final Charset charsetDecodage = CHARSET_ANSI;
//		final Integer numLigne = 376;
//		// ******************************************************
//		
//		this.transcodeur.setCharsetSupposeEntree(charsetDecodage);
//		
//		final String ligneDecodee 
//			= this.transcodeur.lireLigneN(
//					fichier, numLigne, charsetDecodage);
//		
//		final List<String> rapportUtilisateur 
//			= this.transcodeur.getRapportUtilisateur();
//		final List<String> rapportDeveloppeur 
//			= this.transcodeur.getRapportDeveloppeur();
//		
//		/* AFFICHAGE A LA CONSOLE. */
//		if (AFFICHAGE_GENERAL && affichage) {
//			System.out.println("ligne décodée : " + ligneDecodee);
//			System.out.println();
//			System.out.println("Rapport Utilisateur");
//			System.out.println(this.transcodeur.afficherListeString(rapportUtilisateur));
//			System.out.println();
//			System.out.println("Rapport Développeur");
//			System.out.println(this.transcodeur.afficherListeString(rapportDeveloppeur));
//		}
//		
//		
//		
//		final Entry<Integer, Charset> entry 
//			= this.transcodeur.detecterCharsetReel(numLigne, fichier);
//		
//		/* AFFICHAGE A LA CONSOLE. */
//		if (AFFICHAGE_GENERAL && affichage) {
//			System.out.println(this.transcodeur.afficherEntry(entry));
//		}
//
//		final List<String> rapportUtilisateur 
//			= this.transcodeur.getRapportUtilisateur();
//		final List<String> rapportDeveloppeur 
//			= this.transcodeur.getRapportDeveloppeur();
//		
//		/* AFFICHAGE A LA CONSOLE. */
//		if (AFFICHAGE_GENERAL && affichage) {
//			System.out.println();
//			System.out.println("Rapport Utilisateur");
//			System.out.println(this.transcodeur.afficherListeString(rapportUtilisateur));
//			System.out.println();
//			System.out.println("Rapport Développeur");
//			System.out.println(this.transcodeur.afficherListeString(rapportDeveloppeur));
//		}
//		
//	}
	
	
	
	/**
	 * method fournirFichierDIRO2016() :<br/>
	 * Retourne le File "HITDIRO2016.txt".<br/>
	 * <br/>
	 *
	 * @return "HITDIRO2016.txt".<br/>
	 * 
	 * @throws Exception
	 */
	private File fournirFichierDIRO2016() throws Exception {
		
		final Path pathTrafics2016 
			= PreferencesManager
				.getRepFichiersEntreeParDefaut().toPath();
		
		final Path pathAbsoluFichierDIRO2016 
			= pathTrafics2016.resolve("HITDIRO2016.txt");
		
		return pathAbsoluFichierDIRO2016.toFile();
		
	} // Fin de fournirFichierDIRO2016().__________________________________

	
	/**
	 * method fournirFichierDIRA2016() :<br/>
	 * Retourne le File "HITDIRA2016.txt".<br/>
	 * <br/>
	 *
	 * @return "HITDIRA2016.txt".<br/>
	 * 
	 * @throws Exception
	 */
	private File fournirFichierDIRA2016() throws Exception {
		
		final Path pathTrafics2016 
			= PreferencesManager
				.getRepFichiersEntreeParDefaut().toPath();
		
		final Path pathAbsoluFichierDIRA2016 
			= pathTrafics2016.resolve("HITDIRA2016.txt");
		
		return pathAbsoluFichierDIRA2016.toFile();
		
	} // Fin de fournirFichierDIRA2016().__________________________________
	
	
	
	/**
	 * Fournit un File en ANSI relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_ANSI.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_ANSI" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	private File fournirFileAnsi(
			final File pFile) {
		
		return this.fournirFile(pFile, "ANSI");
		
	} // Fin de fournirFileAnsi(...).______________________________________
	
	
	
	/**
	 * Fournit un File en UTF-8 relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_UTF8.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_UTF8" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	private File fournirFileUtf8(
			final File pFile) {
		
		return this.fournirFile(pFile, "UTF8");
		
	} // Fin de fournirFileUtf8(...).______________________________________

	
	
	/**
	 * Fournit un File en pSuffixe relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_pSuffixe.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_pSuffixe" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 * - retourne null si pFile == null.<br/>
	 * - retourne null si pFile n'a pas de parent et grand-parent.<br/>
	 * <br/>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	private File fournirFile(
			final File pFile
				, final String pSuffixe) {
		
		/* retourne null si pFile == null. */
		if (pFile == null) {
			return null;
		}

		final Path pathFile = pFile.toPath();
		final Path pathFileParent = pathFile.getParent();
		
		if (pathFileParent == null) {
			return null;
		}
		
		final Path pathFileGrandParent = pathFileParent.getParent();
		
		if (pathFileGrandParent == null) {
			return null;
		}
		
		final Path resultat = pathFileGrandParent.resolve(
				"1_Fichiers_originaux_encodés_en_" + pSuffixe +"/" 
						+ nommerFichier(pFile, pSuffixe));
		
		return resultat.toFile();
		
	} // Fin de fournirFile(...).__________________________________________
	

	
	/**
	 * Fabrique un nom de fichier en suffixant son nom.<br/>
	 * <ul>
	 * <li><code>nommerFichier(fileHITDIRO2016Txt, ANSI)</code> 
	 * retourne HITDIRO2016_ANSI.txt</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * @param pSuffixe : String : suffixe à rajouter entre le nom et l'extensin.<br/>
	 * 
	 * @return : String : nom pour un File.<br/>
	 */
	private String nommerFichier(
			final File pFile
				, final String pSuffixe) {
		
		if (pFile == null) {
			return null;
		}
		
		String resultat = null;
		
		final String nomFichier = pFile.getName();
		
		final String[] elements = StringUtils.split(nomFichier, '.');
		
		String nom = null;
		String extension = null;
		
		/* pas d'extension. */
		if (elements.length == 1) {
			nom = elements[0];
		} else {
			
			final int taille = elements.length;			
			extension = elements[taille - 1];
			nom = StringUtils.removeEnd(nomFichier, '.' + extension);
		}
		
		if (pSuffixe != null) {
			if (extension != null) {
				resultat = nom + '_' + pSuffixe + '.' + extension;
			} else {
				resultat = nom + '_' + pSuffixe;
			}
			
		} else {
			resultat = nomFichier;
		}
		
		return resultat;
		
	} // Fin de nommerFichier(...).________________________________________
	

	
	/**
	 * Ecrit dans "fichiers/multi_encodages.txt"
	 *  une même phrase avec divers Charsets.<br/>
	 * <br/>
	 *
	 * @return File : "fichiers/multi_encodages.txt" rempli.<br/>
	 * 
	 * @throws Exception
	 */
	private static File creerFileTestSurDisque() throws Exception {
		
		final File fileTestLocal = fournirFileTest();
		
		if (!fileTestLocal.exists()) {
			
			final String stringBase 
			= "Capharnaüm ! Ôh je bêële mon chârme d'été payé par "
					+ "espèce 15 € sur le marché de Noël de Faÿ-lès-Nemours "
					+ "par Oedipe dans le CHARSET ";
		
			for (final Charset charset : SET_CHARSETS) {
				
				final String phrase = stringBase + charset.displayName() + '\n';
				
				GestionnaireFichiers
					.ecrireStringDansFile(fileTestLocal, phrase, charset, "\n");
				
			}
		
		}
				
		return fileTestLocal;
		
	} // Fin de creerFileTestSurDisque().__________________________________

	
	
	/**
	 * Retourne un fichier abstrait (non écrit sur disque) pour écrire 
	 * des phrases avec des encodages différents.<br/>
	 * <br/>
	 *
	 * @return File : "src/test/resources/fichiers/multi_encodages.txt".<br/>
	 * 
	 * @throws Exception
	 */
	private static File fournirFileTest() throws Exception {
		
		final Path pathAbsoluTestResources 
			= Paths.get(
					BundleConfigurationProjetManager
						.getRacineTestResources());
		
		final Path pathAbsoluFichierTest 
			= pathAbsoluTestResources.resolve(
					"fichiers/multi_encodages.txt");
		
		return pathAbsoluFichierTest.toFile();
		
	} // Fin de fournirFileTest()._________________________________________
	
	

} // FIN DE LA CLASSE TranscodeurTest.---------------------------------------
