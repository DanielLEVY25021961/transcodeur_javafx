package levy.daniel.application.model.services.utilitaires.transcodeur.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.IFournisseurFichiersService;
import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.impl.FournisseurFichiersTraficService;
import levy.daniel.application.model.utilitaires.transcodeur.ITranscodeur;
import levy.daniel.application.model.utilitaires.transcodeur.impl.Transcodeur;

/**
 * CLASSE TranscodeurTraficService :<br/>
 * SERVICE pour les Transcodeur.<br/>
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
 * @author daniel.levy Lévy
 * @version 1.0
 * @since 29 mai 2018
 *
 */
public class TranscodeurTraficService {

	// ************************ATTRIBUTS************************************/

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
	 * CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR : String :<br/>
	 * Charset supposé pour le fichier en entrée en dur.<br/>
	 * N'est utilisé que si l'application ne peut lire le Charset 
	 * indiqué dans preferences.properties.<br/>
	 * "Windows-1252".<br/>
	 */
	public static final String CHARSET_STRING_ENTREE_PAR_DEFAUT_EN_DUR 
		= "Windows-1252";

	/**
	 * SAUT_LIGNE_JAVA : char :<br/>
	 * '\n'.<br/>
	 */
	public static final char SAUT_LIGNE_JAVA = '\n';
	
	/**
	 * fournisseurFichiers : IFournisseurFichiersService :<br/>
	 * SERVICE Fournisseur de Files abstraits pour les fichiers 
	 * intermédiaires et transcodés.<br/>
	 */
	private IFournisseurFichiersService fournisseurFichiers 
		= new FournisseurFichiersTraficService();

	/**
	 * transcodeur : ITranscodeur :<br/>
	 * Transcodeur.<br/>
	 */
	private ITranscodeur transcodeur = new Transcodeur();
		
	/**
	 * fileATranscoder : File :<br/>
	 * Fichier à transcoder en charsetSortie.<br/>
	 */
	private File fileATranscoder;
	
	/**
	 * charsetSupposeEntree : Charset :<br/>
	 * Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 */
	private Charset charsetSupposeEntree;

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
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG 
		= LogFactory.getLog(TranscodeurTraficService.class);

	// *************************METHODES************************************/
	
	
	 /**
	 * method CONSTRUCTEUR TranscodeurTraficService() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public TranscodeurTraficService() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	

	
	/**
	 * <b>Transcode un fichier Trafic</b> "pFileATranscoder" 
	 * supposé encodé en ANSI en un fichier 
	 * <b>"this.fichierSortie" transcodé en UTF8</b>.<br/>
	 * Génère si nécessaire (fichier "pFileATranscoder" non 
	 * entièrement encodé 
	 * avec le même Charset ANSI) un fichier intermédiaire 
	 * "this.fileEntierementEncodeEntree" entièrement encodé en 
	 * ANSI.<br/>
	 * <ul>
	 * <li>délègue à un SERVICE IFournisseurFichiersService 
	 * la détermination des fichiers intermédiaires et de transcodage.</li>
	 * <li>délègue à un MODEL ITranscodeur la tâche de transcodage.</li>
	 * <li>alimente les attributs de la classe.</li>
	 * <li>retourne le fichier transcodé "this.fichierSortie".</li>
	 * <li>Alimente les rapports utilisateurs et développeurs.</li>
	 * </ul>
	 * <br/>
	 *
	 * @param pFileATranscoder : File : fichier existant en entrée.<br/>
	 * 
	 * @return File : this.fichierSortie : 
	 * le fichier "fichierSortie" entièrement transcodé en UTF8.<br/>
	 * 
	 * @throws Exception
	 */
	public final File transcoderAnsiEnUtf8(
			final File pFileATranscoder) throws Exception {
		
		return this.transcoder(
				pFileATranscoder, CHARSET_ANSI, CHARSET_UTF8);
		
	} // Fin de transcoderAnsiEnUtf8(...)._________________________________
	
	
	
	/**
	 * <b>Transcode un fichier Trafic</b> "pFileATranscoder" 
	 * supposé encodé en "pCharsetSupposeEntree" en un fichier 
	 * <b>"this.fichierSortie" transcodé en "this.charsetSortie"</b>.<br/>
	 * Génère si nécessaire (fichier "pFileATranscoder" non 
	 * entièrement encodé 
	 * avec le même Charset) un fichier intermédiaire 
	 * "this.fileEntierementEncodeEntree" entièrement encodé en 
	 * "pCharsetSupposeEntree".<br/>
	 * <ul>
	 * <li>délègue à un SERVICE IFournisseurFichiersService 
	 * la détermination des fichiers intermédiaires et de transcodage.</li>
	 * <li>délègue à un MODEL ITranscodeur la tâche de transcodage.</li>
	 * <li>alimente les attributs de la classe.</li>
	 * <li>retourne le fichier transcodé "this.fichierSortie".</li>
	 * <li>Alimente les rapports utilisateurs et développeurs.</li>
	 * </ul>
	 * <br/>
	 *
	 * @param pFileATranscoder : File : fichier existant en entrée.<br/>
	 * @param pCharsetSupposeEntree : Charset : Charset avec lequel 
	 * on suppose que pFileATranscoder a été encodé à l'origine.<br/>
	 * @param pCharsetSortie : Charset : Charset dans lequel on encode 
	 * le fichier transcodé de sortie.<br/>
	 * 
	 * @return File : this.fichierSortie : 
	 * le fichier "fichierSortie" entièrement transcodé en charsetSortie.<br/>
	 * 
	 * @throws Exception
	 */
	public final File transcoder(
			final File pFileATranscoder
				, final Charset pCharsetSupposeEntree
						, final Charset pCharsetSortie) 
										throws Exception {
		
		// OBTENTION DES FICHIERS INTERMEDIAIRE ET TRANSCODE 
		// AUPRES DU SERVICE FOURNISSEUR DE FICHIERS.
		final File fichierIntermediaire 
			= this.fournisseurFichiers
				.fournirFile(
						pFileATranscoder
							, this.fournirSuffixe(
									pCharsetSupposeEntree));
		
		final File fichierSortieLocal 
			= this.fournisseurFichiers
				.fournirFile(
						pFileATranscoder
							, this.fournirSuffixe(
									pCharsetSortie));
		
		// ALIMENTATION DES ATTRIBUTS.
		this.fileATranscoder 
			= pFileATranscoder;
		this.charsetSupposeEntree 
			= this.determinerCharsetEcriture(pCharsetSupposeEntree);
		this.fileEntierementEncodeEntree 
			= fichierIntermediaire;
		this.charsetSortie 
			= this.determinerCharsetLecture(pCharsetSortie);
		this.fichierSortie 
			= fichierSortieLocal;
		
		// APPEL DE L'OBJET METIER TRANSCODEUR.
		final File fileResultat = 
				this.transcodeur.transcoder(
						this.fileATranscoder
							, this.charsetSupposeEntree
								, this.fileEntierementEncodeEntree
									, this.charsetSortie
										, this.fichierSortie);
		
		// ALIMENTE LES RAPPORTS UTILISATEUR ET DEVELOPPEUR.
		this.rapportUtilisateur
			.addAll(this.transcodeur.getRapportUtilisateur());
		this.rapportDeveloppeur
			.addAll(this.transcodeur.getRapportDeveloppeur());
		
		return fileResultat;
		
	} // Fin de transcoder(...).___________________________________________
	

	
	
	/**
	 * <b>Transcode l'ensemble des fichiers de Trafic 
	 * contenus dans un répertoire</b> "pRepATranscoder" 
	 * supposés encodés en ANSI en fichiers 
	 * transcodés en UTF8.<br/>
	 * - skip les sous-répertoires.<br/>
	 * - retourne null si pRepATranscoder == null.<br/>
	 * - retourne null si pRepATranscoder n'est pas un répertoire.<br/>
	 * <br/>
	 *
	 * @param pRepATranscoder : File : Répertoire contenant 
	 * les fichiers simples existants en entrée encodés en ANSI.<br/>
	 * 
	 * @return List&lt;File&gt; : liste des fichiers 
	 * transcodés en UTF8.<br/>
	 * 
	 * @throws Exception
	 */
	public final List<File> transcoderRepertoireANsiEnUtf8(
						final File pRepATranscoder)	 throws Exception {
		
		return this.transcoderRepertoire(
				pRepATranscoder, CHARSET_ANSI, CHARSET_UTF8);
		
	} // Fin de transcoderRepertoireANsiEnUtf8(...)._______________________
	
	
	
	/**
	 * <b>Transcode l'ensemble des fichiers de Trafic 
	 * contenus dans un répertoire</b> "pRepATranscoder" 
	 * supposés encodés en "pCharsetSupposeEntree" en fichiers 
	 * transcodés en "this.charsetSortie".<br/>
	 * - skip les sous-répertoires.<br/>
	 * - retourne null si pRepATranscoder == null.<br/>
	 * - retourne null si pRepATranscoder n'est pas un répertoire.<br/>
	 * <br/>
	 *
	 * @param pRepATranscoder : File : Répertoire contenant 
	 * les fichiers simples existants en entrée.<br/>
	 * @param pCharsetSupposeEntree : Charset : Charset avec lequel 
	 * on suppose que pFileATranscoder a été encodé à l'origine.<br/>
	 * @param pCharsetSortie : Charset : Charset dans lequel on encode 
	 * le fichier transcodé de sortie.<br/>
	 * 
	 * @return List&lt;File&gt; : liste des fichiers 
	 * transcodés en charsetSortie.<br/>
	 * 
	 * @throws Exception
	 */
	public final List<File> transcoderRepertoire(
			final File pRepATranscoder
				, final Charset pCharsetSupposeEntree
					, final Charset pCharsetSortie) throws Exception {
		
		/* retourne null si pRepATranscoder == null. */
		if (pRepATranscoder == null) {
			return null;
		}
		
		/* retourne null si pRepATranscoder n'est pas un répertoire. */
		if (!pRepATranscoder.isDirectory()) {
			return null;
		}
		
		final List<File> resultat = new ArrayList<File>();
		
		final DirectoryStream<Path> directoryStream 
			= Files.newDirectoryStream(pRepATranscoder.toPath());
		
		final Iterator<Path> ite = directoryStream.iterator();
		
		while (ite.hasNext()) {
			
			final File fichier = ite.next().toFile();
			
			/* skip les sous-répertoires. */
			if (fichier.isFile()) {
				
				// TRANSCODAGE.
				final File fichierTranscode 
					= this.transcoder(
							fichier
								, pCharsetSupposeEntree, pCharsetSortie);
				
				resultat.add(fichierTranscode);
				
			}
		}
		
		return resultat;
		
	} // Fin de transcoderRepertoire(...)._________________________________
	
	
	
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
	 * Retourne un Suffixe relatif à un Charset 
	 * pour les noms de fichier.<br/>
	 * <ul>
	 * Par exemple :
	 * <li><code>fournirSuffixe(CHARSET_UTF8)</code> retourne "UTF8".</li>
	 * <li><code>fournirSuffixe(CHARSET_ANSI)</code> retourne "ANSI".</li>
	 * <li><code>fournirSuffixe(CHARSET_IBM850)</code> retourne "IBM850".</li>
	 * </ul>
	 *
	 * @param pCharset : Charset.<br/>
	 * @return : String : Suffixe pour les noms de fichiers.<br/>
	 */
	private String fournirSuffixe(
			final Charset pCharset) {
		
		/* retourne null si pCharset == null. */
		if (pCharset == null) {
			return null;
		}
		
		if (pCharset.equals(CHARSET_UTF8)) {
			return "UTF8";
		} else if (pCharset.equals(CHARSET_ANSI)) {
			return "ANSI";
		} else if (pCharset.equals(CHARSET_IBM850)) {
			return "IBM850";
		} else {
			return pCharset.displayName(Locale.getDefault());
		}
				
	} // Fin de fournirSuffixe(...)._______________________________________
	

	
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
	 * Getter du SERVICE Fournisseur de Files 
	 * abstraits pour les fichiers 
	 * intermédiaires et transcodés.<br/>
	 * <br/>
	 *
	 * @return fournisseurFichiers : IFournisseurFichiersService.<br/>
	 */
	public IFournisseurFichiersService getFournisseurFichiers() {
		return this.fournisseurFichiers;
	} // Fin de getFournisseurFichiers().__________________________________


	
	/**
	* Setter du SERVICE Fournisseur de Files 
	* abstraits pour les fichiers 
	 * intermédiaires et transcodés.<br/>
	* <br/>
	*
	* @param pFournisseurFichiers : IFournisseurFichiersService : 
	* valeur à passer à fournisseurFichiers.<br/>
	*/
	public void setFournisseurFichiers(
			final IFournisseurFichiersService pFournisseurFichiers) {
		this.fournisseurFichiers = pFournisseurFichiers;
	} // Fin de setFournisseurFichiers(...)._______________________________


	
	/**
	 * Getter du Transcodeur.<br/>
	 * <br/>
	 *
	 * @return transcodeur : ITranscodeur.<br/>
	 */
	public ITranscodeur getTranscodeur() {
		return this.transcodeur;
	} // Fin de getTranscodeur().__________________________________________


	
	/**
	* Setter du Transcodeur.<br/>
	* <br/>
	*
	* @param pTranscodeur : ITranscodeur : 
	* valeur à passer à transcodeur.<br/>
	*/
	public void setTranscodeur(
			final ITranscodeur pTranscodeur) {
		this.transcodeur = pTranscodeur;
	} // Fin de setTranscodeur(...)._______________________________________



	/**
	 * Getter du Fichier intermédiaire entièrement encodé 
	 * en charsetSupposeEntree.<br/>
	 * <br/>
	 *
	 * @return : File : this.fileEntierementEncodeEntree.<br/>
	 */
	public File getFileEntierementEncodeEntree() {
		return this.fileEntierementEncodeEntree;
	} // Fin de getFileEntierementEncodeEntree().__________________________

	

	/**
	 * Setter du Fichier intermédiaire entièrement encodé 
	 * en charsetSupposeEntree.<br/>
	 * <br/>
	 *
	 * @param pFileEntierementEncodeEntree : File : 
	 * valeur à passer à this.fileEntierementEncodeEntree.<br/>
	 */
	public void setFileEntierementEncodeEntree(
			final File pFileEntierementEncodeEntree) {
		this.fileEntierementEncodeEntree = pFileEntierementEncodeEntree;
	} // Fin de setFileEntierementEncodeEntree(...)._______________________

	

	/**
	 * Getter du Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSupposeEntree : Charset : 
	 * this.charsetSupposeEntree.<br/>
	 */
	public Charset getCharsetSupposeEntree() {
		return this.charsetSupposeEntree;
	} // Fin de getCharsetSupposeEntree()._________________________________
	
	

	/**
	* Setter du Charset avec lequel on suppose que le 
	* fichier en entrée a été encodé.<br/>
	* <br/>
	*
	* @param pCharsetSupposeEntree : Charset : 
	* valeur à passer à this.charsetSupposeEntree.<br/>
	*/
	public void setCharsetSupposeEntree(
			final Charset pCharsetSupposeEntree) {
		this.charsetSupposeEntree = pCharsetSupposeEntree;
	} // Fin de setCharsetSupposeEntree(...).______________________________
	


	/**
	 * Getter du Fichier à transcoder en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fileATranscoder : File : this.fileATranscoder.<br/>
	 */
	public File getFileATranscoder() {
		return this.fileATranscoder;
	} // Fin de getFileATranscoder().______________________________________



	/**
	* Setter du Fichier à transcoder en charsetSortie.<br/>
	* <br/>
	*
	* @param pFileATranscoder : File : 
	* valeur à passer à this.fileATranscoder.<br/>
	*/
	public void setFileATranscoder(
			final File pFileATranscoder) {
		this.fileATranscoder = pFileATranscoder;
	} // Fin de setFileATranscoder(...).___________________________________
	

	
	/**
	 * Getter du Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSortie : Charset : this.charsetSortie.<br/>
	 */
	public Charset getCharsetSortie() {
		return this.charsetSortie;
	} // Fin de getCharsetSortie().________________________________________



	/**
	* Setter du Charset dans lequel on veut que le fichierSortie 
	* soit encodé.<br/>
	* <br/>
	*
	* @param pCharsetSortie : Charset : 
	* valeur à passer à this.charsetSortie.<br/>
	*/
	public void setCharsetSortie(
			final Charset pCharsetSortie) {
		this.charsetSortie = pCharsetSortie;
	} // Fin de setCharsetSortie(...)._____________________________________
	

	
	/**
	 * Getter du fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fichierSortie : File : this.fichierSortie.<br/>
	 */
	public File getFichierSortie() {
		return this.fichierSortie;
	} // Fin de getFichierSortie().________________________________________



	/**
	* Setter du fichier produit par le transcodeur entièrement 
	* encodé en charsetSortie.<br/>
	* <br/>
	*
	* @param pFichierSortie : File : 
	* valeur à passer à this.fichierSortie.<br/>
	*/
	public void setFichierSortie(
			final File pFichierSortie) {
		this.fichierSortie = pFichierSortie;
	} // Fin de setFichierSortie(...)/_____________________________________
	

	
	/**
	 * method getRapportUtilisateur() :<br/>
	 * Getter du rapport à l'attention des utilisateurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 * <br/>
	 *
	 * @return rapportUtilisateur : List&lt;String&gt;.<br/>
	 */
	public List<String> getRapportUtilisateur() {
		return this.rapportUtilisateur;
	} // Fin de getRapportUtilisateur().___________________________________


	
	/**
	 * method getRapportDeveloppeur() :<br/>
	 * Getter du rapport à l'attention des développeurs.<br/>
	 * NE PEUT PAS ETRE NULL, TESTER isEmpty().<br/>
	 * <br/>
	 *
	 * @return rapportDeveloppeur : List&lt;String&gt;.<br/>
	 */
	public List<String> getRapportDeveloppeur() {
		return this.rapportDeveloppeur;
	} // Fin de getRapportDeveloppeur().___________________________________



} // Fin de la CLASSE TranscodeurTraficService.------------------------------------
