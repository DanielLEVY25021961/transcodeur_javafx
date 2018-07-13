package levy.daniel.application.model.utilitaires.transcodeur;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



/**
 * INTERFACE ITranscodeur :<br/>
 * Interface chargée de factoriser les comportements des Transcodeur.<br/>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author dan Lévy
 * @version 1.0
 * @since 3 juin 2018
 *
 */
public interface ITranscodeur {

	
	/**
	 * CLASSE_TRANSCODEUR : String :<br/>
	 * "Classe Transcodeur".<br/>
	 */
	String CLASSE_TRANSCODEUR = "Classe Transcodeur";


	/**
	 * METHODE_TRANSCODER : String :<br/>
	 * "Méthode transcoder(...)".<br/>
	 */
	String METHODE_TRANSCODER = "Méthode transcoder(...)";


	/**
	 * SEP_MOINS_AERE : String :<br/>
	 * " - ".<br/>
	 */
	String SEP_MOINS_AERE = " - ";


	/**
	 * AIT_ETE_ENCODEE : String :<br/>
	 * " ait été encodée ".<br/>
	 */
	String AIT_ETE_ENCODEE = " ait été encodée ";


	/**
	 * AVEC_LE_CHARSET : String :<br/>
	 * "avec le Charset ".<br/>
	 */
	String AVEC_LE_CHARSET = "avec le Charset ";


	/**
	 * DU_FICHIER : String :<br/>
	 * " du fichier ".<br/>
	 */
	String DU_FICHIER = " du fichier ";


	/**
	 * SAUT_LIGNE_JAVA : char :<br/>
	 * '\n'.<br/>
	 */
	char SAUT_LIGNE_JAVA = '\n';


	/**
	 * LOCALE_FR_FR : Locale :<br/>
	 * new Locale("fr", "FR").<br/>
	 */
	Locale LOCALE_FR_FR = new Locale("fr", "FR");


	/**
	 * CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR : String :<br/>
	 * Charset supposé pour le fichier en entrée en dur.<br/>
	 * N'est utilisé que si l'application ne peut lire le Charset 
	 * indiqué dans preferences.properties.<br/>
	 * "Windows-1252".<br/>
	 */
	String CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR = "Windows-1252";


	/**
	 * CHARSET_UTF8 : Charset :<br/>
	 * Charset.forName("UTF-8").<br/>
	 * Eight-bit Unicode (or UCS) Transformation Format.<br/> 
	 */
	Charset CHARSET_UTF8 = Charset.forName("UTF-8");


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
	Charset CHARSET_ANSI = Charset.forName("windows-1252");


	/**
	 * CHARSET_IBM850 : Charset :<br/>
	 * Charset IBM-850.<br/>
	 * Cp850, MS-DOS Latin-1.<br/>
	 */
	Charset CHARSET_IBM850 = Charset.forName("IBM-850");


	/**
	 * CARACTERES_INDESIRABLES_SET : Set&lt;Character&gt; :<br/>
	 * Set contenant des caractères indésirables 
	 * (impossibles à écrire simplement au clavier).<br/>
	 */
	Set<Character> CARACTERES_INDESIRABLES_SET = new HashSet<Character>();


	/**
	 * charsetsDisponibles : Set<Charset> :<br/>
	 * .<br/>
	 */
	Set<Charset> CHARSETS_DISPONIBLES = new HashSet<Charset>();



	/**
	 * <b>Transcode</b> intégralement le fichier pFileATranscoder 
	 * <i>(supposé encodé à l'origine en pCharsetSupposeEntree)</i> 
	 * en <b>pCharsetSortie</b>.<br/>
	 * Le résultat transcodé est disponible dans pFileSortie.<br/>
	 * <ul>
	 * <li>essaie d'abord de décoder pFileATranscoder 
	 * avec pCharsetSupposeEntree.</li>
	 * <li>décode avec CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
	 * si pCharsetSupposeEntree == null.</li>
	 * <li><b>Vérifie si pFileATranscoder est réellement entièrement 
	 * encodé avec pCharsetSupposeEntree</b>.<br/>
	 * <b>Détecte l'encodage réel de chaque ligne</b> si 
	 * ce n'est pas le cas.
	 * </li>
	 * <li>Crée si nécessaire sur disque un fichier 
	 * pFileEntierementEncodeEntree dont 
	 * toutes les lignes sont entièrement encodées en 
	 * pCharsetSupposeEntree.</li>
	 * <li>Transcode ce fichier pFileEntierementEncodeEntree 
	 * en pCharsetSortie.</li>
	 * <li>transcode en CHARSET_UTF8 si pCharsetSortie == null.</li>
	 * </ul>
	 * - retourne null si le fichier en entrée pFileATranscoder 
	 * ne convient pas (null, inexistant, répertoire, vide).<br/>
	 * - retourne null si pFileEntierementEncodeEntree == null.<br/>
	 * - retourne null si pFileSortie == null.<br/>
	 * <br/>
	 *
	 * @param pFileATranscoder : File : fichier à transcoder.<br/>
	 * @param pCharsetSupposeEntree : Charset : 
	 * Charset pour le decodage de pFileATranscoder.
	 * @param pFileEntierementEncodeEntree : File : 
	 * Fichier intermédiaire entièrement encodé 
	 * en charsetSupposeEntree.<br/>
	 * @param pCharsetSortie : Charset : charset résultat.<br/>
	 * @param pFileSortie : File : fichier sur disque dur contenant 
	 * le résultat du transcodage.<br/>
	 * 
	 * @return : File : le fichier pFileSortie transcodé 
	 * en pCharsetSortie.<br/>
	 * 
	 * @throws Exception 
	 */
	File transcoder(File pFileATranscoder
			, Charset pCharsetSupposeEntree
				, File pFileEntierementEncodeEntree
					, Charset pCharsetSortie
						, File pFileSortie) throws Exception;


	/**
	 * Fournit une String pour l'affichage 
	 * d'une Map&lt;Integer, String&gt;.<br/>
	 * <br/>
	 * - retourne null si pMap == null.<br/>
	 * <br/>
	 *
	 * @param pMap : Map&lt;Integer, String&gt;.<br/>
	 * 
	 * @return : String.<br/>
	 */
	String afficherMapIntegerString(Map<Integer, String> pMap);
	

	/**
	 * Fournit une String pour l'affichage 
	 * d'une Map&lt;Integer, Charset&gt;.<br/>
	 * <br/>
	 * - retourne null si pMap == null.<br/>
	 * <br/>
	 *
	 * @param pMap : Map&lt;Integer, Charset&gt;.<br/>
	 * 
	 * @return : String.<br/>
	 */
	String afficherMapIntegerCharset(Map<Integer, Charset> pMap);


	/**
	 * Fournit une String pour l'affichage 
	 * d'une List&lt;String&gt;.<br/>
	 * <br/>
	 * - retourne null si pList == null.<br/>
	 * <br/>
	 *
	 * @param pList : List&lt;String&gt;
	 * 
	 * @return : String.<br/>
	 */
	String afficherListeString(List<String> pList);


	/**
	 * Fournit une String pour l'affichage 
	 * d'une Entry&lt;Integer, Charset&gt;.<br/>
	 * <br/>
	 * - retourne null si pEntry == null.<br/>
	 * <br/>
	 * 
	 * @param pEntry : Entry&lt;Integer, Charset&gt;.<br/>
	 * 
	 * @return : String.<br/>
	 */
	String afficherEntry(Entry<Integer, Charset> pEntry);


	/**
	 * Getter du Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSupposeEntree : Charset : 
	 * this.charsetSupposeEntree.<br/>
	 */
	Charset getCharsetSupposeEntree();


	/**
	* Setter du Charset avec lequel on suppose que le 
	* fichier en entrée a été encodé.<br/>
	* <br/>
	*
	* @param pCharsetSupposeEntree : Charset : 
	* valeur à passer à this.charsetSupposeEntree.<br/>
	*/
	void setCharsetSupposeEntree(Charset pCharsetSupposeEntree);


	/**
	 * Getter du Fichier à transcoder en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fileATranscoder : File : this.fileATranscoder.<br/>
	 */
	File getFileATranscoder();


	/**
	* Setter du Fichier à transcoder en charsetSortie.<br/>
	* <br/>
	*
	* @param pFileATranscoder : File : 
	* valeur à passer à this.fileATranscoder.<br/>
	*/
	void setFileATranscoder(File pFileATranscoder);


	/**
	 * Getter du Fichier intermédiaire entièrement encodé 
	 * en charsetSupposeEntree.<br/>
	 * <br/>
	 *
	 * @return fileEntierementEncodeEntree : File.<br/>
	 */
	File getFileEntierementEncodeEntree();


	/**
	* Setter du Fichier intermédiaire entièrement encodé 
	* en charsetSupposeEntree.<br/>
	* <br/>
	*
	* @param pFileEntierementEncodeEntree : File : 
	* valeur à passer à fileEntierementEncodeEntree.<br/>
	*/
	void setFileEntierementEncodeEntree(File pFileEntierementEncodeEntree);


	/**
	 * Getter du Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSortie : Charset : this.charsetSortie.<br/>
	 */
	Charset getCharsetSortie();


	/**
	* Setter du Charset dans lequel on veut que le fichierSortie 
	* soit encodé.<br/>
	* <br/>
	*
	* @param pCharsetSortie : Charset : 
	* valeur à passer à this.charsetSortie.<br/>
	*/
	void setCharsetSortie(Charset pCharsetSortie);


	/**
	 * Getter du fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fichierSortie : File : this.fichierSortie.<br/>
	 */
	File getFichierSortie();


	/**
	* Setter du fichier produit par le transcodeur entièrement 
	* encodé en charsetSortie.<br/>
	* <br/>
	*
	* @param pFichierSortie : File : 
	* valeur à passer à this.fichierSortie.<br/>
	*/
	void setFichierSortie(File pFichierSortie);


	/**
	 * method getRapportUtilisateur() :<br/>
	 * Getter du rapport à l'attention des utilisateurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 * <br/>
	 *
	 * @return rapportUtilisateur : List&lt;String&gt;.<br/>
	 */
	List<String> getRapportUtilisateur();


	/**
	 * method getRapportDeveloppeur() :<br/>
	 * Getter du rapport à l'attention des développeurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 * <br/>
	 *
	 * @return rapportDeveloppeur : List&lt;String&gt;.<br/>
	 */
	List<String> getRapportDeveloppeur();
	
	

} // FIN DE L'INTERFACE ITranscodeur.----------------------------------------
