/**
 * 
 */
package levy.daniel.application.model.utilitaires.transcodeur.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import levy.daniel.application.model.utilitaires.transcodeur.ITranscodeur;

/**
 * CLASSE Transcodeur :<br/>
 * MODEL.<br/>
 * Classe chargée du <b>transcodage dun fichier textuel</b>.<br/>
 * <b>Transcode un fichier textuel</b> "fichierOrigine" suppose encodé 
 * en "charsetSupposeEntree" en fichier textuel entièrement 
 * encodé en "charsetSortie".<br/>
 * Le résultat transcodé est disponible dans "fichierSortie".<br/>
 * <b>Vérifie ligne par ligne</b> que "fichierOrigine" est <i>réellement</i> 
 * encodé en "charsetSupposeEntree".<br/>
 * Si ce n'est pas le cas, <b>détecte le charset réel d'encodage 
 * de chaque ligne</b> de "fichierOrigine".<br/>
 * Constitue alors un fichier intermédiaire 
 * "fileEntierementEncodeEntree" entièrement encodé 
 * en "charsetSupposeEntree".<br/>
 * C'est alors ce fichier intermédiaire 
 * "fileEntierementEncodeEntree" entièrement encodé 
 * en "charsetSupposeEntree" qui est intégralement transcodé en 
 * "charsetSortie" dans "fichierSortie".<br/>
 * <br/><br/>
 * <img src="../../../../../../../../../../javadoc/images/method_Transcoder.trancoder.mauvais_fichier_1.png" 
 * alt="méthode transcoder(...)" border="1" align="center" /><br/>
 * <img src="../../../../../../../../../../javadoc/images/method_Transcoder.trancoder.mauvais_fichier_2.png" 
 * alt="méthode transcoder(...)" border="1" align="center" />
 * <br/><br/>
 *
 * - Exemple d'utilisation :<br/>
 * <code>new Transcodeur.transcoder(HITDIRO.txt, CHARSET_ANSI, 
 * HITDIRO_ANSI.txt, CHARSET_UTF8, HITDIRO_UTF8.txt);</code> <br/>
 * retourne le fichier HITDIRO_UTF8.txt entièrement trancodé en UTF-8.<br/>
 * Le fichier intermédiaire créé (entièrement encodé en ANSI 
 * alors que HITDIRO.txt ne l'était pas) s'appelle HITDIRO_ANSI.txt.<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * transcodage, décodage, decodage, encodage, ReadAllLines, <br/>
 * afficherMapIntegerString, afficher Map<Integer, String>, <br/>
 * afficherListString, afficherList<String>, afficher Liste String, <br/>
 * afficherListeString, <br/>
 * instancier Entry, instancier Entry<Integer, Charset>, <br/>
 * Instanciation de l'Entry.<br/>
 * new AbstractMap.SimpleEntry<Integer, Charset>(pNumLigne, charset);<br/>
 * String.format, formater String, String.format(
 * Locale.getDefault(), "numéro de ligne : %1$-7d     
 * ligne : %2$s", numLigne, ligne);<br/>
 * lecture dans un fichier, lecture fichier, 
 * bufferedReader.readLine(),<br/>
 * ecriture dans fichier, écriture fichier, 
 * bufferedWriter.write(ligneLue);<br/>
 * bufferedWriter.newLine();bufferedWriter.flush();<br/>
 * conversion de Set en Tableau, 
 * StringUtils.containsAny(String, char[]).<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author daniel.levy Lévy
 * @version 1.0
 * @since 31 mai 2018
 *
 */
public class Transcodeur implements ITranscodeur {

	// ************************ATTRIBUTS************************************/
	

	/**
	 * charsetSupposeEntree : Charset :<br/>
	 * Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 */
	private Charset charsetSupposeEntree;
	
	/**
	 * fileATranscoder : File :<br/>
	 * Fichier à transcoder en charsetSortie.<br/>
	 */
	private File fileATranscoder;

	/**
	 * fileEntierementEncodeEntree : File :<br/>
	 * Fichier intermédiaire entièrement encodé 
	 * en charsetSupposeEntree.<br/>
	 */
	private File fileEntierementEncodeEntree;
	
	/**
	 * charsetSortie : Charset :<br/>
	 * Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 */
	private Charset charsetSortie;

	/**
	 * fichierSortie : File :<br/>
	 * fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 */
	private File fichierSortie;

	/**
	 * rapporterUtilisateur : Boolean :<br/>
	 * Boolean qui stipule si les méthodes doivent générer 
	 * un rapport pour les utilisateurs.<br/>
	 */
	private static Boolean rapporterUtilisateur = true;
	
	/**
	 * rapporterDeveloppeur : Boolean :<br/>
	 * Boolean qui stipule si les méthodes doivent générer 
	 * un rapport pour les développeurs.<br/>
	 */
	private static Boolean rapporterDeveloppeur = true;
	
	/**
	 * rapporterLignesOK : Boolean :<br/>
	 * Boolean qui stipule si la méthode detecterCharsetReel(
	 * Integer, File) doit générer un rapport 
	 * pour les lignes avec détection OK.<br/>
	 */
	private static Boolean rapporterLignesOK = false;
	
	/**
	 * rapporterLignesKO : Boolean :<br/>
	 * Boolean qui stipule si la méthode detecterCharsetReel(
	 * Integer, File) doit générer un rapport 
	 * pour les lignes avec détection KO.<br/>
	 */
	private static Boolean rapporterLignesKO = true;
	
	/**
	 * rapportUtilisateur : List&lt;String&gt; :<br/>
	 * rapport à l'attention des utilisateurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 */
	private final transient List<String> rapportUtilisateur 
		= new ArrayList<String>();
	
	/**
	 * rapportDeveloppeur : List<String>List&lt;String&gt; :<br/>
	 * rapport à l'attention des développeurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 */
	private final transient List<String> rapportDeveloppeur 
		= new ArrayList<String>();
	
	/**
	 * copierFichierTotalementEncode : Boolean :<br/>
	 * Boolean qui détermine si un fichier déjà totalement encodé 
	 * dans un Charset donné doit être recopié 
	 * à un autre emplacement.<br/>
	 * Par exemple, "HITDIRA2016.txt" déjà totalement encodé en ANSI 
	 * sera recopié sous 
	 * "1_Fichiers_originaux_encodés_en_ANSI/HITDIRA2016_ANSI.txt".<br/>
	 */
	private static Boolean copierFichierTotalementEncode = true;
	
	
	static {
		
		/* remplit le set des Charsets disponibles pour le transcodage. */
		remplirCharsetsDisponibles();
		
		// REMPLIT CARACTERES_INDESIRABLES_SET.
		/* ACUTE ACCENT '´' */
		CARACTERES_INDESIRABLES_SET.add('\u00b4');
		/* GRAVE ACCENT '`' */
		CARACTERES_INDESIRABLES_SET.add('\u0060');
		/* CIRCUMFLEX ACCENT '^' */
		CARACTERES_INDESIRABLES_SET.add('\u005e');
		/* BOX DRAWINGS DOUBLE DOWN AND LEFT '╗' */
		CARACTERES_INDESIRABLES_SET.add('\u2557');
		/* BOX DRAWINGS LIGHT DOWN AND LEFT '┐' */
		CARACTERES_INDESIRABLES_SET.add('\u2510');
		/* LATIN CAPITAL LETTER U WITH ACUTE 'Ú' */
		CARACTERES_INDESIRABLES_SET.add('\u00da');
		/* RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK '»' */
		CARACTERES_INDESIRABLES_SET.add('\u00bb');
		/* INVERTED QUESTION MARK '¿' */
		CARACTERES_INDESIRABLES_SET.add('\u00bf');		
		/* LATIN SMALL LETTER D WITH CARON 'ď' */
		CARACTERES_INDESIRABLES_SET.add('\u010f');
		/* LATIN SMALL LETTER T WITH CARON 'ť' */
		CARACTERES_INDESIRABLES_SET.add('\u0165');
		/* LATIN SMALL LETTER Z WITH DOT ABOVE 'ż' */
		CARACTERES_INDESIRABLES_SET.add('\u017c');
		/* LATIN 1 SUPPLEMENT 80 ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u0080');		
		/* LATIN SMALL LETTER R WITH ACUTE 'ŕ' */
		CARACTERES_INDESIRABLES_SET.add('\u0155');
		/* LATIN SMALL LETTER C WITH CARON 'č' */
		CARACTERES_INDESIRABLES_SET.add('\u010d');
		/* LATIN SMALL LETTER E WITH OGONEK 'ę' */
		CARACTERES_INDESIRABLES_SET.add('\u0119');		
		/* LATIN CAPITAL LETTER O WITH ACUTE 'Ó' */
		CARACTERES_INDESIRABLES_SET.add('\u00d3');
		/* LATIN CAPITAL LETTER O WITH CIRCUMFLEX 'Ô' */
//		CARACTERES_INDESIRABLES_SET.add('\u00d4');
		/* LATIN CAPITAL LETTER THORN 'Þ' */
		CARACTERES_INDESIRABLES_SET.add('\u00de');
		/* LATIN CAPITAL LETTER U WITH CIRCUMFLEX 'Û' */
		CARACTERES_INDESIRABLES_SET.add('\u00db');
		/* LATIN CAPITAL LETTER U WITH GRAVE 'Ù' */
		CARACTERES_INDESIRABLES_SET.add('\u00d9');
		/* LATIN CAPITAL LETTER C WITH CEDILLA 'Ç' */
		CARACTERES_INDESIRABLES_SET.add('\u00c7');
		/* NKO LETTER HA 'ߤ' */
		CARACTERES_INDESIRABLES_SET.add('\u07e4');
		/* SYNCHRONOUS IDLE '' */
		CARACTERES_INDESIRABLES_SET.add('\u0016');
		/* INFORMATION SEPARATOR THREE ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u001d');
		/* SYRIAC END OF PARAGRAPH '܀' */
		CARACTERES_INDESIRABLES_SET.add('\u0700');
		/* HEBREW LETTER HE 'ה' */
		CARACTERES_INDESIRABLES_SET.add('\u05d4');
		/* ARABIC LETTER REH WITH SMALL V BELOW 'ה' */
		CARACTERES_INDESIRABLES_SET.add('\u0695');
		/* ARABIC KASRATAN ' ٍ' */
		CARACTERES_INDESIRABLES_SET.add('\u064d');
		/* COPTIC SMALL LETTER GANGIA 'ϫ' */
		CARACTERES_INDESIRABLES_SET.add('\u03eb');
		
		/* NULL ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u0000');
		/* null '޷' */
		CARACTERES_INDESIRABLES_SET.add('\u07b7');
		/* ACKNOWLEDGE '' */
		CARACTERES_INDESIRABLES_SET.add('\u0006');
		/* END OF TEXT '' */
		CARACTERES_INDESIRABLES_SET.add('\u0003');
		/* START OF HEADING '' */
		CARACTERES_INDESIRABLES_SET.add('\u0001');
		/* DEVICE CONTROL TWO '' */
		CARACTERES_INDESIRABLES_SET.add('\u0012');
		/* END OF TRANSMISSION '' */
		CARACTERES_INDESIRABLES_SET.add('\u0004');
		/* DEVICE CONTROL FOUR '' */
		CARACTERES_INDESIRABLES_SET.add('\u0014');
		/* BACKSPACE '' */
		CARACTERES_INDESIRABLES_SET.add('\u0008');
		/* ENQUIRY '' */
		CARACTERES_INDESIRABLES_SET.add('\u0005');
		/* BELL '' */
		CARACTERES_INDESIRABLES_SET.add('\u0007');
		/* CANCEL '' */
		CARACTERES_INDESIRABLES_SET.add('\u0018');
		
		/* REPLACEMENT CHARACTER '�' */
		CARACTERES_INDESIRABLES_SET.add('\ufffd');
		/* LATIN CAPITAL LETTER A WITH TILDE 'Ã' */
		CARACTERES_INDESIRABLES_SET.add('\u00c3');
		/* COPYRIGHT SIGN '©' */
		CARACTERES_INDESIRABLES_SET.add('\u00a9');
		/* DIAERESIS '¨' */
		CARACTERES_INDESIRABLES_SET.add('\u00a8');
		/* CHARACTER TABULATION WITH JUSTIFICATION ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u0089');
		/* LINE TABULATION SET ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u008a');
		/* PER MILLE SIGN '‰' */
		CARACTERES_INDESIRABLES_SET.add('\u2030');
		/* LATIN CAPITAL LETTER S WITH CARON 'Š' */
		CARACTERES_INDESIRABLES_SET.add('\u0160');
		/* SINGLE LOW-9 QUOTATION MARK '‚' */
		CARACTERES_INDESIRABLES_SET.add('\u201a');
		/* BREAK PERMITTED HERE ' ' */
		CARACTERES_INDESIRABLES_SET.add('\u0082');
		
	}
	
	/**
	 * CARACTERES_INDESIRABLES_SET_CHAR : char[] :<br/>
	 * char[] contenant des caractères indésirables 
	 * (impossibles à écrire simplement au clavier).<br/>
	 */
	public static final char[] CARACTERES_INDESIRABLES_SET_CHAR 
		= convertirSetEnTableau(CARACTERES_INDESIRABLES_SET);

	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG = LogFactory.getLog(Transcodeur.class);

	// *************************METHODES************************************/
	
	
	 /**
	 * method CONSTRUCTEUR Transcodeur() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public Transcodeur() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________


	
	/**
	 * remplit le Set des Charsets disponibles 
	 * pour le transcodage <b>CHARSETS_DISPONIBLES</b>.<br/>
	 */
	private static void remplirCharsetsDisponibles() {
		CHARSETS_DISPONIBLES.add(CHARSET_ANSI);
		CHARSETS_DISPONIBLES.add(CHARSET_UTF8);
		CHARSETS_DISPONIBLES.add(CHARSET_IBM850);
	} // Fin de remplirCharsetsDisponibles().______________________________
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final File transcoder(
			final File pFileATranscoder
				, final Charset pCharsetSupposeEntree
					, final File pFileEntierementEncodeEntree
						, final Charset pCharsetSortie
							, final File pFileSortie) 
									throws Exception {
		
		/* retourne null si le fichier en entrée pFileATranscoder 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFileATranscoder
				, METHODE_TRANSCODER
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* décode avec CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
		 * si pCharsetSupposeEntree == null. */
		final Charset charsetLecture 
			= determinerCharsetLecture(pCharsetSupposeEntree);

		/* transcode en CHARSET_UTF8 si pCharsetSortie == null. */
		final Charset charsetSortieLocal 
			= determinerCharsetEcriture(pCharsetSortie);
		
		/* retourne null si pFileEntierementEncodeEntree == null. */
		if (pFileEntierementEncodeEntree == null) {
			return null;
		}
		
		/* retourne null si pFileSortie == null. */
		if (pFileSortie == null) {
			return null;
		}

		/* rafraichit les attributs. */
		this.charsetSupposeEntree = charsetLecture;
		this.fileATranscoder = pFileATranscoder;
		this.fileEntierementEncodeEntree = pFileEntierementEncodeEntree;
		this.charsetSortie = charsetSortieLocal;
		this.fichierSortie = pFileSortie;
		
		
		File fileATranscoderLocal = null;
		
		/* vérifie d'abord si le fichier a été intégralement 
		 * encodé avec pCharsetSupposeEntree. */
		final Boolean encodeCharsetLecture 
			= this.verifierSiFichierEncodeEn(
					pFileATranscoder, charsetLecture);

		// SI LE FICHIER EST INTEGRALEMENT 
		// ENCODE EN charsetLecture.
		if (encodeCharsetLecture) {
		 
			fileATranscoderLocal = pFileATranscoder;
			
			/* recopie le fichier en entrée dans 
			 * pFileEntierementEncodeEntree. */
			if (copierFichierTotalementEncode) {
				this.copierRenommerFile(
						fileATranscoderLocal
							, pFileEntierementEncodeEntree);
				
				/* passage à l'attribut. */
				this.fileEntierementEncodeEntree 
					= fileATranscoderLocal;
			}
			
		// SI LE FICHIER N'EST PAS INTEGRALEMENT 
		// ENCODE EN charsetLecture.
		} else {
									
			/* Crée le fichier pFileEntierementEncodeEntree 
			 * sur disque et écrit dedans 
			 * toutes les lignes entièrement 
			 * encodées en CharsetLecture. */
			fileATranscoderLocal 
				= this.creerFichierEntierementEncode(
					pFileATranscoder
						, charsetLecture
							, pFileEntierementEncodeEntree);
			
			/* passage à l'attribut. */
			this.fileEntierementEncodeEntree 
				= fileATranscoderLocal;						
		}
		
		// TRANSCODAGE INTEGRAL.
		final File fileResultat 
			= this.transcoderFichier(
					fileATranscoderLocal
						, charsetLecture
							, pFileSortie
								, charsetSortieLocal);
		
		/* passage à l'attribut. */
		this.fichierSortie = fileResultat;
		
		return this.fichierSortie;
		
	} // Fin de transcoder(...).___________________________________________


	
	/**
	 * Transcode intégralement un fichier pFileEntree 
	 * <b>encodé intégralement en pCharsetEntree</b> dans un 
	 * fichier pFileSortie encodé intégralement en pFileSortie.<br/>
	 * <ul>
	 * <li>crée sur disque le fichier pFileSortie.</li>
	 * <li>utilise des BufferedReader et BufferedWriter.</li>
	 * <li>Ecrit (annule et remplace) dans le fichier de sortie 
	 * les lignes transcodées.</li>
	 * <li>retourne le fichier de sortie.</li>
	 * </ul>
	 * - retourne null si pCharsetEntree.equals(pCharsetSortie).<br/>
	 * - retourne null si le fichier en entrée pFileEntree 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * - retourne null si pCharsetEntree == null.<br/>
	 * - retourne null si pCharsetSortie == null.<br/>
	 * - retourne null si pFileSortie == null.<br/>
	 * <br/>
	 *
	 * @param pFileEntree : File : fichier encodé 
	 * intégralement en pCharsetEntree<br/>
	 * @param pCharsetEntree : Charset :
	 *  Charset d'encodage de pFileEntree.<br/>
	 * @param pFileSortie : File : fichier résultat encodé 
	 * intégralement en pFileSortie.<br/>
	 * @param pCharsetSortie : Charset.<br/>
	 * 
	 * @return File : pFileSortie stocké sur disque dur 
	 * et contenant pFileEntree transcodé.<br/>
	 * 
	 * @throws IOException
	 */
	private File transcoderFichier(
			final File pFileEntree
				, final Charset pCharsetEntree
					, final File pFileSortie
						, final Charset pCharsetSortie) 
									throws IOException {
				
		/* retourne null si le fichier en entrée pFileEntree 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFileEntree
				, "Méthode transcoderFichier(...)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* retourne null si pCharsetEntree == null. */
		if (pCharsetEntree == null) {
			return null;
		}
		
		/* retourne null si pCharsetSortie == null. */
		if (pCharsetSortie == null) {
			return null;
		}
		
		/* retourne null si pFileSortie == null. */
		if (pFileSortie == null) {
			return null;
		}
		
		/* retourne null si pCharsetEntree.equals(pCharsetSortie). */
		if (pCharsetEntree.equals(pCharsetSortie)) {
			return null;
		}
		
		/* crée sur disque le fichier pFileSortie. */
		this.creerFichierSurDisque(pFileSortie);
		
		// ECRITURE DANS LE FICHIER.
		/* lecteur. */
		final BufferedReader bufferedReader 
			= Files.newBufferedReader(
					pFileEntree.toPath()
						, pCharsetEntree);
		
		/* ecrivain. */
		final BufferedWriter bufferedWriter 
			= Files.newBufferedWriter(
					pFileSortie.toPath()
						, pCharsetSortie
							, StandardOpenOption.WRITE);
		
		String ligneLue = null;
		
		while ((ligneLue = bufferedReader.readLine()) != null) {
			bufferedWriter.write(ligneLue);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		}
		
		bufferedReader.close();
		bufferedWriter.close();
		
		return pFileSortie;
		
	} // Fin de transcoderFichier(...).____________________________________
	

	
	/**
	 * <b>Détecte avec quel Charset</b> la ligne pNumLigne 
	 * du fichier pFile a été <b>réellement encodée</b>.<br/>
	 * <br/>
	 * <img src="../../../../../../../../../../javadoc/images/methode.detecterCharsetReel.png" 
	 * alt="méthode detecterCharsetReel(...)" border="1" align="center" />
	 * <br/>
	 * <br/>
	 * <br/>
	 * <img src="../../../../../../../../../../javadoc/images/methode.detecterCharsetReel_sequence.png" 
	 * alt="méthode detecterCharsetReel(...)" border="1" align="center" />
	 * <br/>
	 * <br/>
	 * Retourne une Entry&lt;Integer, Charset&gt; avec :<br/>
	 * <ul>
	 * <li>Integer : le numéro de la ligne pNumLigne.</li>
	 * <li>Charset : le Charset d'encodage réel de la ligne pNUmLigne.</li>
	 * </ul>
	 * <ul>
	 * <li>décode d'abord avec le Charset supposé 
	 * d'encodage de l'entrée <b>this.charsetSupposeEntree</b>.</li>
	 * <li>essaie ensuite avec les autres Charsets contenus 
	 * dans CHARSETS_DISPONIBLES.</li>
	 * <li>retourne une Entry avec le premier Charset qui a matché.</li>
	 * <li><b>retourne une Entry avec un Charset null</b> si le 
	 * décodage n'a été possible avec aucun Charset.</li>
	 * </ul>
	 * - retourne null si pNumLigne est null ou < 1 
	 * ou > taille fichier.<br/>
	 * - retourne null si le fichier en entrée pFile 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * <br/>
	 *
	 * @param pNumLigne : Integer : numéro 1-based de la ligne.<br/>
	 * @param pFile : File : fichier contenant la ligne.<br/>
	 * 
	 * @return Entry&lt;Integer, Charset&gt;.<br/>
	 * 
	 * @throws Exception
	 */
	private Entry<Integer, Charset> detecterCharsetReel(
			final Integer pNumLigne
				, final File pFile) 
						throws Exception {
		
		/* retourne null si pNumLigne est null ou < 1 
		 * ou > taille fichier. */
		if (!verifierBonneLigne(
				pNumLigne
					, pFile
					, "Méthode detecterCharsetReel(Integer pNumLigne, File pFile)"
						, this.rapportUtilisateur
							, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* retourne null si le fichier en entrée pFile 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFile
				, "Méthode detecterCharsetReel(Integer pNumLigne, File pFile)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
				
		String ligneDecodee = null;
		
		/* décode d'abord avec le Charset supposé 
		 * d'encodage de l'entrée. */
		ligneDecodee 
			= this.lireLigneN(pFile, pNumLigne, this.charsetSupposeEntree);
		
		if (ligneDecodee != null) {
			
			/* vérifie si la ligne a pu être encodée 
			 * en this.charsetSupposeEntree. */
			if (verifierLigne(ligneDecodee)) {
				
				/* Instanciation de l'Entry. */
				final Entry<Integer, Charset> entry 
					= new AbstractMap.SimpleEntry<Integer, Charset>(
							pNumLigne, this.charsetSupposeEntree);
							
				/* Rapport si détection OK. */
				if ((rapporterUtilisateur || rapporterDeveloppeur) 
						&& rapporterLignesOK) {
					this.rapporterDetecterCharsetReelOK(
							pFile, pNumLigne, this.charsetSupposeEntree);
				}
						
				return entry;
				
			}
			
			/* Rapport si détection KO. */
			if ((rapporterUtilisateur || rapporterDeveloppeur) 
					&& rapporterLignesKO) {
				this.rapporterDetecterCharsetReelKO(
						pFile, pNumLigne, this.charsetSupposeEntree);
			}

		}
		
		
		// RECHERCHE DES AUTRES CHARSETS
		for (final Charset charset : CHARSETS_DISPONIBLES) {
			
			/* passe */
			if (charset.equals(this.charsetSupposeEntree)) {
				continue;
			}
			
			/* décode ensuite avec le Charset charset. */
			ligneDecodee 
				= this.lireLigneN(pFile, pNumLigne, charset);
			
			if (ligneDecodee != null) {
				
				/* vérifie si la ligne a pu être encodée 
				 * en charset. */
				if (verifierLigne(ligneDecodee)) {
					
					/* Instanciation de l'Entry. */
					final Entry<Integer, Charset> entry 
						= new AbstractMap.SimpleEntry<Integer, Charset>(
								pNumLigne, charset);
						
					/* Rapport si détection OK. */
					if ((rapporterUtilisateur || rapporterDeveloppeur) 
							&& rapporterLignesOK) {
						this.rapporterDetecterCharsetReelOK(
								pFile, pNumLigne, charset);
					}
					
					return entry;
					
				}
					
				/* Rapport si détection KO. */
				if ((rapporterUtilisateur || rapporterDeveloppeur) 
						&& rapporterLignesKO) {
					this.rapporterDetecterCharsetReelKO(
							pFile, pNumLigne, charset);
				}
			}
									
		}
		
		/* retourne une Entry avec un Charset null si le décodage 
		 * n'a été possible avec aucun Charset. */
		/* Instanciation de l'Entry. */
		final Entry<Integer, Charset> entry 
			= new AbstractMap.SimpleEntry<Integer, Charset>(
				pNumLigne, null);
		
		return entry;
		
	} // Fin de detecterCharsetReel(...).__________________________________
	

	
	/**
	 * <b>Décode</b> et <b>retourne</b> la ligne pNumLigne 
	 * du fichier pFile avec le Charset pCharset.<br/>
	 * <ul>
	 * <li>retourne null si la ligne pNumlLigne n'a pas 
	 * pu être lue avec le Charset pCharset.</li>
	 * </ul>
	 * - retourne null si pFile ne convient pas 
	 * (null, inexistant, répertoire, vide).<br/>
	 * - retourne null si pNumLigne est null ou < 1.<br/>
	 * - retourne null si pCharset == null.<br/>
	 * <br/>
	 *
	 * @param pFile : File : fichier textuel dont 
	 * est extrait la ligne à décoder.<br/>
	 * @param pNumLigne : Integer : numero 1-based de 
	 * la ligne dans le fichier textuel pFile.<br/>
	 * @param pCharset : Charset : Charset de décodage.<br/>
	 * 
	 * @return : String : ligne pNumLigne décodée avec pCharset.<br/>
	 */
	private String lireLigneN(
			final File pFile
				, final Integer pNumLigne
					, final Charset pCharset) {
		
		/* retourne null si le fichier en entrée pFile 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFile
				, "Méthode lireLigneN(File pFile, Integer pNumLigne, Charset pCharset)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* retourne null si pNumLigne est null ou < 1 
		 * ou > taille fichier. */
		if (!verifierBonneLigne(
				pNumLigne
					, pFile
					, "Méthode lireLigneN(File pFile, Integer pNumLigne, Charset pCharset)"
						, this.rapportUtilisateur
							, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* retourne null si pCharset == null. */
		if (pCharset == null) {
			return null;
		}
		
		
		try {
			
			final FileInputStream fileInputStream 
				= new FileInputStream(pFile);
			final InputStreamReader inputStreamReader 
				= new InputStreamReader(fileInputStream, pCharset);					
			final BufferedReader bufferedReader 
				= new BufferedReader(inputStreamReader);
			
			String ligneLue = null;
			boolean finFichier = false;
			int compteur = 0;
			
			while (!finFichier) {
				
				compteur++;
				
				try {
					
					ligneLue = bufferedReader.readLine();
					
					if (ligneLue == null) {
						finFichier = true;
						break;
					}
					
					if (compteur == pNumLigne) {
						return ligneLue;
					}
					
					if (compteur > pNumLigne) {
						
						bufferedReader.close();
						inputStreamReader.close();
						fileInputStream.close();
						
						return null;
					}
					
				}
				catch (Exception e) {
					
					if (compteur > pNumLigne) {
						
						bufferedReader.close();
						inputStreamReader.close();
						fileInputStream.close();
						
						return null;
					}
					
					continue;
				}				
				
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
			
			/* retourne null si la ligne pNumlLigne n'a pas 
			 * pu être lue avec le Charset pCharset. */
			return null;
			
		}
		catch (IOException ioe) {
			
			throw new RuntimeException(
					"Impossible de décoder la ligne " 
							+ pNumLigne + " du fichier " 
							+ pFile.getAbsolutePath() 
							+ "avec le Charset " 
							+ pCharset.displayName()
								, ioe);
		}
				
	} // Fin de lireLigneN(...).___________________________________________
	

	
	/**
	 * Vérifie que la ligne pLigne ne contient aucun 
	 * caractère indésirable contenu dans 
	 * CARACTERES_INDESIRABLES_SET.<br/>
	 * <ul>
	 * <li>retourne true si la ligne ne contient aucun caractère 
	 * indésirable contenu dans CARACTERES_INDESIRABLES_SET.</li>
	 * </ul>
	 * - retourne false si pLigne == null.<br/>
	 * <br/>
	 *
	 * @param pLigne : String.<br/>
	 * 
	 * @return boolean : true si la ligne ne comporte 
	 * aucun caractère indésirable.<br/>
	 * 
	 * @throws Exception
	 */
	private boolean verifierLigne(
			final String pLigne) throws Exception {
		
		/* retourne false si pLigne == null. */
		if (pLigne == null) {
			return false;
		}
				
		if (StringUtils.containsAny(
				pLigne, CARACTERES_INDESIRABLES_SET_CHAR)) {
			return false;
		}
		
		return true;
		
	} // Fin de verifierLigne(...).________________________________________
	

	
	/**
	 * Convertit en Set&lt;Character&gt; en tableau de char[].<br/>
	 * <br/>
	 * - retourne null si pSet == null.<br/>
	 * <br/>
	 *
	 * @param pSet : Set&lt;Character&gt;.<br/>
	 * 
	 * @return : char[].<br/>
	 */
	private static char[] convertirSetEnTableau(
			final Set<Character> pSet) {
		
		/* retourne null si pSet == null. */
		if (pSet == null) {
			return null;
		}
		
		final char[] resultat = new char[pSet.size()];
		
		int i = 0;
		
		for (final Character caractere : pSet) {
			
			final char caractereChar = caractere;
			
			resultat[i] = caractereChar;
			
			i++;
		}
		
		return resultat;
		
	} // Fin de convertirSetEnTableau(...).________________________________
	
	
	
	/**
	 * <b>Décode un fichier textuel</b> avec le 
	 * <b>Charset pCharset</b>.<br/>
	 * <ul>
	 * <li>retourne une <b>Map&lt;Integer, String&gt;</b> contenant 
	 * le fichier textuel avec :
	 * <ul>
	 * <li>Integer : le numero 1-based de la ligne.</li>
	 * <li>String : le contenu de la ligne.</li>
	 * </ul>
	 * </li>
	 * <li>retourne une <b>Map VIDE</b> si le décodage est 
	 * impossible avec pCharset.</li>
	 * <li>utilise <code>Files.readAllLines(pFile.toPath()
	 * , charsetLecture);</code>.</li>
	 * </ul>
	 * - retourne null si le fichier en entrée pFile 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * - décode avec CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
	 * si pCharsetSupposeEntree == null.<br/>
	 * <br/>
	 *
	 * @param pFile : File : fichier à décoder.<br/>
	 * @param pCharset : Charset : Charset pour le decodage de pFile.
	 * 
	 * @return : Map&lt;Integer, String&gt;.<br/>
	 * 
	 * @throws IOException 
	 */
	private Map<Integer, String> decoderFile(
			final File pFile
				, final Charset pCharset) throws IOException {
		
		/* retourne null si le fichier en entrée pFile 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFile
				, "Méthode decoder(File pFile, Charset pCharset)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* décode avec CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
		 * si pCharsetSupposeEntree == null. */
		Charset charsetLecture = null;
		
		if (pCharset == null) {
			charsetLecture 
			= Charset.forName(
					CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR);
			
		} else {
			charsetLecture = pCharset;
		}
		
		
		// DECODAGE. 
		final Map<Integer, String> map 
			= new ConcurrentHashMap<Integer, String>();
		
		List<String> liste = null;
		
		try {
			
			/* Décode l'ensemble du fichier textuel avec charsetLecture. */
			liste = Files.readAllLines(pFile.toPath(), charsetLecture);
			
			int compteur = 0;
			
			for (final String ligne : liste) {
				
				compteur++;
				
				/* construit la map. */
				map.put(compteur, ligne);
			}
			
		}
		catch (MalformedInputException malformedExc) {
			
			/* alimente les rapports si le fichier est 
			 * indécodable avec charsetLecture. */
			if (rapporterUtilisateur || rapporterDeveloppeur) {
				this.rapporterDecoderFileException(
						pFile, charsetLecture, malformedExc);
			}
						
		} catch (Exception exc) {
			
			/* alimente les rapports si le fichier est 
			 * indécodable avec charsetLecture. */
			if (rapporterUtilisateur || rapporterDeveloppeur) {
				this.rapporterDecoderFileException(
						pFile, charsetLecture, exc);
			}
			
			return null;
		}
				
		return map;
		
	} // Fin de decoderFile(...).__________________________________________


	
	/**
	 * retourne un Charset par défaut 
	 * (CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR) 
	 * si pCharset == null.<br/>
	 * retourne pCharset sinon.<br/>
	 * <br/>
	 *
	 * @param pCharset : Charset.<br/>
	 * 
	 * @return : charsetLecture : Charset.<br/>
	 */
	private Charset determinerCharsetLecture(
			final Charset pCharset) {
		
		Charset charsetLecture = null;
		
		if (pCharset == null) {
			charsetLecture 
			= Charset.forName(
					CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR);
			
		} else {
			charsetLecture = pCharset;
		}
		
		return charsetLecture;
		
	} // Fin de determinerCharsetLecture(...)._____________________________
	

	
	/**
	 * retourne un Charset par défaut 
	 * (CHARSET_UTF8) 
	 * si pCharset == null.<br/>
	 * retourne pCharset sinon.<br/>
	 * <br/>
	 *
	 * @param pCharset : Charset.<br/>
	 * 
	 * @return : charsetSortieLocal : Charset.<br/>
	 */
	private Charset determinerCharsetEcriture(
			final Charset pCharset) {
		
		Charset charsetSortieLocal = null;
		
		if (pCharset == null) {
			charsetSortieLocal = CHARSET_UTF8;
		} else {
			charsetSortieLocal = pCharset;
		}
		
		return charsetSortieLocal;
		
	} // Fin de determinerCharsetEcriture(...).____________________________
	
	
	
	/**
	 * Copie à l'identique un fichier pFile à un 
	 * emplacement pFileDestination.<br/>
	 * <ul>
	 * <li>crée le fichier destination pFileDestination sur disque.</li>
	 * <li>recopie pFile dans pFileDestination.</li>
	 * </ul>
	 * - retourne null si le fichier en entrée pFile 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * - retourne null si pFileDestination == null.<br/>
	 * <br/>
	 *
	 * @param pFile : File : fichier à recopier.<br/>
	 * @param pFileDestination : File : 
	 * file devant accueillir le fichier recopié.<br/>
	 * 
	 * @return : File : fichier recopié.<br/>
	 * 
	 * @throws IOException 
	 */
	private File copierRenommerFile(
			final File pFile
				, final File pFileDestination) 
						throws IOException {
		
		/* retourne null si le fichier en entrée pFile 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFile
				, "Méthode copierRenommerFile(...)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return null;
		}
		
		/* retourne null si pFileDestination == null. */
		if (pFileDestination == null) {
			return null;
		}
				
		/* crée le fichier destination pFileDestination sur disque. */
		/* recopie pFile dans pFileDestination. */
		final Path copiePath = Files.copy(
				pFile.toPath()
					, pFileDestination.toPath()
						, StandardCopyOption.REPLACE_EXISTING);
		
		return copiePath.toFile();
		
	} // Fin de copierRenommerFile(...).___________________________________
	
	
	
	/**
	 * Crée un fichier sur disque pFileResultat 
	 * et y écrit le contenu entièrement encodé en pCharset à 
	 * partir du fichier pFile.<br/>
	 * <ul>
	 * <li>crée sur disque le fichier resultat.</li>
	 * <li>Copie dedans (avec remplacement) toutes 
	 * les lignes <b>décodées avec leur Charset réel 
	 * puis encodées avec pCharset</b>.</li>
	 * </ul>
	 *
	 * @param pFile : File : fichier à partir duquel on veut créer 
	 * un fichier entièrement encodé en pCharset.<br/>
	 * @param pCharset : Charset : charset dans lequel on veut 
	 * que pFileResultat soit entièrement encodé.
	 * @param pFileResultat : Fichier sur disque dur dans lequel 
	 * toute les lignes sont entièrment encodées en pCharset.
	 * 
	 * @return : File : pFileResultat.<br/>
	 * 
	 * @throws Exception
	 */
	private File creerFichierEntierementEncode(
			final File pFile
				, final Charset pCharset
					, final File pFileResultat) 
							throws Exception {
		
		final Map<Integer, Charset> mapCharsets 
			= this.fournirMapCharsetsReels(pFile);
		
		final Map<Integer, String> mapFichier 
			= this.decoderFile(pFile, pCharset);
		
		final List<String> fileEntierementEncodeList 
			= new ArrayList<String>();
		
		String ligneACopier = null;
		
		/* Constitue la liste des lignes toutes encodées 
		 * de la même manière. */
		for (final Integer numLigne : mapCharsets.keySet()) {
			
			final Charset charsetReel = mapCharsets.get(numLigne);
			
			if (charsetReel.equals(pCharset)) {
				
				ligneACopier = mapFichier.get(numLigne);
				
			} else {
				
				final Map<Integer, String> mapFichierReel 
					= this.decoderFile(pFile, charsetReel);
				
				ligneACopier = mapFichierReel.get(numLigne);
			}
			
			fileEntierementEncodeList.add(ligneACopier);
		}
		
		/* crée sur disque le fichier resultat. */
		final File fileResultat 
			= this.creerFichierSurDisque(pFileResultat);
		
		/* écrit dans le fichier resultat les lignes 
		 * toutes encodées dans le charset voulu. */
		final Path pathFichierResultat 
			= Files.write(fileResultat.toPath()
					, fileEntierementEncodeList
						, pCharset
							, StandardOpenOption.WRITE);
		
		return pathFichierResultat.toFile();
		
	} // Fin de creerFichierEntierementEncode(...).________________________
	

	
	/**
	 * Crée sur disque dur un fichier VIDE pFile 
	 * si il n'existe pas déjà.<br/>
	 * <ul>
	 * <li>Crée toute l'arborescence parente si nécessaire.</li>
	 * <li>retourne le fichier VIDE créé sur disque 
	 * ou le fichier déjà existant.</li>
	 * </ul>
	 * - retourne null si pFile == null.<br/>
	 * <br/>
	 *
	 * @param pFile : File : fichier à créer sur disque dur.<br/>
	 * 
	 * @return File : pFile vide créé sur le disque.<br/>
	 * 
	 * @throws IOException 
	 */
	private File creerFichierSurDisque(
			final File pFile) 
			throws IOException {
		
		/* retourne null si pFile == null. */
		if (pFile == null) {
			return null;
		}
		
		final Path pathParent = pFile.toPath().getParent();
		
		if (pathParent == null) {
			return null;
		}
		
		/* Crée toute l'arborescence parente si nécessaire. */
		if (!pathParent.toFile().exists()) {
			Files.createDirectories(pathParent);
		}
		
		if (!pFile.exists()) {
			Files.createFile(pFile.toPath());
		}
		
		return pFile;
		
	} // Fin de creerFichierSurDisque(...).________________________________
	
	
	
	/**
	 * Vérifie si le fichier pFile est intégralement 
	 * encodé avec pCharset.<br/>
	 * <ul>
	 * <li>retourne true si il est possible que le fichier 
	 * soit intégralement encodé avec pCharset.</li>
	 * <li>retourne false dès qu'une ligne contient 
	 * un caractère indésirable contenu dans 
	 * CARACTERES_INDESIRABLES_SET.</li>
	 * <li>utilise this.verifierLigne(ligne).</li>
	 * </ul>
	 * - retourne false si le fichier en entrée pFile 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * - retourne false si pCharset == null.<br/>
	 * <br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pCharset Charset.<br/>
	 * 
	 * @return boolean : true si le fichier est intégralement 
	 * encodé avec pCharset.<br/>
	 * 
	 * @throws Exception
	 */
	private boolean verifierSiFichierEncodeEn(
			final File pFile
				, final Charset pCharset) 
							throws Exception {
		
		/* retourne false si le fichier en entrée pFile 
		 * ne convient pas (null, inexistant, répertoire, vide). */
		if (!verifierBonFile(pFile
				, "Méthode decoder(File pFile, Charset pCharset)"
					, this.rapportUtilisateur
						, this.rapportDeveloppeur)) {
			return false;
		}
		
		/* retourne false si pCharset == null. */
		if (pCharset == null) {
			return false;
		}
		
		final Map<Integer, String> lignesMap 
			= this.decoderFile(pFile, pCharset);
		
		final Set<Entry<Integer, String>> entrySet = lignesMap.entrySet();
		
		for (final Entry<Integer, String> entry : entrySet) {
			final String ligne = entry.getValue();
			
			if (!verifierLigne(ligne)) {
				return false;
			}
		}
		
		return true;
		
	} // Fin de verifierSiFichierEncodeEn(...).____________________________
	
	
	
	/**
	 * alimente les rapports 
	 * this.rapportUtilisateur et this.rapportDeveloppeur 
	 * si le fichier pFile est indécodable avec pCharset.<br/>
	 * <br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pCharset : Charset.<br/>
	 * @param pExc : Exception.<br/>
	 */
	private void rapporterDecoderFileException(
			final File pFile
				, final Charset pCharset
					, final Exception pExc) {
		
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append("Méthode decoderFile(File pFile, Charset pCharset)");
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();
		
		final String phraseUtilisateur 
			= "Il est impossible que ce fichier " 
					+ pFile.getName() 
					+ " ait été totalement encodé "
					+ AVEC_LE_CHARSET + pCharset.displayName();
		
		final String messageUtilisateur = phraseUtilisateur;
		
		final String messageDeveloppeur 
			= messageBase 
				+ messageUtilisateur + SEP_MOINS_AERE 
				+ pExc;
		if (rapporterUtilisateur) {
			this.rapportUtilisateur.add(messageUtilisateur);
		}
		
		if (rapporterDeveloppeur) {
			this.rapportDeveloppeur.add(messageDeveloppeur);
		}
		
	} // Fin de rapporterDecoderFileException(...).________________________
	

	
	/**
	 * Retourne true si pNumLigne est un numéro de ligne possible 
	 * du fichier simple existant pFile.<br/>
	 * <ul>
	 * <li>retourne false si pNumLigne == null.</li>
	 * <li>retourne false si pFile == null</li>
	 * <li>retourne false si pFile n'existe pas.</li>
	 * <li>retourne false si pFile est un répertoire.</li>
	 * <li>retourne false si pFile est vide.</li>
	 * <li>retourne false si pNumLigne < 1.</li>
	 * <li>retourne false si pNumLigne > tailleFichier.</li>
	 * <li>rapporte dans rapportUtilisateur et rapportDeveloppeur 
	 * en cas de problème.</li>
	 * </ul>
	 *
	 * @param pNumLigne : Integer : 
	 * numéro 1-based d'une ligne du fichier pFile.<br/>
	 * @param pFile : java.io.File.<br/>
	 * @param pMethode : String : nom de la méthode appelante.<br/>
	 * @param pRapportUtilisateur : List&lt;String&gt;.<br/>
	 * @param pRapportDeveloppeur : List&lt;String&gt;.<br/>
	 * 
	 * @return : boolean : true si pNumLigne est un 
	 * numéro de ligne possible dans le fichier 
	 * simple existant pFile.<br/>
	 */
	private boolean verifierBonneLigne(
			final Integer pNumLigne
				, final File pFile
					, final String pMethode
						, final List<String> pRapportUtilisateur
							, final List<String> pRapportDeveloppeur) {
		
		if (pMethode == null) {
			throw new RuntimeException(
					"pMethode ne peut être null");
		}
		
		if (pRapportUtilisateur == null) {
			throw new RuntimeException(
					"pRapportUtilisateur ne peut être null");
		}
		
		if (pRapportDeveloppeur == null) {
			throw new RuntimeException(
					"pRapportDeveloppeur ne peut être null");
		}
		
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append(pMethode);
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();
		
		String messageUtilisateur = null;
		String messageDeveloppeur = null;
		
		/* retourne false si pNumLigne == null. */
		if (pNumLigne == null) {
			
			messageDeveloppeur = messageBase 
					+ "le numéro de ligne passé en paramètre est null !!!";
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
						
			return false;
		}
		
		/* retourne false si pFile n'est pas un 
		 * fichier simple existant. */
		if (!verifierBonFile(
				pFile, pMethode
					, pRapportUtilisateur
						, pRapportDeveloppeur)) {
			
			return false;
		}
		
		/* retourne false si pNumLigne < 1. */
		if (pNumLigne < 1) {
			
			final String message 
			= "Le numéro de ligne : " 
					+ pNumLigne 
						+ " n'existe pas (< 1) !!!";
		
			messageDeveloppeur = messageBase + message;
			messageUtilisateur = message;
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
			
			if (rapporterUtilisateur) {
				pRapportUtilisateur.add(messageUtilisateur);
			}
					
			return false;			
		}
		
		int tailleFichier = 0;
		
		try {
			
			tailleFichier = this.compterNombreLignes(pFile);
			
			/* retourne false si pNumLigne > tailleFichier. */
			if (pNumLigne > tailleFichier) {
				
				final String message 
				= "Le numéro de ligne : " 
						+ pNumLigne 
							+ " n'existe pas "
							+ "( > nombre de lignes du fichier "
							+  tailleFichier + ") !!!";
			
				messageDeveloppeur = messageBase + message;
				messageUtilisateur = message;
				
				if (rapporterDeveloppeur) {
					pRapportDeveloppeur.add(messageDeveloppeur);
				}
				
				if (rapporterUtilisateur) {
					pRapportUtilisateur.add(messageUtilisateur);
				}
						
				return false;			
			}
		}
		catch (IOException e) {
			
			final String message 
				= "impossible de compter le nombre de lignes du fichier "
					+ "avec le Charset ANSI. "
					+ "Pas de contrôle du nombre de lignes";
		
			messageDeveloppeur = messageBase + message;
			messageUtilisateur = message;
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
			
			if (rapporterUtilisateur) {
				pRapportUtilisateur.add(messageUtilisateur);
			}
			
		}
		
		return true;
		
	} // Fin de verifierBonneLigne(...).___________________________________
	

	
	/**
	 * Retourne true si un File pFile est un 
	 * bon fichier simple existant.<br/>
	 * <ul>
	 * <li>retourne false si pFile == null</li>
	 * <li>retourne false si pFile n'existe pas.</li>
	 * <li>retourne false si pFile est un répertoire.</li>
	 * <li>retourne false si pFile est vide.</li>
	 * <li>rapporte dans rapportUtilisateur et rapportDeveloppeur 
	 * en cas de problème.</li>
	 * </ul>
	 *
	 * @param pFile : java.io.File.<br/>
	 * @param pMethode : String : nom de la méthode appelante.<br/>
	 * @param pRapportUtilisateur : List&lt;String&gt;.<br/>
	 * @param pRapportDeveloppeur : List&lt;String&gt;.<br/>
	 * 
	 * @return : boolean : true si pFile est un 
	 * fichier simple existant.<br/>
	 */
	private boolean verifierBonFile(
			final File pFile
				, final String pMethode
					, final List<String> pRapportUtilisateur
						, final List<String> pRapportDeveloppeur) {
		
		if (pMethode == null) {
			throw new RuntimeException(
					"pMethode ne peut être null");
		}
		
		if (pRapportUtilisateur == null) {
			throw new RuntimeException(
					"pRapportUtilisateur ne peut être null");
		}
		
		if (pRapportDeveloppeur == null) {
			throw new RuntimeException(
					"pRapportDeveloppeur ne peut être null");
		}
		
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append(pMethode);
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();
		
		String messageUtilisateur = null;
		String messageDeveloppeur = null;
		
		/* retourne false si pFile == null. */
		if (pFile == null) {
			
			messageDeveloppeur = messageBase 
					+ "le fichier passé en paramètre est null !!!";
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
						
			return false;
		}
		
		/* retourne false si pFile n'existe pas. */
		if (!pFile.exists()) {
			
			final String message 
				= "Le fichier : " 
						+ pFile.getAbsolutePath() 
							+ " n'existe pas !!!";
			
			messageDeveloppeur = messageBase + message;
			messageUtilisateur = message;
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
			
			if (rapporterUtilisateur) {
				pRapportUtilisateur.add(messageUtilisateur);
			}
					
			return false;
		}
		
		/* retourne false si pFile est un répertoire. */
		if (pFile.isDirectory()) {
			
			final String message 
				= "Le fichier : " 
					+ pFile.getAbsolutePath() 
						+ " est un répertoire !!!";
			
			messageDeveloppeur = messageBase + message;
			messageUtilisateur = message;
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
			
			if (rapporterUtilisateur) {
				pRapportUtilisateur.add(messageUtilisateur);
			}
						
			return false;
		}
		
		/* retourne false si pFile est vide. */
		if (pFile.length() == 0) {
			
			final String message 
			= "Le fichier : " 
				+ pFile.getAbsolutePath() 
					+ " est vide !!!";
		
			messageDeveloppeur = messageBase + message;
			messageUtilisateur = message;
			
			if (rapporterDeveloppeur) {
				pRapportDeveloppeur.add(messageDeveloppeur);
			}
			
			if (rapporterUtilisateur) {
				pRapportUtilisateur.add(messageUtilisateur);
			}
					
			return false;			
		}
		
		return true;
		
	} // Fin de verifierBonFile(...).______________________________________
	


	/**
	 * alimente les rapports 
	 * this.rapportUtilisateur et this.rapportDeveloppeur 
	 * si le fichier pFile est indécodable avec pCharset 
	 * et que lireLigneN(...) ne peut pas s'exécuter.<br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pNumLigne : Integer.<br/>
	 * @param pCharset : Charset.<br/>
	 */
	private void rapporterLireLigneNProbleme(
			final File pFile
				, final Integer pNumLigne
					, final Charset pCharset) {
		
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append("Méthode lireLigneN(File pFile"
				+ ", Integer pNumLigne, Charset pCharset)");
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();
		
		final String phraseUtilisateur 
			= "Il est impossible que cette ligne numéro " 
					+ pNumLigne + DU_FICHIER + pFile.getName() 
						+ AIT_ETE_ENCODEE
							+ AVEC_LE_CHARSET 
								+ pCharset.displayName();
		
		final String messageUtilisateur = phraseUtilisateur;
		
		final String messageDeveloppeur 
			= messageBase 
				+ messageUtilisateur;
		
		if (rapporterUtilisateur) {
			this.rapportUtilisateur.add(messageUtilisateur);
		}
		
		if (rapporterDeveloppeur) {
			this.rapportDeveloppeur.add(messageDeveloppeur);
		}
				
	} // Fin de rapporterLireLigneNProbleme(...).__________________________
	

	
	/**
	 * Fournit la map des Charsets pour chaque ligne.<br/>
	 * <ul>
	 * <li>Integer : numéro de ligne 1-based.</li>
	 * <li>Charset : Charset réel d'encodage de la ligne.</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return Map&lt;Integer, Charset&gt;
	 * 
	 * @throws Exception
	 */
	private Map<Integer, Charset> fournirMapCharsetsReels(
			final File pFile) 
			throws Exception {
		
		final Map<Integer, Charset> resultat 
			= new ConcurrentHashMap<Integer, Charset>();
		
		final int nbreLignes = this.compterNombreLignes(pFile);
		
		for (int i = 1; i <= nbreLignes; i++) {
			
			final Entry<Integer, Charset> entryDetecte 
				= this.detecterCharsetReel(i, pFile);
			resultat.put(entryDetecte.getKey(), entryDetecte.getValue());
		}
		
		return resultat;
		
	} // Fin de fournirMapCharsetsReels(...).______________________________
	

	
	/**
	 * Retourne le nombre de lignes d'un fichier textuel pFile.<br/>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return int : nombre de lignes.
	 * 
	 * @throws IOException
	 */
	private int compterNombreLignes(
			final File pFile) throws IOException {
		
		List<String> liste = null;
		
		try {
			
			liste = Files.readAllLines(pFile.toPath(), CHARSET_ANSI);
		}
		catch (Exception exc1) {
			
			try {
				liste = Files.readAllLines(pFile.toPath(), CHARSET_UTF8);
			} catch (Exception exc2) {
				try {
					liste = Files.readAllLines(pFile.toPath(), CHARSET_IBM850);
				} catch (Exception exc3) {
				return 0;
				}
			}
		}
		
		if (liste != null) {
			return liste.size();
		}
		
		throw new RuntimeException(
				"Impossible de compter les lignes du fichier");
		
	} // Fin de compterNombreLignes(...).__________________________________
	
	
	
	/**
	 * alimente les rapports 
	 * this.rapportUtilisateur et this.rapportDeveloppeur 
	 * si la détection du Charset d'une ligne a fonctionné.<br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pNumLigne : Integer.<br/>
	 * @param pCharset : Charset.<br/>
	 */
	private void rapporterDetecterCharsetReelOK(
			final File pFile
				, final Integer pNumLigne
					, final Charset pCharset) {
		
		/* Rapport. */
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append("Méthode detecterCharsetReel("
				+ "Integer pNumLigne, File pFile)");
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();

		final String phraseUtilisateur 
		= "Il est possible que cette ligne numéro " 
				+ pNumLigne + DU_FICHIER + pFile.getName() 
					+ AIT_ETE_ENCODEE
						+ AVEC_LE_CHARSET 
							+ pCharset.displayName();
	
		final String messageUtilisateur = phraseUtilisateur;
		
		final String messageDeveloppeur 
			= messageBase 
				+ messageUtilisateur;
		
		if (rapporterUtilisateur) {
			this.rapportUtilisateur.add(messageUtilisateur);
		}
		
		if (rapporterDeveloppeur) {
			this.rapportDeveloppeur.add(messageDeveloppeur);
		}
		
	} // Fin de rapporterDetecterCharsetReelOK(...)._______________________
	
	
	
	/**
	 * alimente les rapports 
	 * this.rapportUtilisateur et this.rapportDeveloppeur 
	 * si la détection du Charset d'une ligne n'a pas fonctionné.<br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pNumLigne : Integer.<br/>
	 * @param pCharset : Charset.<br/>
	 */
	private void rapporterDetecterCharsetReelKO(
			final File pFile
				, final Integer pNumLigne
					, final Charset pCharset) {
		
		
		/* Rapport. */
		final StringBuffer stbBase = new StringBuffer();
		
		stbBase.append(CLASSE_TRANSCODEUR);
		stbBase.append(SEP_MOINS_AERE);
		stbBase.append("Méthode detecterCharsetReel("
				+ "Integer pNumLigne, File pFile)");
		stbBase.append(SEP_MOINS_AERE);
		
		final String messageBase 
			= stbBase.toString();
		
		final String phraseUtilisateur 
		= "Il est impossible que cette ligne numéro " 
				+ pNumLigne + DU_FICHIER + pFile.getName() 
					+ AIT_ETE_ENCODEE
						+ AVEC_LE_CHARSET 
							+ pCharset.displayName();
	
		final String messageUtilisateur = phraseUtilisateur;
		
		final String messageDeveloppeur 
			= messageBase 
				+ messageUtilisateur;
		
		if (rapporterUtilisateur) {
			this.rapportUtilisateur.add(messageUtilisateur);
		}
		
		if (rapporterDeveloppeur) {
			this.rapportDeveloppeur.add(messageDeveloppeur);
		}
		
	} // Fin de rapporterDetecterCharsetReelKO(...)._______________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String afficherMapIntegerString(
			final Map<Integer, String> pMap) {
		
		/* retourne null si pMap == null. */
		if (pMap == null) {
			return null;
		}
		
		final StringBuffer stb = new StringBuffer();
		
		final Set<Entry<Integer, String>> entrySet = pMap.entrySet();
		final Iterator<Entry<Integer, String>> ite = entrySet.iterator();
		
		while (ite.hasNext()) {
			
			final Entry<Integer, String> entry = ite.next();
			
			final Integer numLigne = entry.getKey();
			final String ligne = entry.getValue();
			
			final String ligneAffichee 
				= String.format(
						Locale.getDefault()
						, "numéro de ligne : %1$-7d     ligne : %2$s"
						, numLigne, ligne);
			
			stb.append(ligneAffichee);
			stb.append(SAUT_LIGNE_JAVA);
		}
		
		return stb.toString();
		
	} // Fin de afficherMapIntegerString(...)._____________________________
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String afficherMapIntegerCharset(
			final Map<Integer, Charset> pMap) {
		
		/* retourne null si pMap == null. */
		if (pMap == null) {
			return null;
		}
		
		final StringBuffer stb = new StringBuffer();
		
		final Set<Entry<Integer, Charset>> entrySet = pMap.entrySet();
		final Iterator<Entry<Integer, Charset>> ite = entrySet.iterator();
		
		while (ite.hasNext()) {
			
			final Entry<Integer, Charset> entry = ite.next();
			
			final Integer numLigne = entry.getKey();
			final Charset charset = entry.getValue();
			
			final String ligneAffichee 
				= String.format(
						Locale.getDefault()
						, "numéro de ligne : %1$-7d     Charset : %2$s"
						, numLigne, charset);
			
			stb.append(ligneAffichee);
			stb.append(SAUT_LIGNE_JAVA);
		}
		
		return stb.toString();
		
	} // Fin de afficherMapIntegerCharset(...).____________________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String afficherListeString(
			final List<String> pList) {
		
		/* retourne null si pList == null. */
		if (pList == null) {
			return null;
		}
		
		final StringBuffer stb = new StringBuffer();
		
		for (final String ligne : pList) {
			stb.append(ligne);
			stb.append(SAUT_LIGNE_JAVA);
		}
		
		return stb.toString();
		
	} // Fin de afficherListeString(...).__________________________________
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String afficherEntry(
			final Entry<Integer, Charset> pEntry) {
		
		if (pEntry == null) {
			return null;
		}
				
		final Integer numLigne = pEntry.getKey();
		final Charset charset = pEntry.getValue();
		final String nomCharset = charset.displayName();
		
		final String stringAAfficher 
			= String.format(
					Locale.getDefault()
						, "numéro de ligne : %1$-7d     Charset : %2$s"
							, numLigne, nomCharset);
		
		return stringAAfficher;
		
	} // Fin de afficherEntry(...).________________________________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Charset getCharsetSupposeEntree() {
		return this.charsetSupposeEntree;
	} // Fin de getCharsetSupposeEntree()._________________________________
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharsetSupposeEntree(
			final Charset pCharsetSupposeEntree) {
		this.charsetSupposeEntree = pCharsetSupposeEntree;
	} // Fin de setCharsetSupposeEntree(...).______________________________
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileATranscoder() {
		return this.fileATranscoder;
	} // Fin de getFileATranscoder().______________________________________



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFileATranscoder(
			final File pFileATranscoder) {
		this.fileATranscoder = pFileATranscoder;
	} // Fin de setFileATranscoder(...).___________________________________
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFileEntierementEncodeEntree() {
		return this.fileEntierementEncodeEntree;
	} // Fin de getFileEntierementEncodeEntree().__________________________


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFileEntierementEncodeEntree(
			final File pFileEntierementEncodeEntree) {
		this.fileEntierementEncodeEntree = pFileEntierementEncodeEntree;
	} // Fin de setFileEntierementEncodeEntree(...)._______________________



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Charset getCharsetSortie() {
		return this.charsetSortie;
	} // Fin de getCharsetSortie().________________________________________



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCharsetSortie(
			final Charset pCharsetSortie) {
		this.charsetSortie = pCharsetSortie;
	} // Fin de setCharsetSortie(...)._____________________________________
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFichierSortie() {
		return this.fichierSortie;
	} // Fin de getFichierSortie().________________________________________



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFichierSortie(
			final File pFichierSortie) {
		this.fichierSortie = pFichierSortie;
	} // Fin de setFichierSortie(...)/_____________________________________


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getRapportUtilisateur() {
		return this.rapportUtilisateur;
	} // Fin de getRapportUtilisateur().___________________________________


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getRapportDeveloppeur() {
		return this.rapportDeveloppeur;
	} // Fin de getRapportDeveloppeur().___________________________________


	
	/**
	 * Getter du Boolean qui stipule si les méthodes doivent générer 
	 * un rapport pour les utilisateurs.<br/>
	 * <br/>
	 *
	 * @return rapporterUtilisateur : Boolean.<br/>
	 */
	public static Boolean getRapporterUtilisateur() {
		return rapporterUtilisateur;
	} // Fin de getRapporterUtilisateur()._________________________________



	/**
	* Setter du Boolean qui stipule si les méthodes doivent générer 
	* un rapport pour les utilisateurs.<br/>
	* <br/>
	*
	* @param pRapporterUtilisateur : Boolean : 
	* valeur à passer à rapporterUtilisateur.<br/>
	*/
	public static void setRapporterUtilisateur(
			final Boolean pRapporterUtilisateur) {
		rapporterUtilisateur = pRapporterUtilisateur;
	} // Fin de setRapporterUtilisateur(...).______________________________



	/**
	 * Getter du Boolean qui stipule si les méthodes doivent générer 
	 * un rapport pour les développeurs.<br/>
	 * <br/>
	 *
	 * @return rapporterDeveloppeur : Boolean : 
	 * rapporterDeveloppeur.<br/>
	 */
	public static Boolean getRapporterDeveloppeur() {
		return rapporterDeveloppeur;
	} // Fin de getRapporterDeveloppeur()._________________________________



	/**
	* Setter du Boolean qui stipule si les méthodes doivent générer 
	* un rapport pour les développeurs.<br/>
	* <br/>
	*
	* @param pRapporterDeveloppeur : Boolean : 
	* valeur à passer à rapporterDeveloppeur.<br/>
	*/
	public static void setRapporterDeveloppeur(
			final Boolean pRapporterDeveloppeur) {
		rapporterDeveloppeur = pRapporterDeveloppeur;
	} // Fin de setRapporterDeveloppeur(...).______________________________



	/**
	 * Getter du Boolean qui stipule si la méthode detecterCharsetReel(
	 * Integer, File) doit générer un rapport 
	 * pour les lignes avec détection OK.<br/>
	 * <br/>
	 *
	 * @return rapporterLignesOK : Boolean : 
	 * this.rapporterLignesOK.<br/>
	 */
	public static Boolean getRapporterLignesOK() {
		return rapporterLignesOK;
	} // Fin de getRapporterLignesOK().____________________________________



	/**
	* Setter du Boolean qui stipule si la méthode detecterCharsetReel(
	* Integer, File) doit générer un rapport 
	* pour les lignes avec détection OK.<br/>
	* <br/>
	*
	* @param pRapporterLignesOK : Boolean : 
	* valeur à passer à this.rapporterLignesOK.<br/>
	*/
	public static void setRapporterLignesOK(
			final Boolean pRapporterLignesOK) {
		rapporterLignesOK = pRapporterLignesOK;
	} // Fin de setRapporterLignesOK(...)._________________________________



	/**
	 * Getter du Boolean qui stipule si la méthode detecterCharsetReel(
	 * Integer, File) doit générer un rapport 
	 * pour les lignes avec détection KO.<br/>
	 * <br/>
	 *
	 * @return rapporterLignesKO : Boolean : 
	 * this.rapporterLignesKO.<br/>
	 */
	public static Boolean getRapporterLignesKO() {
		return rapporterLignesKO;
	} // Fin de getRapporterLignesKO().____________________________________



	/**
	* Setter du Boolean qui stipule si la méthode detecterCharsetReel(
	* Integer, File) doit générer un rapport 
	* pour les lignes avec détection KO.<br/>
	* <br/>
	*
	* @param pRapporterLignesKO : Boolean : 
	* valeur à passer à this.rapporterLignesKO.<br/>
	*/
	public static void setRapporterLignesKO(
			final Boolean pRapporterLignesKO) {
		rapporterLignesKO = pRapporterLignesKO;
	} // Fin de setRapporterLignesKO(...)._________________________________


	
	/**
	 * Getter du Boolean qui détermine si un 
	 * fichier déjà totalement encodé 
	 * dans un Charset donné doit être recopié 
	 * à un autre emplacement.<br/>
	 * Par exemple, "HITDIRA2016.txt" déjà totalement encodé en ANSI 
	 * sera recopié sous 
	 * "1_Fichiers_originaux_encodés_en_ANSI/HITDIRA2016_ANSI.txt".<br/>
	 * <br/>
	 *
	 * @return copierFichierTotalementEncode : Boolean.<br/>
	 */
	public static Boolean getCopierFichierTotalementEncode() {
		return copierFichierTotalementEncode;
	} // Fin de getCopierFichierTotalementEncode().________________________


	
	/**
	* Setter du Boolean qui détermine si un 
	* fichier déjà totalement encodé 
	* dans un Charset donné doit être recopié 
	* à un autre emplacement.<br/>
	* Par exemple, "HITDIRA2016.txt" déjà totalement encodé en ANSI 
	* sera recopié sous 
	* "1_Fichiers_originaux_encodés_en_ANSI/HITDIRA2016_ANSI.txt".<br/>
	* <br/>
	*
	* @param pCopierFichierTotalementEncode : Boolean : 
	* valeur à passer à copierFichierTotalementEncode.<br/>
	*/
	public static void setCopierFichierTotalementEncode(
			final Boolean pCopierFichierTotalementEncode) {
		copierFichierTotalementEncode = pCopierFichierTotalementEncode;
	} // Fin de setCopierFichierTotalementEncode(...)._____________________

	

} // FIN DE LA CLASSE Transcodeur.-------------------------------------------
